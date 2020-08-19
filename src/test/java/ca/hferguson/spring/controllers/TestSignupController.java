package ca.hferguson.spring.controllers;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.ui.Model;

import ca.hferguson.spring.bean.IBasket;
import ca.hferguson.spring.misc.RegistrationForm;
import ca.hferguson.spring.service.IMemberService;



public class TestSignupController {

	private SignupController controller;
	private IBasket basket;
	private IMemberService mService;
	private BCryptPasswordEncoder encoder;
	private Model model;
	
	@BeforeEach
	void setup() {
		 this.basket = mock(IBasket.class);
		 this.mService = mock(IMemberService.class);
	     this.model = mock(Model.class);
	     this.encoder = mock(BCryptPasswordEncoder.class);
	     this.controller = new SignupController(basket, mService, encoder);
	}
	@Test
	public void testSignup() {
		String userBefore = controller.getAuthenticated();
		controller.signUp(model, createNewRegistration() );
		String userAfter = controller.getAuthenticated();
		log("User before: " + userBefore);
		log("User after: " + userAfter);
	}
	
	private RegistrationForm createNewRegistration() {
		String pwdUnenc = "testing123";
		RegistrationForm form = new RegistrationForm();
		form.setUid("luser@hferguson.ca");
		form.setFirstName("Larry");
		form.setLastName("User");
		form.setPasswd(pwdUnenc);
		form.setStreet("123 Fake St.");
		form.setCity("Springfield");
		form.setStateOrProvince("OH");
		form.setPostalCode("123456");
		log("Org password: " + pwdUnenc);
		
		return form;
	}
	
	private void log(String msg) {
		System.out.println(String.format("DEBUG: %s", msg));
	}
}
