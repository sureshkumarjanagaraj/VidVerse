package com.example.demo.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.User;
import java.util.List;


public interface AdminRepository extends JpaRepository<User, Integer>{

	User findByUserName(String userName);
}
