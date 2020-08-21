package ca.hferguson.spring.controllers;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;

import ca.hferguson.spring.OAuth2Utils;
import ca.hferguson.spring.bean.*;
import ca.hferguson.spring.misc.Account;
import ca.hferguson.spring.misc.Address;
import ca.hferguson.spring.misc.NavLink;
import ca.hferguson.spring.service.*;

@Controller
public class BaseController {
	
	IBasket shoppingCart;
	IMemberService memberService;
	
	@Value("${application.title}")
	protected String APP_TITLE = "";
	protected String[] links = {"", "about",  "contact"};
	protected String[] linkDisp = {"Home", "About Us", "Contact Us"};
	
	public BaseController(IBasket basket, IMemberService service) {
		this.shoppingCart = basket;
		this.memberService = service;
	}
	protected Model setupModel(Model model, String currLink) {
		model.addAttribute("StoreName", "Hugh's House of Pancakes");
		model.addAttribute("TemplateTitle", APP_TITLE);
		model.addAttribute("cartItems", shoppingCart.getItemsInBasket());
		// Add link info
		List<NavLink> linkObj = new ArrayList<NavLink>();
		for (int i=0;i< Math.min(links.length, linkDisp.length); i++) {
			NavLink link = new NavLink(links[i], linkDisp[i]);
			if (currLink.equals(links[i]))
				link.current = true;
			linkObj.add(link);
		}
		model.addAttribute("links", linkObj);
		model = setUpUserOrLogin(model);
		return model;
	}
	
	protected Model setUpUserOrLogin(Model model) {
		Account user = this.getUserFromToken();
	
		if (user != null) {
			log(String.format("Found principal %s", user.getUid()) );
			String userMsg = String.format("Welcome, %s (%s)", user.getFirstName(), user.getUid());
			model.addAttribute("loginOrUser", "/account");
			model.addAttribute("loginOrUserDisp", userMsg);
			if (user != null) {
				model.addAttribute("address", getUserAddress(user));
				model.addAttribute("user", user);
			}
		} else {
			model.addAttribute("loginOrUser", "/login");
			model.addAttribute("loginOrUserDisp", "Login or Sign up");
		}
		if (!model.containsAttribute("address")) {
			// For Mustache, give a fake address consisting of blanks if not
			// one set in model.
			Address addy = new Address("", "", "", "", "");
			model.addAttribute("address", addy);
		}
		if (!model.containsAttribute("user")) {
			Account acct = new Account("", "", "", "");
			model.addAttribute("user", acct);
		}
		return model;
	}
	
	protected Address getUserAddress(Account user) {
		Address addy = user.getAddress();
		if (addy == null)
			addy = new Address(user.getUid(), "", "", "", "");
		return addy;
	}
	protected Account getUserFromToken() {
		Account user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null) {
			Authentication token = context.getAuthentication();
			log("authenticated " + String.valueOf(token.isAuthenticated()));
			if (OAuth2Utils.isUserToken(token)) {
				user = memberService.findAccount(token.getName());
			} else if (OAuth2Utils.isOAuth2Token(token)) {
				user = OAuth2Utils.getAccount((OAuth2AuthenticationToken)token);
				Address addy = memberService.findAddress(user.getUid());
				if (addy != null)
					user.setAddress(addy);
			}
		}
		
		return user;
	}
	
	
	protected boolean isNullOrEmpty(String val) {
		boolean retVal = true;
		if (val != null)
			retVal = val.isBlank();
		return retVal;
	}
	
	protected boolean isOAuth2Token(Authentication token) {
		return (token instanceof OAuth2AuthenticationToken);
	}
	
	protected void setAsAuthenticated(String username, String passwd) {
		try {
			SecurityContext context = SecurityContextHolder.createEmptyContext(); 
			Authentication authentication =
			    new UsernamePasswordAuthenticationToken(username, passwd, List.of(new SimpleGrantedAuthority("ROLE_USER"))); 
			context.setAuthentication(authentication);
			log(String.format("Setting user as authenticated. User %s, passwd %s", username, passwd));
			SecurityContextHolder.setContext(context);
		} catch (Exception e) {
			error("Exception caught while setting user auth context");
			e.printStackTrace();
		}
	}
	protected void log(String msg) {
		System.out.println(msg);
	}
	
	protected void error(String msg) {
		System.err.println(msg);
	}
	
	protected void debugFormData(MultiValueMap<String,String> map) {
		Iterator<String> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			log(String.format("%s : %s", key, map.getFirst(key) ));
		}
	}
}
