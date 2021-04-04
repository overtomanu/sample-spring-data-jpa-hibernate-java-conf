package com.luv2code.springdemo.component;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import com.luv2code.springdemo.service.UserService;

@Component
public class OnApplicationStartUp {

	public static final String SEED_USER = "susan";

	@Autowired
	private UserService userService;

	@Autowired
	private DataSource dataSource;

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {

		// Write your business logic here.
		if (userService.findUserByUserName(SEED_USER) == null) {
			preloadData();
		}
	}

	private void preloadData() {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(
				false, false, "UTF-8", new ClassPathResource("seed_data.sql"));
		resourceDatabasePopulator.execute(dataSource);
	}
}
