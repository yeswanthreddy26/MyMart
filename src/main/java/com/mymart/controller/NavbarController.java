package com.mymart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mymart.model.ContactUs;
import com.mymart.repository.ContactUsRepository;
import com.mymart.repository.ProductsRepository;
import com.mymart.service.DealService;

@Controller
public class NavbarController {
	
	 @Autowired
	 DealService dealService;
	 
	 @Autowired
	 ProductsRepository repo;
	 
	 @Autowired
	 ContactUsRepository contactRepo;



	 @GetMapping("/Admin/Deals")
	    public String adminPage(Model model) {
		    model.addAttribute("products", dealService.getProductsWithDiscountDeals());

	        return "products/AdminDeals";
	    }
	 @GetMapping("/Deals")
	    public String adminPagea(Model model) {
		    model.addAttribute("products", dealService.getProductsWithDiscountDeals());

	        return "products/UserProduct";
	    }
	 @GetMapping("/Contact")
	 public String userContact(Model model) {
	        ContactUs latestContact = contactRepo.findFirstByOrderByIdDesc();
	        model.addAttribute("contactUs", latestContact);
	        return "Contact";
	    }
	 @GetMapping("/Admin/Contact")
	    public String adminContact(Model model) {
	        model.addAttribute("contactUs", new ContactUs());
	        return "admincontact";
	    }
	 @PostMapping("/admincontact") // Add this mapping for handling POST requests
	    public String adminContactSubmit(@ModelAttribute ContactUs contactUs) {
	        contactRepo.save(contactUs);
	        return "redirect:/Admin/Contact";
	    }
}
