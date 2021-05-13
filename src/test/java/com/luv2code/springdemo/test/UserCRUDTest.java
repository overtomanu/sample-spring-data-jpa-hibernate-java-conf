package com.luv2code.springdemo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.luv2code.springdemo.config.DemoAppConfig;
import com.luv2code.springdemo.entity.Role;
import com.luv2code.springdemo.entity.User;
import com.luv2code.springdemo.service.UserService;
import com.luv2code.springdemo.test.util.TestUtils;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DemoAppConfig.class })
@TestPropertySource("/persistence-hsqldb.properties")
public class UserCRUDTest {
	@Autowired
	UserService userService;

	@Autowired
	DataSource testDataSource;

	@Test
	public void findUserTest() {
		// user inserted in script seed_data.sql

		String testUserName = "susan";
		User user = userService.findUserByUserName(testUserName);
		assertNotNull(user);
		assertEquals(user.getUsername(), testUserName);
	}

	@Test
	public void saveUserTest() throws SQLException {
		User user = new User("testUSer", "test123", true,
				Set.of(new Role("ROLE_ADMIN")));
		userService.saveUser(user);
		Connection connection = testDataSource.getConnection();
		TestUtils.executeQueryAndVerifySingleRow(
				connection, "select * from users where username='"
						+ user.getUsername() + "'",
				1, Map.of("username", user.getUsername()));
		connection.close();
	}
}
