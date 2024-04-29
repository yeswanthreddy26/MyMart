package com.mymart.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mymart.model.DropdownItem;
import com.mymart.model.NavLink;
import com.mymart.model.User;
import com.mymart.model.UserDto;
import com.mymart.service.NavBarService;
import com.mymart.service.UserService;
import com.mymart.service.UserServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class loginController {
	
	
	private final NavBarService service; 
	
    public loginController(NavBarService service) {
        this.service = service;
    }

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserServiceImpl userServiceimpl;
	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
		return "login/register";
	}
	

	@PostMapping("/registration")
	public String saveUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult, Model model) {
	    if (userServiceimpl.existsByEmail(userDto.getEmail())) {
	        model.addAttribute("message", "There is already an account registered with this email.");
	    } else if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
	        model.addAttribute("message", "Password and confirm password should be same");
	    } else {
	        userService.save(userDto);
	        model.addAttribute("successMessage", "You have Registered Successfully!");
	    }

	    return "login/register";
	}

	@GetMapping("/login")
	public String login() {
		return "login/login";
	}
	
	
	@GetMapping("user-page")
	public String userPage (Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		//return "login/user";
		return "redirect:/";
	}
	
	

	@GetMapping("admin-page")
	public String adminPage (Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		
		 List<NavLink> allNavLinks = service.getAllNavLinks();
	        Map<NavLink, List<DropdownItem>> navbarWithDropdownData = service.getNavbarWithDropdownData();

	       

	        model.addAttribute("allNavLinks", allNavLinks);
	        model.addAttribute("navbarWithDropdownData", navbarWithDropdownData);
		
		
		model.addAttribute("user", userDetails);
		return "login/admin";
	}
	
	
	

}

