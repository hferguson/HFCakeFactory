package ca.hferguson.spring.bean;

import java.math.BigDecimal;

import lombok.*;

@Data
public class Item {

	@Setter(AccessLevel.NONE)
	private final String sku;
	private final String title;
	private final BigDecimal price;
	// These are the optional ones that are not part of the solution
	private final String description;
	private final String image;
	
}
