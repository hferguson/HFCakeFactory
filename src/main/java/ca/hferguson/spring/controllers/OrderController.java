package ca.hferguson.spring.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ca.hferguson.spring.bean.BasketEntry;
import ca.hferguson.spring.bean.IBasket;
import ca.hferguson.spring.order.*;
import ca.hferguson.spring.payment.IPaymentService;
import ca.hferguson.spring.payment.OrderDetails;
import ca.hferguson.spring.payment.PaymentConfirmation;
import ca.hferguson.spring.service.IMemberService;


@Controller
public class OrderController extends BaseController {
	public static String ATTR_PP_ORDER_INFO = "PP_ORDER_DETAILS";
	private IPaymentService paymentService;
	
	//private final IBasket shoppingCart;
	private final ApplicationEventPublisher eventPublisher;
	
	public OrderController(IBasket basket, IMemberService service, IPaymentService payServ, ApplicationEventPublisher publisher) {
		super(basket, service);
		this.paymentService = payServ;
		this.eventPublisher = publisher;
	}
	
	@PostMapping("/order/payment")
	public String getPayment(final Model model, 
							HttpSession session, 
							HttpServletRequest request, 
							HttpServletResponse response, 
							@RequestBody MultiValueMap<String, String> formData) {
		String forwardURL = "/error";
		OrderDetails order = this.buildOrderForPayment(formData);
		log("Entering Paypal Payment section");
		try {
			
			order = paymentService.createOrder(order, this.getBaseUrl(request));
			if (order != null) {
				String ppOrderReceipt = order.getPaypalOrderID();
				log("Order created, receipt is " + ppOrderReceipt);
				forwardURL = order.getPaypalNextURL();
				log("Forwarding URL is " + forwardURL);
				//session.setAttribute("PP_ORDER_ID", ppOrderReceipt);
				session.setAttribute(ATTR_PP_ORDER_INFO, order);
			} else {
				error("Order was null");
			}
		} catch (Exception e) {
			// handle exception(?)
			error("Exception occurred while creating order with Paypal");
			e.printStackTrace();
		}
		return "redirect:" + forwardURL;
	}
	@GetMapping("/order/complete")
	public String orderPage(@RequestParam Map<String,String> allRequestParams, final Model model, HttpSession session) {
		String token = allRequestParams.get("token");
		//String payerID = allRequestParams.get("PayerID");
		String errMsg = "";
		OrderDetails order = (OrderDetails) session.getAttribute(ATTR_PP_ORDER_INFO);
		boolean ok = true;
		
		log("Complete order GET called");
				
		if (order == null) {
			ok = false;
			errMsg = "Unable to get order details from session";
		} else if (!order.getPaypalOrderID().contentEquals(token)) {
			ok = false;
			errMsg = "Got invalid token from paypal";
		}
		if (ok) {
			try {
				PaymentConfirmation pConf = paymentService.captureOrder(token);
				sendOrder(order);
				setupModel(model, "");
				model.addAttribute("orderInfo", pConf);
				log(String.format("Retrieved order %s from session", order.getPaypalOrderID()));
				
			} catch(Exception e) {
				e.printStackTrace();
				ok = false;
				errMsg = e.getMessage();
			}
		} 
		if (!ok)
			error(errMsg);
		return "order-complete";
	}
	
	private void sendOrder(OrderDetails details) {
		OrderReceipt receipt = new OrderReceipt(new java.util.Date());
		OrderReceivedEvent event = new OrderReceivedEvent(details.getAddrLine1(), details.getAdminArea1(), details.getPostalCode(), this.shoppingCart.cartInventory(), receipt);
		eventPublisher.publishEvent(event);
		if (receipt.getOrderStatus() == OrderReceipt.ORDER_RECEIVED) {
			log("Emptying cart...");
			shoppingCart.emptyCart();
		}
	}
	//@PostMapping("/order/complete")
	/*
	public String completeOrder(final Model model, @RequestBody MultiValueMap<String, String> formData ) {
		
		log("Completing order...");
		//List<String> skuList = shoppingCart.entriesToList();
		List<BasketEntry> entries = shoppingCart.cartInventory();
		String addr1 = formData.getFirst("addr1");
		String addr2 = formData.getFirst("addr2");
		String pCode = formData.getFirst("postcode");
		
		//Order order = new Order(skuList, addr1, addr2, pCode);
		OrderReceipt receipt = new OrderReceipt(new java.util.Date());
		OrderReceivedEvent event = new OrderReceivedEvent(addr1, addr2, pCode, entries, receipt);
		eventPublisher.publishEvent(event);
		
		if (receipt.getOrderStatus() == OrderReceipt.ORDER_RECEIVED) {
			shoppingCart.emptyCart();
		}
		// Move the model set up here so that it picks up the change in basket
		setupModel(model, "");
		model.addAttribute("orderID", receipt.getOrderUID());
		model.addAttribute("orderDate", receipt.getOrderDate());
		model.addAttribute("orderStatus", receipt.getOrderStatus());
		//model.addAttribute("order", order);
		
		return "order-complete";
	}
	*/
	private OrderDetails buildOrderForPayment(MultiValueMap<String, String> formData) {
		OrderDetails newOrder = new OrderDetails();
		List<BasketEntry> entries = shoppingCart.cartInventory();
		
		newOrder.setFirstName(formData.getFirst("fname"));
		newOrder.setLastName(formData.getFirst("lname"));
		newOrder.setAddrLine1(formData.getFirst("addr1"));
		
		newOrder.setAdminArea1(formData.getFirst("city"));
		newOrder.setAdminArea2(formData.getFirst("stateProv"));
		newOrder.setPostalCode(formData.getFirst("postcode"));
		
		newOrder.setItems(entries);
		
		return newOrder;
	}
	
}
