package ca.hferguson.spring.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes={ca.hferguson.spring.HfCakeFactoryApplication.class})
public class TestBasketController {

	@Autowired
	private BasketController controller;
	
	@Test
	public void contexLoads() throws Exception {
		assertThat(controller).isNotNull();
	}
}
