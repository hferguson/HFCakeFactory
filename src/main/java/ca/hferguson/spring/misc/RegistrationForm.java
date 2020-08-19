package ca.hferguson.spring.misc;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

/**
 * contains all the fields in the registration form
 * for both Account and Address entities
 * @author Hugh Ferguson
 *
 */
@Data
public class RegistrationForm {
	private String uid;
	private String firstName;
	private String lastName;
	private String passwd;
	private String street;
	private String city;
	private String stateOrProvince;
	private String postalCode;
	
	public Account toUser(PasswordEncoder encoder) {
		String encPwd = encoder.encode(passwd);
		Account user = new Account(uid, firstName, lastName, encPwd);
		user.setAddress(toAddress());
		System.out.println("Using encrypted password " + encPwd);
		return user;
	}
	
	public Address toAddress() {
		return new Address(uid, street, city, stateOrProvince, postalCode);
	}
}
