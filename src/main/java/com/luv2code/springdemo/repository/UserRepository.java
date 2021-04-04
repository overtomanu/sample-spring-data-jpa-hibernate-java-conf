package com.luv2code.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	User findByUsername(String userName);
}