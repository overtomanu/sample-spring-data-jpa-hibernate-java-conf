package com.luv2code.springdemo.config;

import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.luv2code.springdemo.ComponentScanMarker;
import com.luv2code.springdemo.audit.AuditorAwareImpl;
import com.luv2code.springdemo.repository.RepositoryPackageMarker;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = { RepositoryPackageMarker.class })
@EnableJpaAuditing
@ComponentScan(basePackageClasses = { ComponentScanMarker.class })
@PropertySource({ "classpath:persistence-mysql.properties" })
public class DemoAppConfig implements WebMvcConfigurer {

	@Autowired
	private Environment env;

	private Logger logger = Logger.getLogger(getClass().getName());

	// define a bean for ViewResolver

	@Bean
	public ViewResolver viewResolver() {

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;
	}

	@Bean
	public DataSource myDataSource() {

		// create connection pool
		ComboPooledDataSource myDataSource = new ComboPooledDataSource();

		// set the jdbc driver
		try {
			myDataSource.setDriverClass(env.getProperty("jdbc.driver"));
		} catch (PropertyVetoException exc) {
			throw new RuntimeException(exc);
		}

		// for sanity's sake, let's log url and user ... just to make sure we
		// are reading the data
		logger.info("jdbc.url=" + env.getProperty("jdbc.url"));
		logger.info("jdbc.user=" + env.getProperty("jdbc.user"));

		// set database connection props
		myDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		myDataSource.setUser(env.getProperty("jdbc.user"));
		myDataSource.setPassword(env.getProperty("jdbc.password"));

		// set connection pool props
		myDataSource.setInitialPoolSize(
				getIntProperty("connection.pool.initialPoolSize"));
		myDataSource
				.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
		myDataSource
				.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
		myDataSource
				.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

		return myDataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			DataSource myDataSource, JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
		emfb.setDataSource(myDataSource);
		emfb.setJpaVendorAdapter(jpaVendorAdapter);
		emfb.setPackagesToScan(env.getProperty("jpa.packagesToScan"));
		emfb.setJpaProperties(getJPAProperties());
		return emfb;
	}

	// Defining HibernateJpaVendorAdapter bean
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		// HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		// adapter.setDatabase("HSQL");
		// adapter.setShowSql(true);
		return new HibernateJpaVendorAdapter();
	}

	private Properties getJPAProperties() {

		// set hibernate properties
		Properties props = new Properties();


		// update operation is not in JPA standard for
		// javax.persistence.schema-generation.database.action
		// load data script is not called if action is update

		// use below property values to get similar behavior without using
		// ContextRefreshedEvent event listener

		// javax.persistence.schema-generation.database.action, create or
		// drop-and-create
		// props.setProperty("javax.persistence.schema-generation.database.action","drop-and-create");
		// props.setProperty("javax.persistence.sql-load-script-source","seed_data.sql");

		props.setProperty("javax.persistence.schema-generation.database.action",
				"update");

		props.setProperty("hibernate.dialect",
				env.getProperty("hibernate.dialect"));
		// setting below property may not be required if we are doing
		// adapter.setShowSql
		props.setProperty("hibernate.show_sql",
				env.getProperty("hibernate.show_sql"));

		return props;
	}

	@Bean
	public PlatformTransactionManager transactionManager(
			LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager
				.setEntityManagerFactory(entityManagerFactory.getObject());

		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	// need a helper method
	// read environment property and convert to int

	private int getIntProperty(String propName) {

		String propVal = env.getProperty(propName);

		// now convert to int
		int intPropVal = Integer.parseInt(propVal);

		return intPropVal;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/");

		// https://www.webjars.org/documentation#springmvc
		// https://stackoverflow.com/questions/44200423/webjars-locator-doesnt-work-with-xml-based-spring-mvc-4-2-x-configuration
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("/webjars/").setCachePeriod(3600)
				.resourceChain(true); // !!! very important
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories
				.createDelegatingPasswordEncoder();
	}

	/*
	@Bean
	AuditorAware<User> auditorProvider(UserService userService) {
		return () -> Optional.ofNullable(SecurityContextHolder.getContext())
				.map(SecurityContext::getAuthentication)
				.filter(Authentication::isAuthenticated)
				.map(Authentication::getPrincipal)
				.map((Object principalObj) -> (
						((UserAwareUserDetails) principalObj).getUser()));
	}
	*/

	@Bean
	AuditorAware<String> auditorProvider() { return new AuditorAwareImpl(); }
}
