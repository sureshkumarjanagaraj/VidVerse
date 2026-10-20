
package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.User;
import com.example.demo.Entity.UserOtp;
import com.example.demo.Repository.UserOtpRepository;
import com.example.demo.Repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserOtpRepository OtpRepo;
	
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	BCryptPasswordEncoder passwordEncode;
	
	String emailForPasswordReset;
	
	public User isEmailExists(String email) {
		
		return userRepo.findByEmail(email);
	}
	
	public boolean isUserNameExists(String userName) {
	    return userRepo.existsByUserName(userName);
	}
	
	public void registerUser(User user) {

		user.setPassword(passwordEncode.encode(user.getPassword()));
		userRepo.save(user);
	}
	
	public User loginUser(String email, String password) {
		
		 User user = userRepo.findByEmail(email);

		 if ((user != null && passwordEncode.matches(password, user.getPassword())) && user.getEmail().equals(email)) {
	            return user;
	        }

	        return null;
	}
	
	 public boolean forgotPassword(String email) {

	        User user = userRepo.findByEmail(email);

	        if (user == null) {
	            return false;
	        } 
	        
	        else if(!user.getEmail().equals(email)) {
	        	return false;
	        }

	        String otp = generateOtp();

	        UserOtp otpEntity = new UserOtp();

	        otpEntity.setEmail(email);
	        otpEntity.setOtp(otp);

	        OtpRepo.save(otpEntity);

	        sendOtpEmail(email, otp);

	        return true;
	    }

	    public String generateOtp() {

	        int number = (int)(Math.random() * 900000) + 100000;

	        return String.valueOf(number);
	    }

	    
	    public boolean validateOtp(String otp) {

	        UserOtp otpEntity = OtpRepo.findByOtp(otp);

	        if (otpEntity != null) {

	            emailForPasswordReset = otpEntity.getEmail();

	            return true;
	        }

	        return false;
	    }
	    private void sendOtpEmail(String email, String otp) {

	        SimpleMailMessage message = new SimpleMailMessage();

	        message.setTo(email);
	        message.setSubject("VidVerse Password Reset OTP");
	        message.setText(
	            "Your VidVerse OTP is: " + otp +
	            "\n\nThis OTP is used to verify your password reset request." );

	        mailSender.send(message);
	    }
	    public boolean updatePassword(String newPassword) {

	        // Find the user using the email
	        // connected to the verified OTP

	        User user =  userRepo.findByEmail(emailForPasswordReset);


	        if (user == null) {

	            return false;
	        }

	        // ENCODE NEW PASSWORD

	        String encodedPassword =  passwordEncode.encode(newPassword);

	        // SET NEW PASSWORD

	        user.setPassword(encodedPassword);

	        // SAVE TO DATABASE

	        userRepo.save(user);

	        // RESET EMAIL VARIABLE
	        emailForPasswordReset = null;

	        return true;
	    }
	    
}
