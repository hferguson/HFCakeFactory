package ca.hferguson.spring.controllers;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ca.hferguson.spring.bean.BasketEntry;
import ca.hferguson.spring.bean.IBasket;
import ca.hferguson.spring.order.*;
import ca.hferguson.spring.service.IMemberService;


@Controller
public class OrderController extends BaseController {

	
	//private final IBasket shoppingCart;
	private final ApplicationEventPublisher eventPublisher;
	
	public OrderController(IBasket basket, IMemberService service, ApplicationEventPublisher publisher) {
		super(basket, service);
		this.shoppingCart = basket;
		this.eventPublisher = publisher;
	}
	
	@PostMapping("/order/complete")
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
}
