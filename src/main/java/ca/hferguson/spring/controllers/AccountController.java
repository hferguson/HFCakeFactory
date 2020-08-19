package ca.hferguson.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import ca.hferguson.spring.bean.IBasket;
import ca.hferguson.spring.misc.Address;
import ca.hferguson.spring.service.IMemberService;

@Controller
public class AccountController extends BaseController {

	public AccountController(IBasket basket, IMemberService service) {
		super(basket, service);
		
	}
	
	@GetMapping("/account")
	public String getAccountInfo(final Model model) {
		setupModel(model, "account");
		
		return "account";
	}
	
	@PostMapping("/account/addrUpdate")
	public String updateAddress(final Model model, Address addy) {
		//log("UID for this address is " + addy.getAccountID());
		//log("new city " + addy.getCity());
		this.memberService.updateAddress(addy);
		
		setupModel(model, "account");
		model.addAttribute("updateResults", "Your address has been updated");
		return "redirect:/account";
	}

}
