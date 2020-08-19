package ca.hferguson.spring.bean;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BasketEntry {
	private final String sku;
	private final int quantity;
	private final Item item;
	private final BigDecimal price;
}
