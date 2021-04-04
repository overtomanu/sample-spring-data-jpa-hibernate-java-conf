package com.luv2code.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
	Role findByRole(String role);

}