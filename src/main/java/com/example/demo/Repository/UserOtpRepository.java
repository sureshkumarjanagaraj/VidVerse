package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.UserOtp;

public interface UserOtpRepository extends JpaRepository<UserOtp, Integer> {

    UserOtp findByOtp(String otp);

}