package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.User;


public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);
	
	boolean existsByUserName(String userName);
	
	User findByUserName(String userName);

}
