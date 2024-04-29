package com.mymart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mymart.model.Product;
import com.mymart.service.ProductService;

@Controller
public class SearchController {

    @Autowired
    private ProductService productService;


    
    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<Product> products = productService.searchProducts(query);

        if (products.isEmpty()) {
            model.addAttribute("errorMessage", "No products found for the query: " + query);
            return "products/error";
        } else {
            model.addAttribute("products", products);
            return "products/UserProduct"; 
        }
    }
}