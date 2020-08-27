package ca.hferguson.spring.bean;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import ca.hferguson.spring.service.IPastryService;
import lombok.*;

@Component
@SessionScope
@Data
public class Basket implements IBasket {

	@Setter(AccessLevel.NONE)
	private final String sessionId;		// Associate this with a particular user session
	private final Map<String,Integer> contents = new HashMap<String,Integer>();	
	
	@Autowired
	private IPastryService productService;
	
	public Basket(String sessionId) {
		this.sessionId = sessionId;
	}
	public Basket() {
		this.sessionId = "012345";	// Fix this later
	}
	
	@Override
	public void addItem(String sku) {
		if (contents.containsKey(sku)) {
			int count = contents.get(sku).intValue();
			contents.replace(sku, Integer.valueOf(count+1));
		} else {
			contents.put(sku, Integer.valueOf(1));
		}
	}
	
	@Override
	public void removeItem(String sku) {
		if (contents.containsKey(sku)) {
			int count = contents.get(sku).intValue();
			// Don't decrement if we have zero items - this shouldn't happen
			if (count > 0)
				count--;
			// If count > 0 still, decrement count in the map, otherwise
			// remove this ski from the list
			if (count > 0) {
				//System.out.println("DEBUG decrementing SKU " + sku);
				contents.replace(sku, Integer.valueOf(count));
			} else {
				//System.out.println("DEBUG Removing SKU " + sku);
				contents.remove(sku);
			}
		}
	}
	
	@Override
	public int getCountForItem(String sku) {
		int retVal = 0;
		if (contents.containsKey(sku)) 
			retVal = contents.get(sku).intValue();
		return retVal;
	}
	
	@Override
	public int getItemsInBasket() {
		int retVal = 0;
		if (contents.size() > 0) {
			Iterator<String> iter = contents.keySet().iterator();
			while (iter.hasNext()) {
				String sku = iter.next();
				retVal += contents.get(sku).intValue();
			}
		}
		return retVal;
	}
	
	@Override
	public List<BasketEntry> cartInventory() {
		List<BasketEntry> cartManifest = new ArrayList<BasketEntry>();
		Iterator<String> iter = contents.keySet().iterator();
		while (iter.hasNext()) {
			String sku = iter.next();
			
			
			BasketEntry entry = getBasketEntry(sku);
			if (entry != null)
				cartManifest.add(entry);
		}
		return cartManifest;
	}
	
	@Override
	public BasketEntry getBasketEntry(String sku) {
		BasketEntry entry = null;
		
		Item item = productService.findOne(sku);
		if (item != null) {
			int quantity = getCountForItem(sku);
			if (quantity > 0) {
				BigDecimal price = item.getPrice().multiply(new BigDecimal(quantity));
				entry = new BasketEntry(sku, quantity, item, price);
			}
		}
		
		return entry;
	}
	@Override
	public void emptyCart() {
		contents.clear();
	}
	
	@Override
	public List<String> entriesToList() {
		List<String> items = new ArrayList<String>();
		List<BasketEntry> entries = cartInventory();
		for (int i=0;i<entries.size();i++) {
			BasketEntry entry = entries.get(i);
			for (int j=0;j<entry.getQuantity();j++) {
				items.add(new String(entry.getSku()));
			}
		}
		
		return items;
	}
	@Override
	public BigDecimal getCartTotal() {
		return getSumOfEntries(this.cartInventory());
	}
	
	public static BigDecimal getSumOfEntries(List<BasketEntry> entries) {
		BigDecimal total = new BigDecimal(0);
		for (int i=0;i<entries.size();i++) {
			total = total.add(entries.get(i).getPrice());
		}
		return total;
	}
	
}
