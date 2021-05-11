package com.luv2code.springdemo.test;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.luv2code.springdemo.application.PropertySourcesApplicationContextInitializer;
import com.luv2code.springdemo.config.DemoAppConfig;
import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.service.CustomerService;
import com.luv2code.springdemo.test.util.TestUtils;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
		DemoAppConfig.class }, initializers = PropertySourcesApplicationContextInitializer.class)
@ActiveProfiles(profiles = "test")
public class CustomerCRUDTest {
	@Autowired
	CustomerService customerService;

	@Autowired
	DataSource testDataSource;

	@Test
	public void createCustomerTest() throws SQLException {
		Customer theCustomer = new Customer();
		theCustomer.setFirstName("testCustomerFirstName");
		theCustomer.setLastName("test");
		theCustomer.setEmail("testCustomer@test.com");
		customerService.saveCustomer(theCustomer);
		String query = "select * from customer where id='" + theCustomer.getId() + "'";
		TestUtils.executeQueryAndVerifySingleRow(testDataSource.getConnection(),
				query,
				Map.of("first_name", "testCustomerFirstName", "email",
						"testCustomer@test.com"));
	}

}
