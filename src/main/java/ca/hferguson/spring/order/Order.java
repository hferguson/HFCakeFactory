package ca.hferguson.spring.order;

import java.util.List;

import lombok.Data;

@Data
public class Order {

	//private final int id;
	private final List<String> items;
	// This part may get refactored in ch 4 and beyond
	// for now, we will encapsulate address information in this class
	private final String addrLine1;
	private final String addrLine2;
	private final String postalCode;
	
	
}
