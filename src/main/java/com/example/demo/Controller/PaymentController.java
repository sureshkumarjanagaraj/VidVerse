package com.example.demo.Controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Entity.User;
import com.example.demo.Entity.Video;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.VideoService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoService videoService;


    // =========================
    // PAYMENT PAGE
    // =========================

    @GetMapping("/payment")
    public String paymentPage(
            Model model,
            HttpSession session) {

        String email =
                (String) session.getAttribute("email");

        if (email == null) {
            return "redirect:/login";
        }

        User user =
                userRepository.findByEmail(email);

        if (user == null) {
            return "redirect:/login";
        }

        // Already premium
        if (user.isPremium()) {
            return "redirect:/watch-videos";
        }

        model.addAttribute("keyId", keyId);

        return "payment";
    }


    // =========================
    // CREATE RAZORPAY ORDER
    // =========================

    @GetMapping("/create-order")
    @ResponseBody
    public String createOrder() {

        try {

            RazorpayClient razorpay =
                    new RazorpayClient(
                            keyId,
                            keySecret
                    );

            JSONObject orderRequest =
                    new JSONObject();

            // ₹49 = 4900 paise
            orderRequest.put(
                    "amount",
                    4900
            );

            orderRequest.put(
                    "currency",
                    "INR"
            );

            orderRequest.put(
                    "receipt",
                    "vidverse_demo_001"
            );

            Order order =
                    razorpay.orders.create(
                            orderRequest
                    );

            return order.toString();

        } catch (Exception e) {

            e.printStackTrace();

            return "Error: " + e.getMessage();
        }
    }


    // =========================
    // PAYMENT SUCCESS
    // =========================

    @GetMapping("/premium-success")
    public String premiumSuccess(
            HttpSession session) {

        String email =
                (String) session.getAttribute("email");

        if (email == null) {
            return "redirect:/login";
        }

        User user =
                userRepository.findByEmail(email);

        if (user == null) {
            return "redirect:/login";
        }

        // Make user premium
        user.setPremium(true);

        // Save in database
        userRepository.save(user);

        // IMPORTANT:
        // Show premium-success.html
        return "premium-success";
    }


    // =========================
    // WATCH VIDEOS
    // =========================

    @GetMapping("/watch-videos")
    public String watchVideos(
            HttpSession session,
            Model model) {

        String email =
                (String) session.getAttribute("email");

        if (email == null) {
            return "redirect:/login";
        }

        User user =
                userRepository.findByEmail(email);

        if (user == null) {
            return "redirect:/login";
        }

        // Only premium users can watch
        if (!user.isPremium()) {
            return "redirect:/payment";
        }

        List<Video> videos =
                videoService.getAllVideos();

        model.addAttribute(
                "videos",
                videos
        );

        return "watch-videos";
    }
}