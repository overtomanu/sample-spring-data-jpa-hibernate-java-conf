package com.luv2code.springdemo.test;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
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
				query, 1,
				Map.of("first_name", theCustomer.getFirstName(), "email",
						theCustomer.getEmail()));
	}
	
	@Test
	public void getAndUpdateCustomerTest() throws SQLException {

		String customerId = "id753";
		String customerFirstName = "test2FirstName";
		String insertSQL = "INSERT INTO customer "
				+ "(id,first_name,last_name,email,version,created_by,creation_date,last_updated_by,last_update_date) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection connection = testDataSource.getConnection();

		TestUtils.insertSingleRow(connection, insertSQL,
				customerId, customerFirstName, "test2LastName",
				"test2@test.com", 2, "anonymous", LocalDateTime.now(),
				"anonymous", LocalDateTime.now());
		Customer customer = customerService.getCustomer(customerId);
		assertEquals(customerFirstName, customer.getFirstName());
		customerFirstName = "test2NameUpdated";
		customer.setFirstName(customerFirstName);
		customerService.saveCustomer(customer);
		TestUtils.executeQueryAndVerifySingleRow(connection,
				"select * from customer where id='" + customerId + "'",
				1, Map.of("first_name", customerFirstName));
		connection.close();
	}

	@Test
	public void deleteCustomerTest() throws SQLException {
		String customerId = "idDelete";
		String customerFirstName = "test3FirstName";
		String insertSQL = "INSERT INTO customer "
				+ "(id,first_name,last_name,email,version,created_by,creation_date,last_updated_by,last_update_date) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection connection = testDataSource.getConnection();

		TestUtils.insertSingleRow(connection, insertSQL, customerId,
				customerFirstName, "test3LastName", "test3@test.com", 2,
				"anonymous", LocalDateTime.now(), "anonymous",
				LocalDateTime.now());
		customerService.deleteCustomer(customerId);
		TestUtils.executeQueryAndVerifySingleRow(connection,
				"select * from customer where id='" + customerId + "'", 0,
				new HashMap<String, Object>());
		connection.close();
	}

}
