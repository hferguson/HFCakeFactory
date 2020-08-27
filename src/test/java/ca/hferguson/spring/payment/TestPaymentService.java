package ca.hferguson.spring.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.paypal.core.PayPalEnvironment;

import ca.hferguson.spring.bean.IBasket;
import ca.hferguson.spring.filter.UserBasketFilter;


@SpringBootTest(classes={ca.hferguson.spring.HfCakeFactoryApplication.class})

public class TestPaymentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserBasketFilter.class);
	
	@Autowired
	IBasket basket;
	@Autowired 
	PayPalEnvironment payPalEnv;
	PaymentService service;
	String mockBaseURL = "http://localhost:8080";
	
	@Value("${spring.profiles.active}")
	String activeProfile;
	
	@BeforeEach
	void setup() {
		LOGGER.info("Using profile {}", activeProfile);
	}
	public PaymentService getService() {
		if (this.service == null)
			this.service = new PaymentService(payPalEnv);
		return this.service;
	}
	
	@Test
	void testCreateOrder() throws Exception {
		fillBasket();
		OrderDetails od = new OrderDetails();
		od.setItems(basket.cartInventory());
		od.setFirstName("Hugh");
		od.setLastName("Ferguson");
		od.setAddrLine1("117 Goulburn Ave");
		od.setAddrLine2("Unit #1");
		od.setAdminArea1("Ottawa");
		od.setAdminArea2("ON");
		od.setPostalCode("K1N 8C9");
		
		OrderDetails order = getService().createOrder(od, mockBaseURL);
		assertThat(order != null);
		assertThat(order.getPaypalStatus().equals("CREATED"));
		LOGGER.info("Returned with order {} ", order.getPaypalOrderID());
		assertThat(order.getPaypalOrderID().length() > 0);
		String userLink = order.getPaypalNextURL();
		LOGGER.info("Approver link {}", userLink);
		assertThat(userLink.startsWith("https://www.sandbox.paypal.com/checkout"));

	}
	
	private void fillBasket() {
		//basket = new Basket();
		basket.addItem("ccr");
		basket.addItem("ccr");
		basket.addItem("abcr");
		basket.addItem("b");
	}
}
