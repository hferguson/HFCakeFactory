package ca.hferguson.spring.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.paypal.core.PayPalEnvironment;

import ca.hferguson.spring.bean.*;
import ca.hferguson.spring.filter.UserBasketFilter;
import ca.hferguson.spring.payment.IPaymentService;
import ca.hferguson.spring.payment.PaymentService;
import ca.hferguson.spring.service.IMemberService;
import javax.servlet.http.*;

@SpringBootTest(classes={ca.hferguson.spring.HfCakeFactoryApplication.class})

public class TestOrderController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserBasketFilter.class);
	
	private OrderController controller;
	@Autowired
    private Basket basket;
	@Autowired
	private PayPalEnvironment payPalEnv;
    private IMemberService mService;
    private IPaymentService pService;
    private ApplicationEventPublisher eventPublisher;
    private Model model;
    private String skuCC = "ccr";
    private String skuBC = "abcr";
    
    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    @BeforeEach
    void setUp() {
        //this.basket = new Basket();
        this.mService = mock(IMemberService.class);
        this.pService = new PaymentService(payPalEnv);	// Mocking not doing so well for this one
        this.model = mock(Model.class);
        this.session = mock(HttpSession.class);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        setUpServletObjects();
        
        
        this.eventPublisher = mock(ApplicationEventPublisher.class);
        this.controller = new OrderController(this.basket, this.mService, this.pService, this.eventPublisher);
        basket.addItem(skuCC);
        basket.addItem(skuBC);
        basket.addItem(skuBC);
        log("items added to basket");
    }
    void setUpServletObjects() {
    	when(this.request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/dummy"));
    	when(this.request.getRequestURI()).thenReturn("/dummy");
    }
    @Test
    void testPublisher() {
    	if (this.model == null)
    		System.err.println("Failed to mock model");
    	else
    		System.out.println("Model is mocked");
    	if (this.basket == null)
    		System.err.println("failed to mock basket");
    	else
    		System.out.println("basket is mocked");
    	assertThat(this.controller.shoppingCart.cartInventory().size() > 0);
    	List<BasketEntry> items = this.controller.shoppingCart.cartInventory();
    	log(String.format("Counting basket items. %d items", items.size()));
    	for (BasketEntry item : items) {
    		assertThat(item.getPrice().floatValue() > 0);
    		log(String.format("Basket item %s x %d, %s", item.getItem().getTitle(), item.getQuantity(), item.getPrice().toPlainString()));
    	}
    	MultiValueMap<String,String> formData = new LinkedMultiValueMap<String,String>();
    	formData.add("fName", "Marge");
    	formData.add("lName", "Simpson");
    	formData.add("email", "marge@springfield.edu");
    	formData.add("addr1", "123 Fake St.");
    	formData.add("city", "Springfield");
    	formData.add("stateProv", "IL");
    	formData.add("postcode", "12345");

    	if (request.getRequestURL() == null) {
    		log("WARNING request URL is null");
    		
    	}
    	String respURL = this.controller.getPayment(model, session, request, response, formData);
    	log("Response URL is " + respURL);
    	assertThat(respURL.startsWith("https://www.sandbox.paypal.com/checkout"));

    }
    
    void log(String msg) {
    	LOGGER.info(msg);
    }
}
