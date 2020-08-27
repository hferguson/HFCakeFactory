package ca.hferguson.spring.payment;


import java.io.Serializable;
import java.util.*;

import ca.hferguson.spring.bean.BasketEntry;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uid;
	private String firstName;
	private String lastName;
	private String addrLine1;
	private String addrLine2;
	private String adminArea1;	// city
	private String adminArea2;	// state or province
	private String postalCode;
	private String countryCode = "CA";
	// These three filled out when we are returned by the service
	private String paypalStatus;
	private String paypalOrderID;
	private String paypalNextURL;
	
	private List<BasketEntry> items;
	
	// Adding logic is not quite in the spirit of a DTO but this
	// makes it very quick and easy to set the line items from the shopping Basket
	public void setItems(List<BasketEntry> items) {
		this.items = items;
	}
	
}
