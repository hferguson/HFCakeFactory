package ca.hferguson.spring.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;



import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import ca.hferguson.spring.bean.Item;
import ca.hferguson.spring.service.PastryService;



@SpringBootTest(classes={ca.hferguson.spring.HfCakeFactoryApplication.class})

class TestHomeController {
	private final int numProducts = 6;
	
	@Value("${application.title}")
	String appTitle = "";
	
	@Autowired
	PastryService pService;
	
	@Test
	void contextLoads() {
		System.out.println("Context loaded");
	}

	@Test
	@DisplayName("index page returns the landing page")
	void returnsLandingPage() throws Exception {
		//System.out.println("Got here");
		try (final WebClient webClient = new WebClient()) {
	        final HtmlPage page = webClient.getPage("http://localhost:8080/");
	        assertEquals("Cake Factory", page.getTitleText());

	        final String pageAsXml = page.asXml();
	        assertTrue(pageAsXml.contains("<body>"));

	        final String pageAsText = page.asText();

	        assertTrue(pageAsText.contains("Hugh's House of Pancakes"));
	    } catch (Exception e) {
	    	throw e;
	    }
		
	}
	@Test
	@DisplayName("products page returns 6 pastries")
	void returnsProductPage() throws Exception {
		try (final WebClient webClient = new WebClient()) {
			final HtmlPage page = webClient.getPage("http://localhost:8080/");
			final HtmlElement cakeRow = page.getHtmlElementById("spring-cakewalk");
			int cakesOnPage = cakeRow.getChildElementCount();
			assertTrue(cakesOnPage == this.numProducts);
		}
	}
	@Test
	void getAllPastries() throws Exception {
		Iterable<Item> products = pService.getProducts();
		List<Item> items = new ArrayList<Item>();
		products.forEach(items::add);
		assertFalse(items.isEmpty());
		assertTrue((items.size() == numProducts));
	}

}