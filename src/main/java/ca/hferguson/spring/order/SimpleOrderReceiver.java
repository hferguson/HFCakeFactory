package ca.hferguson.spring.order;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ca.hferguson.spring.bean.*;

@Component
public class SimpleOrderReceiver {

	@EventListener
	public void onOrderReceived(OrderReceivedEvent orEvent) {
		List<BasketEntry> items = orEvent.getItems();
		if (items != null) {
			for (int i=0;i<items.size();i++) {
				BasketEntry entry = items.get(i);
				Item item = entry.getItem();
				String sku = entry.getSku();
				String itemInfo = String.format("item %d: %s (sku %s), quantity %d - %s", i+1, item.getTitle(), sku, entry.getQuantity(), formatPrice(entry.getPrice()));
				log(itemInfo);
			}
		}
		orEvent.getReceipt().setOrderStatus(OrderReceipt.ORDER_RECEIVED);
		orEvent.getReceipt().setOrderUID("12345");
	}
	
	
	private String formatPrice(BigDecimal price) {
		return NumberFormat.getCurrencyInstance().format(price);
	}
	private void log(String msg) {
		System.out.println(msg);
	}
}
