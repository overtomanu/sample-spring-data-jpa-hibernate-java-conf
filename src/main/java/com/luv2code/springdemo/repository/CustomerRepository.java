package com.luv2code.springdemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {

}
