package ca.hferguson.spring.controllers;


import ca.hferguson.spring.bean.*;
import ca.hferguson.spring.service.IMemberService;
import ca.hferguson.spring.service.IPastryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class TemplateController extends BaseController {
	
	
	@Autowired
	private IPastryService pService;
	
	public TemplateController(IBasket basket, IMemberService service) {
		super(basket, service);
	}
	// I have a feeling that there is a better way to implement this other than
	// simply
	@GetMapping("/")
	public String getBaseTemplate(final Model model) {
		log("Default page called");
		setupModel(model, "");
		Iterable<Item> products = pService.getProducts();
		model.addAttribute("products", products);
		
		return "index";
	}
	
	
	@GetMapping("/about")
	public String getAboutTemplate(final Model model) {
		setupModel(model, "about");
		return "about";
	}
	
	@GetMapping("/contact")
	public String getContactTemplate(final Model model) {
		setupModel(model, "contact");
		return "contact";
	}
	
}
