package com.luv2code.springdemo.repository;

import org.springframework.data.repository.CrudRepository;

import com.luv2code.springdemo.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, String> {

}
