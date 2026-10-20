package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repository.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
	AdminRepository adminRepositary;

	public List<User> getAllUsers() {
		
	    return adminRepositary.findAll();
	}
	
	
	public String makeUserAdmin(String userName) {

	    User user = adminRepositary.findByUserName(userName);

	    if (user != null) {

	        user.setRole(Role.ADMIN);

	        adminRepositary.save(user);

	        return "User " + userName + " is now an ADMIN";
	    }

	    return "User " + userName + " not found";
	}
	
	
	 public User findUserByUserName(String userName) {

	        return adminRepositary.findByUserName(userName);
	    }
	
	 
	 public String removeUser(String userName) {

		    User user = adminRepositary.findByUserName(userName);
		    
		    if (user != null) {
		        adminRepositary.delete(user);
		        return "User " + userName + " has been removed successfully";
		    }
		    return "User " + userName + " not found";
		}
}
