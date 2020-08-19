package ca.hferguson.spring.bean;

import java.math.BigDecimal;
import java.util.List;

public interface IBasket {

	public void addItem(String sku);
	public void removeItem(String sku);
	public int getCountForItem(String sku);
	public int getItemsInBasket();
	public List<BasketEntry> cartInventory();
	public BasketEntry getBasketEntry(String sku);
	public void emptyCart();
	public List<String> entriesToList();
	public BigDecimal getCartTotal();
}
