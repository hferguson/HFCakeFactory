package ca.hferguson.spring.payment;


import com.paypal.core.*;
import com.paypal.http.HttpResponse;
import com.paypal.http.serializer.Json;
import com.paypal.orders.*;

import ca.hferguson.spring.bean.Basket;
import ca.hferguson.spring.bean.BasketEntry;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements IPaymentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
	
	@Autowired
	private PayPalEnvironment payPalEnv;
	private PayPalHttpClient ppClient;
	
	private String returnURI;
	private String cancelURI;
	private String paypalDefAction = "PAY_NOW";
	private boolean debug = true;
	
	public PaymentService(PayPalEnvironment env)  {
		//String clientID = System.getProperty("ppClientId");
		//String clientSecret = System.getProperty("ppClientSecret");
		returnURI = "/order/complete";
		cancelURI = "/order/cancel";
		
		// TODO: Toggle this to use "live" based on settings
		this.payPalEnv = env;
		
		if (payPalEnv == null) {
			
			LOGGER.warn("No PP Environment!!");
		}
		
	}
	
	public PayPalHttpClient client() {
		if (this.ppClient == null)
			this.ppClient = new PayPalHttpClient(payPalEnv);
	    return this.ppClient;
	}
	
	@Override
	public PaymentConfirmation captureOrder(String orderID) throws IOException {
		PaymentConfirmation pConf = new PaymentConfirmation();
		OrdersCaptureRequest request = new OrdersCaptureRequest(orderID);
		request.requestBody(new OrderRequest());
		HttpResponse<Order> response = client().execute(request);
		Order order = response.result();
		Payer buyer = response.result().payer();
		if (order != null) {

			pConf.setOrderID(orderID);
			pConf.setStatus(order.status());
			pConf.setPayerID(order.payer().payerId());
			pConf.setPayerEmail(buyer.email());
			pConf.setPaymentDate(new Date().toString());

		}
		if (debug && order != null) {
			log("Status Code: " + response.statusCode());
			log("Status: " + response.result().status());
			log("Order ID: " + response.result().id());
			log("Links: ");
			for (LinkDescription link : response.result().links()) {
				log("\t" + link.rel() + ": " + link.href());
			}
			log("Capture ids:");
			for (PurchaseUnit purchaseUnit : response.result().purchaseUnits()) {
				for (Capture capture : purchaseUnit.payments().captures()) {
					log("\t" + capture.id());
				}
			}
			log("Buyer: ");
			
			log("\tEmail Address: " + buyer.email());
			log("\tName: " + buyer.name().givenName() + " " + buyer.name().surname());
			log("Full response body:");
			log(new JSONObject(new Json().serialize(response.result())).toString(4));
		}
		return pConf;
	}
	/**
	 * Method to create order - taken from Paypal sample SDK
	 *
	 * @param debug true = print response data
	 * @return HttpResponse<Order> response received from API
	 * @throws IOException Exceptions from API if any
	 */
	@Override
	public OrderDetails createOrder(OrderDetails order, String baseURL) throws IOException {
		

		Order ackOrder = null;
		OrdersCreateRequest request = new OrdersCreateRequest();
		request.header("prefer","return=representation");
		request.requestBody(buildRequestBody(order, baseURL));
		HttpResponse<Order> response = client().execute(request);
		if (response == null)
			error("NULL Response from PP API!!");
		if (response.statusCode() != 201)
			error(String.format("Unexpected status code %d", response.statusCode()));
		ackOrder = response.result();
		order.setPaypalOrderID(ackOrder.id());
		order.setPaypalStatus(ackOrder.status());
		order.setPaypalNextURL(this.getApproveLink(ackOrder));
		if (debug) {
			
			log("Status Code: " + response.statusCode());
			log("Status: " + response.result().status());
			if (response.statusCode() == 201) {
				
				log("Order ID: " + response.result().id());
				log("Intent: " + response.result().checkoutPaymentIntent());
				log("Links: ");
				for (LinkDescription link : response.result().links()) {
					log("\t" + link.rel() + ": " + link.href() + "\tCall Type: " + link.method());
				}
				log("Total Amount: " + response.result().purchaseUnits().get(0).amountWithBreakdown().currencyCode()
						+ " " + response.result().purchaseUnits().get(0).amountWithBreakdown().value());
				log("Full response body:");
				log(new JSONObject(new Json().serialize(response.result())).toString(4));
			}
		}
		
		return order;
	}
	
	@Override
	public String getApproveLink(Order order) {
		return getLink(order.links(), "approve");
	}
	
	private String getLink(List<LinkDescription> links, String linkName) {
		String url = "";
		for (LinkDescription link : links) {
			if (link.rel().equalsIgnoreCase(linkName)) {
				url = link.href();
				break;
			}
		}
		return url;
	}
	/**
	 * This method pulled from paypal sample code.
	 * Let the refactoring begin. Need to pass our own DTO(s) in to 
	 * represent the items being bought (the purchase units)
	 * @return
	 */
	private OrderRequest buildRequestBody(OrderDetails theOrder, String baseURL) {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.checkoutPaymentIntent("CAPTURE");

		ApplicationContext applicationContext = new ApplicationContext().brandName("HF Cake Factory").landingPage("BILLING")
				.cancelUrl(buildURL(baseURL, cancelURI)).returnUrl(buildURL(baseURL, returnURI)).userAction(this.paypalDefAction)
				.shippingPreference("SET_PROVIDED_ADDRESS");
		orderRequest.applicationContext(applicationContext);

		List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<PurchaseUnitRequest>();
		PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().referenceId("PUHFCF")
				.description("Baked Goods").customId("CUST-HFCF").softDescriptor("CakeF actory")
				.amountWithBreakdown(getAmountWithBreakdown(theOrder))
				.items(convertCartItems(theOrder))
				.shippingDetail(new ShippingDetail().name(new Name().fullName(String.format("%s %s", theOrder.getFirstName(), theOrder.getLastName())))
						.addressPortable(getShippingAddress(theOrder)));
		purchaseUnitRequests.add(purchaseUnitRequest);
		orderRequest.purchaseUnits(purchaseUnitRequests);
		if (debug )
			log("OrderRequest object: " + orderRequest.toString());
		return orderRequest;
	}
	
	private String buildURL(String baseURL, String uri) {
		String url = baseURL;
		if (!url.endsWith("/") && !uri.startsWith("/")) {
			url += "/";
		}
		url += uri;
		return url;
	}
	private AddressPortable getShippingAddress(OrderDetails order) {
		AddressPortable ap = new AddressPortable();
		ap.addressLine1(order.getAddrLine1()).addressLine2(order.getAddrLine2())
		.adminArea1(order.getAdminArea1()).adminArea2(order.getAdminArea2())
		.postalCode(order.getPostalCode())
		.countryCode("CA");
		return ap;
	}
	private AmountWithBreakdown getAmountWithBreakdown(OrderDetails order) {
		AmountWithBreakdown awb = new AmountWithBreakdown();
		BigDecimal subTotal = itemTotal(order);
		BigDecimal shipTotal = getShippingPrice(order);
		BigDecimal handTotal = getHandlingPrice(order);
		BigDecimal taxTotal = getTaxOnOrder(order);
		BigDecimal total = subTotal.add(shipTotal).add(handTotal).add(taxTotal);
		
		awb.currencyCode(getCurrency());
		awb.value(total.toPlainString());
		awb.amountBreakdown(new AmountBreakdown().itemTotal(bigDecimalToMoney(subTotal))
				.shipping(bigDecimalToMoney(shipTotal))
				.handling(bigDecimalToMoney(handTotal))
				.taxTotal(bigDecimalToMoney(taxTotal)));
	
			
		return awb;
	}
	
	private Money bigDecimalToMoney(BigDecimal bd) {
		Money money = new Money().currencyCode(getCurrency());
		money = money.value(bd.toPlainString());
		return money;
	}
	// Fake this out for now
	private BigDecimal getShippingPrice(OrderDetails order) {
		return new BigDecimal(0);
	}
	
	private BigDecimal getHandlingPrice(OrderDetails order) {
		return new BigDecimal(0);
	}
	
	private BigDecimal getTaxOnOrder(OrderDetails order) {
		return new BigDecimal(0);
	}
	
	private BigDecimal itemTotal(OrderDetails order) {
		
		BigDecimal amount = Basket.getSumOfEntries(order.getItems());
		
		return amount;
	}
	/**
	 * get items from shopping cart and apply to Paypal items list
	 * I passed in the address for taxes calculation later
	 * @param entries
	 * @param destAddress
	 * @return
	 */
	private List<Item> convertCartItems(OrderDetails order) {
		List<BasketEntry> entries = order.getItems();
		List<Item> items = new ArrayList<Item>();
		Iterator<BasketEntry> iter = entries.iterator();
		while (iter.hasNext()) {
			BasketEntry be = iter.next();
			Item item = new Item().name(be.getItem().getTitle())
								.description(be.getItem().getDescription())
								.sku(be.getSku())
								.unitAmount(toPPAmount(getCurrency(), be.getItem().getPrice()))
								.tax(getTax(be))
								.quantity(String.valueOf(be.getQuantity()))
								.category("PHYSICAL_GOODS");
			items.add(item);					 
		}	
		return items;
	}
	
	private String getCurrency() {
		return "CAD";
	}
	
	private Money getTax(BasketEntry entry) {
		// default to just return empty money item
		return toPPAmount(getCurrency(), new BigDecimal(0));
	}
	private Money toPPAmount(String currency, BigDecimal price) {
		Money money = new Money();
		money.currencyCode(currency);
		money.value(price.toPlainString());
		
		return money;
	}
	private void log(String msg) {
		LOGGER.info(msg);
	}
	
	private void error(String msg) {
		LOGGER.error(msg);
	}
}
