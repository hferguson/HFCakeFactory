package ca.hferguson.spring.controllers;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ca.hferguson.spring.HfCakeFactoryApplication;

import ca.hferguson.spring.bean.*;
import ca.hferguson.spring.filter.*;
import ca.hferguson.spring.misc.*;
import ca.hferguson.spring.service.*;

//import ca.hferguson.spring.service.MemberService;


@WebMvcTest(SignupController.class)
@ContextConfiguration(classes = HfCakeFactoryApplication.class)
public class TestSignupController {

	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@MockBean
	Basket basket;
	
	@MockBean
	CFUserDetailsService userInfoService;
	
	
	@Autowired
    MockMvc mockMvc;

	
    @MockBean
    MemberService memberService;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilter(new UserBasketFilter(basket), "/*")
            .addFilter(new UserSessionFilter(), "/*")
            .apply(springSecurity())
            .build();
	}
    @Test
    void onlyAllowsAccessForAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", endsWith("/login")));
    }
	
	@Test
	public void testSignup() throws Exception {
		mockMvc.perform(get("/loginsignup")).andExpect(status().isFound());
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
