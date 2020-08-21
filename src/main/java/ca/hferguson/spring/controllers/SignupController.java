package ca.hferguson.spring.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ca.hferguson.spring.bean.IBasket;
import ca.hferguson.spring.misc.*;
import ca.hferguson.spring.service.IMemberService;

@Controller
public class SignupController extends BaseController {

	
	
	private PasswordEncoder encoder;
	
	public SignupController(IBasket basket, IMemberService service, PasswordEncoder encoder) {
		super(basket, service);
		
		this.encoder = encoder;
	}
	
	
	@GetMapping("/login")
	public String loginPage(final Model model, HttpServletRequest request) {
		setupModel(model, "login-signup");
		String errMsg = "";
		Exception lastException = (Exception) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if (lastException != null && lastException.getMessage() != null) {
        	errMsg = lastException.getMessage();
        }
        model.addAttribute("error", errMsg);
		return "login";
	}
	
	@GetMapping("/loginsignup")
	public String loginOrSignup(final Model model) {
		setupModel(model, "login-signup");
		return "signup";
	}
	
	@PostMapping("/signup")
	public String signUp(final Model model, RegistrationForm form ) {
		Account account = form.toUser(encoder);
		log("Encrypted pwd: " + account.getPasswd());
		if (account != null) {
			memberService.addAccount(account);
			setAsAuthenticated(account.getUid(), account.getPasswd());
			log("Account added, authenticating automagically");
		} 
		return "redirect:/";
	}
	
}
