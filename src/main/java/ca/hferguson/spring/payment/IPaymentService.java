package ca.hferguson.spring.payment;

import java.io.IOException;

import com.paypal.orders.Order;

public interface IPaymentService {
	public PaymentConfirmation captureOrder(String orderID) throws IOException;
	public OrderDetails createOrder(OrderDetails order, String baseURL) throws IOException;
	public String getApproveLink(Order order);
}
