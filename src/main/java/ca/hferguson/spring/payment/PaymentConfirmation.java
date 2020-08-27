package ca.hferguson.spring.payment;



import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmation {

	private String orderID;
	private String payerID;
	private String status;
	private String payerEmail;
	private String paymentDate;
	
}
