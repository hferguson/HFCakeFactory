package ca.hferguson.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import ca.hferguson.spring.bean.IBasket;
import ca.hferguson.spring.service.IMemberService;


@Controller
public class BasketController extends BaseController{

	public BasketController(IBasket basket, IMemberService service) {
		super(basket, service);
	}
	
	@PostMapping("/basket")
	public String updateCart(String sku) {
		//System.out.println("SKU is " + sku);
		shoppingCart.addItem(sku);
		return "redirect:/";
	}
	
	@GetMapping("/basket")
	public String displayCart(final Model model) {
		setupModel(model, "basket");
		
		model.addAttribute("manifest", shoppingCart.cartInventory());
		return "viewcart";
	}
	
	@PostMapping("/basket/delete")
	public String updateCartRemoval(String sku) {
		shoppingCart.removeItem(sku);
		return "redirect:/basket";
	}
}
