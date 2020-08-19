package ca.hferguson.spring.bean;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes={ca.hferguson.spring.HfCakeFactoryApplication.class})

public class TestShoppingBasket {
	
	@Autowired
	private IBasket shoppingCart;
	//private String sessionId = "shopper-1";
	private String skuBC = "abcr";
	private String skuCC = "cc";
	
	
	
	@Test
	@DisplayName("Context can load")
	void contextLoads() {
		System.out.println("Context loaded");
	}
	
	@Test
	@DisplayName("Add items to basket")
	void testUserStoryOne() throws Exception {
		// Add one item to the cart
		addToCart(skuBC);
		Assertions.assertEquals(1, getItemCount(skuBC));
		Assertions.assertEquals(1, getItemsInCart());
		addToCart(skuBC);
		Assertions.assertEquals(2, getItemCount(skuBC));
		Assertions.assertEquals(2, getItemsInCart());
		
		// Not explicitly asked for but make sure that adding something else
		// only increments its count, and total count and not the count on
		// the original item
		addToCart(skuCC);
		Assertions.assertEquals(1, getItemCount(skuCC));
		Assertions.assertEquals(2, getItemCount(skuBC));
		Assertions.assertEquals(3, getItemsInCart());
		
		
	}
	// User story 2 we will validate that we can get our manifest and it contains
	// the right items based on test 1
	@Test
	@DisplayName("Display the manifest")
	void testUserStoryTwo() {
		
		// New session, cart should be empty
		Assertions.assertEquals(0, shoppingCart.getItemsInBasket());
		addToCart(skuBC);
		List<BasketEntry> items = shoppingCart.cartInventory();
		// After adding, cart should have one item, and so should the inventory
		Assertions.assertEquals(1, items.size());
		BasketEntry be = items.get(0);
		Item item = be.getItem();
		Assertions.assertEquals("All Butter Croissant", item.getTitle());
		
	}
	@Test
	@DisplayName("Remove items from cart and display")
	void testUserStoryThree() {
		// New session, cart should be empty
		//shoppingCart.emptyCart();
		Assertions.assertEquals(0, shoppingCart.getItemsInBasket());
		addToCart(skuBC);
		addToCart(skuBC);
		addToCart(skuCC);
		// Should have one Choco croissant and 2 butter
		Assertions.assertEquals(1, getItemCount(skuCC));
		Assertions.assertEquals(2, getItemCount(skuBC));
		
		List<BasketEntry> items = shoppingCart.cartInventory();
		int qCC = 0;
		int qBC = 0;
		int qOther = 0;
		BasketEntry entry;
		for (int i=0;i<items.size();i++) {
			entry = items.get(i);
			if (entry.getSku().equals(skuBC)) {
				qBC = entry.getQuantity();
			} else if (entry.getSku().equals(skuCC)) {
				qCC = entry.getQuantity();
			} else {
				qOther += entry.getQuantity();
			}
		}
		Assertions.assertEquals(0, qOther);
		Assertions.assertEquals(1, qCC);
		Assertions.assertEquals(2, qBC);
		
		// Now remove one item of each type.
		removeFromCart(skuCC);
		removeFromCart(skuBC);
		
		// Verify that we only have one item in the queue now
		BasketEntry entryCC = shoppingCart.getBasketEntry(skuCC);
		if (entryCC != null)
			System.out.println("Entry CC has quantity " + entryCC.getQuantity());
		Assertions.assertTrue(entryCC == null);
		BasketEntry entryBC = shoppingCart.getBasketEntry(skuBC);
		Assertions.assertTrue(entryBC != null);
		Assertions.assertEquals(1, entryBC.getQuantity());
		
	}
	private void addToCart(String sku) {
		shoppingCart.addItem(sku);
	}
	
	private void removeFromCart(String sku) {
		shoppingCart.removeItem(sku);
	}
	private int getItemCount(String sku) {
		return shoppingCart.getCountForItem(sku);
	}
	
	private int getItemsInCart() {
		return shoppingCart.getItemsInBasket();
	}
	
}
