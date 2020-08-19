package ca.hferguson.spring.order;



import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;
import java.util.*;

@Component
@SessionScope
@Data
public class OrderReceipt {
	public final static int ORDER_NEW = 0;
	public final static int ORDER_ERR = -1;
	public final static int ORDER_RECEIVED = 1;
	
	private String orderUID = "unknown";
	private final Date orderDate;
	private int orderStatus = ORDER_NEW;
}
