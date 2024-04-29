package com.mymart.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mymart.model.Address;
import com.mymart.model.CartItem;
import com.mymart.model.OrderItem;
import com.mymart.model.OrderStatus;
import com.mymart.model.Orders;
import com.mymart.model.Product;
import com.mymart.model.ShippingMethod;
import com.mymart.model.User;
import com.mymart.repository.AddressRepository;
import com.mymart.service.AddressService;
import com.mymart.service.CartItemService;
import com.mymart.service.OrderItemService;
import com.mymart.service.OrderService;
import com.mymart.service.PaymentService;
import com.mymart.service.ProductService;
import com.mymart.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
//@RequestMapping("/checkout")
@Controller
 public class OrderController {
	@Autowired
    private UserService userService;

	@Autowired
    private CartItemService cartService;
	@Autowired
	OrderItemService orderItemService;
    @Autowired
    private AddressService addressService;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ProductService productService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, Principal principal) {
        User user = userService.getCurrentUser();
        List<Address> addresses = user.getAddresses(); // Fetch user's addresses
        List<Product> products = productService.getAllProducts(); // Fetch available products
        List<CartItem> cartItems = cartService.getCartItems(user); // Fetch cart items for the user

        if (addresses.isEmpty()) {
            return "redirect:/addAddress"; // Redirect to add address page if no addresses are available
        }

        Address defaultAddress = addresses.get(0);
        


        double subtotal = cartService.calculateSubtotal(cartItems);
        double shipping = cartService.calculateShipping(subtotal);
        double total = subtotal + shipping;


        model.addAttribute("user", user);
        model.addAttribute("addresses", addresses);
        model.addAttribute("products", products);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("shipping", shipping);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", total);
        model.addAttribute("defaultAddress", defaultAddress);

        return "checkout";
    }


    @GetMapping("/addAddress")
    public String showAddAddressPage(Model model) {
        model.addAttribute("address", new Address());
        return "addAddress";
    }

    @PostMapping("/checkout/addAddress")
    public String addAddress(@ModelAttribute("address") Address address, Principal principal) {
        User user = userService.getCurrentUser();
        address.setUser(user);
        addressService.saveAddress(address);
        return "redirect:/checkout"; // Redirect back to checkout page
    }
  
    
    @GetMapping("/orderConfirmation")
    public String asdd() {
 	   return "orderConfirm";
    }
    
   
    
    
//    @PostMapping("/placeOrder")
//    public String placeOrder(@RequestParam("addressId") int addressId, Orders orders, Principal principal,Model model) {
//        try {
//            if (principal == null) {
//                return "redirect:/login"; // Redirect to login if user is not authenticated
//            }
//
//            String username = principal.getName(); // Get the username from Principal
//            User user = userService.findByEmail(username);
//            if (user == null) {
//                return "redirect:/login"; // Redirect to login if user not found
//            }
//
//            orders.setUser(user); // Set the user in the order object
//
//	        
//	   
//            Address shippingAddress = addressService.findById(addressId); // Fetch the Address object by ID
//            if (shippingAddress == null) {
//                return "Invalid address ID";
//            }
//
//            orders.setShippingAddresses(shippingAddress); // Set the shipping address in the Orders object
//
//            
//            
//            
//	        List<CartItem> cartItems = cartService.getAllCartItemsByUser(user);
//
//            double subtotal = cartService.calculateSubtotal(cartItems);
//	        double shipping = cartService.calculateShipping(subtotal);
//	        double total = subtotal + shipping;
//
//	        
//	        model.addAttribute("subtotal", subtotal);
//	        model.addAttribute("shipping", shipping);
//	        model.addAttribute("total", total);
//            
//            
//            
//            
//            orders.setSubtotal(subtotal);
//
//          orders.setShippingCharges(shipping); // Set the shipping charges in the Orders object
//
//         
//          orders.setTotalAmount(total);
//         orders.setAmount(String.valueOf(total));
//          
//          String paymentTransactionId = processPayment(orders);
//          String orderNumber = generateOrderNumber();
//
//          if (orders.getShippingMethod() == null) {
//              orders.setShippingMethod(ShippingMethod.STANDARD);
//          }
//          orders.setStatus(OrderStatus.PLACED);
//          orders.setPaymentTransactionId(paymentTransactionId);
//          orders.setOrderNumber(orderNumber);
//          
//          
//          Optional<Address> optionalAddress = addressRepository.findById(addressId);
//	       
//	            model.addAttribute("selectedAddress", optionalAddress.get());
//	            
//          
//            orderService.saveOrder(orders); // Save the order
//
//            return "redirect:" + request.getContextPath() + "/orderConfirmation";
//        } catch (Exception e) {
//            e.printStackTrace(); // Log the exception for debugging
//            return "Error storing form data";
//        }
//    }
//
//    
//    
   

    
    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam("addressId") int addressId, Orders orders, Principal principal,Model model) {
        try {
            if (principal == null) {
                return "redirect:/login"; // Redirect to login if user is not authenticated
            }

            String username = principal.getName(); // Get the username from Principal
            User user = userService.findByEmail(username);
            if (user == null) {
                return "redirect:/login"; // Redirect to login if user not found
            }

            orders.setUser(user); // Set the user in the order object

	        
	   
            Address shippingAddress = addressService.findById(addressId); // Fetch the Address object by ID
            if (shippingAddress == null) {
                return "Invalid address ID";
            }

            orders.setShippingAddresses(shippingAddress); // Set the shipping address in the Orders object

            
            
            
	        List<CartItem> cartItems = cartService.getAllCartItemsByUser(user);

            double subtotal = cartService.calculateSubtotal(cartItems);
	        double shipping = cartService.calculateShipping(subtotal);
	        double total = subtotal + shipping;

	        
	        model.addAttribute("subtotal", subtotal);
	        model.addAttribute("shipping", shipping);
	        model.addAttribute("total", total);
            
            
            
            
            orders.setSubtotal(subtotal);

          orders.setShippingCharges(shipping); // Set the shipping charges in the Orders object

         
          orders.setTotalAmount(total);
         orders.setAmount(String.valueOf(total));
          
          String paymentTransactionId = processPayment(orders);
          String orderNumber = generateOrderNumber();

          if (orders.getShippingMethod() == null) {
              orders.setShippingMethod(ShippingMethod.STANDARD);
          }
          orders.setStatus(OrderStatus.PLACED);
          orders.setPaymentTransactionId(paymentTransactionId);
          orders.setOrderNumber(orderNumber);
          
          
          Optional<Address> optionalAddress = addressRepository.findById(addressId);
	       
	            model.addAttribute("selectedAddress", optionalAddress.get());
	            
          
            orderService.saveOrder(orders); // Save the order

            
     //       List<CartItem> cartItems = cartService.getAllCartItemsByUser(user);
            for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(orders); // Set the order for the order item
            orderItem.setProduct(cartItem.getProduct()); // Set the product for the order item
            orderItem.setQuantity(cartItem.getQuantity()); // Set the quantity for the order item
            // Save the order item
            orderItemService.saveOrderItem(orderItem);
            }
            return "redirect:" + request.getContextPath() + "/orderConfirmation";
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return "Error storing form data";
        }
    }

    
    

    private String processPayment(Orders order) {
        return "TXN123456789";
    }

    private String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis();
    }
	
}