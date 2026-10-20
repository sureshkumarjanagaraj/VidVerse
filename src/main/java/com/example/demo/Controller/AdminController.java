package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entity.User;
import com.example.demo.Service.AdminService;

@Controller
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@GetMapping("/admin")
	public String adminPage() {
	    return "admin";
	}
	
	@GetMapping("/fetchAllUserData")
	public String getAllUsers(Model model) {
		List<User> users = adminService.getAllUsers();

        model.addAttribute("users", users);

        return "admin-users";
	}
	
	@GetMapping("/make-admin")
	public String makeAdminPage() {

	    return "make-admin";

	}
	
	@PostMapping("/admin/make-admin")
	public String makeAdmin( @RequestParam String userName,
	        Model model) {

	    String message = adminService.makeUserAdmin(userName);

	    model.addAttribute("message", message);

	    return "make-admin";
	}
	
	@GetMapping("/find-user")
	public String findUserPage() {
		return "find-user";
	}
	
	 @PostMapping("/admin/find-user")
	    public String findUser(
	            @RequestParam String userName, Model model) {

	        User user =  adminService.findUserByUserName(userName);

	        if (user != null) {

	            model.addAttribute("user", user);

	        } else {

	            model.addAttribute("message", "User " + userName + " not found");
	        }
	        
	        return "find-user";
	    }
	 
	 @GetMapping("/remove-user")
	 public String removeUserPage() {

	     return "remove-user";
	 }
	 
	 @PostMapping("/admin/remove-user")
	 public String removeUser(
	         @RequestParam String userName,
	         Model model) {

	     String message =
	             adminService.removeUser(userName);

	     model.addAttribute("message", message);

	     return "remove-user";
	 }
	 
	 
	 
}