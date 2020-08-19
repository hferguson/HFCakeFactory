package ca.hferguson.spring.order;

import ca.hferguson.spring.bean.*;
import lombok.Data;

import java.util.*;

@Data
public class OrderReceivedEvent {
	private final String address1;
	private final String address2;
	private final String postalCode;
	private final List<BasketEntry> items;
	private final OrderReceipt receipt;
}
