package com.luv2code.springdemo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	@Transactional
	public List<Customer> getCustomers() {
		Iterable<Customer> customerIt = customerRepository.findAll();
		List<Customer> allCustomers = new ArrayList<>();
		customerIt.forEach(allCustomers::add);
		return allCustomers;
	}

	@Override
	@Transactional
	public void saveCustomer(Customer theCustomer) {
		customerRepository.save(theCustomer);
	}

	@Override
	@Transactional
	public Customer getCustomer(String theId) {
		return customerRepository.findById(theId).orElse(null);
	}

	@Override
	@Transactional
	public void deleteCustomer(String theId) {
		customerRepository.deleteById(theId);
	}
}
