package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userServ;

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String userName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam long phone) {

        User emailExists = userServ.isEmailExists(email);

        if (emailExists != null) {
            return "userExists";
        }

        if (userServ.isUserNameExists(userName)) {
            return "userNameExists";
        }

        User user = new User();

        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phone);
        user.setRole(Role.USER);

        // New user is NOT premium
        user.setPremium(false);

        userServ.registerUser(user);

        return "login";
    }

    // LOGIN PAGE
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        User user = userServ.loginUser(email, password);

        if (user == null) {
            return "loginFail";
        }

        System.out.println("LOGIN USER: " + user.getEmail());
        System.out.println("PREMIUM STATUS: " + user.isPremium());
        System.out.println("ROLE: " + user.getRole());

        session.setAttribute("user", user);
        session.setAttribute("email", user.getEmail());

        if (user.getRole() == Role.ADMIN) {
            return "admin";
        }

        if (user.isPremium()) {
            return "redirect:/watch-videos";
        }

        return "welcome";
    }

    // ABOUT
    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    // FORGOT PASSWORD PAGE
    @GetMapping("/forgotPassword")
    public String forgotPasswordPage() {
        return "forgotPassword";
    }

    // FORGOT PASSWORD
    @PostMapping("/forgotPassword")
    public String forgotPassword(
            @RequestParam String email) {

        boolean checkPresent =
                userServ.forgotPassword(email);

        if (checkPresent) {
            return "otp";
        }

        return "wrongEmail";
    }
    

    // VERIFY OTP
    @PostMapping("/verifyOtp")
    public String validateOtp(@RequestParam String otp) {

        boolean valid = userServ.validateOtp(otp);

        if (valid) {
            return "new-password";
        }

        return "wrongOtp";
    }
    
    @PostMapping("/newPassword")
    public String newPassword(@RequestParam String password) {

        userServ.updatePassword(password);

        return "password-updated";
    }
}