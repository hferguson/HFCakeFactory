package ca.hferguson.spring.controllers;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import ca.hferguson.spring.bean.IBasket;
import ca.hferguson.spring.service.IMemberService;

public class TestOrderController {
	private OrderController controller;
    private IBasket basket;
    private IMemberService mService;
    private ApplicationEventPublisher eventPublisher;
    private Model model;
    private String skuCC = "ccr";
    private String skuBC = "abcr";
    
    @BeforeEach
    void setUp() {
        this.basket = mock(IBasket.class);
        this.mService = mock(IMemberService.class);
        this.model = mock(Model.class);
        basket.addItem(skuCC);
        basket.addItem(skuBC);
        basket.addItem(skuBC);
        this.eventPublisher = mock(ApplicationEventPublisher.class);
        this.controller = new OrderController(this.basket, this.mService, this.eventPublisher);
        
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
    	MultiValueMap<String,String> formData = new LinkedMultiValueMap<String,String>();
    	formData.add("addr1", "123 Fake St.");
    	formData.add("addr2", "Springfield, IL");
    	formData.add("postCode", "12345");
    	this.controller.completeOrder(model, formData);
    }
}
