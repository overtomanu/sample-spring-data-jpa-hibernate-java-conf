package com.luv2code.springdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.support.SessionAttributeStore;

import com.luv2code.springdemo.conversationSupport.ConversationalSessionAttributeStore;
import com.luv2code.springdemo.conversationSupport.SessionConversationAspect;

@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {

	// add a reference to our security data source
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder delegatingPasswordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(delegatingPasswordEncoder);

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.antMatchers("/customer/showForm*").hasAnyRole("MANAGER", "ADMIN")
			.antMatchers("/customer/save*").hasAnyRole("MANAGER", "ADMIN")
			.antMatchers("/customer/delete").hasRole("ADMIN")
			.antMatchers("/customer/**").hasRole("EMPLOYEE")
			.antMatchers("/resources/**").permitAll()
			.antMatchers("/webjars/**").permitAll()
			.and()
			.formLogin()
				.loginPage("/showMyLoginPage")
				.loginProcessingUrl("/authenticateTheUser")
				.permitAll()
			.and()
			.logout().permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/access-denied");
		
	}

	@Bean
	public SessionConversationAspect sessionConversationAspect() {
		return new SessionConversationAspect();
	}


	@Bean
	public SessionAttributeStore conversationalSessionAttributeStore() {
		return new ConversationalSessionAttributeStore();
	}
	
	// uncomment below code if you want to overwrite bean definition registry
	// for requestDataValueProcessorPostProcessor
	/*
	
	@Bean("requestDataValueProcessor")
	public ConversationIdRequestProcessor conversationIdRequestProcessor() {
		return new ConversationIdRequestProcessor();
	}
	
	@Bean
	public static BeanDefinitionRegistryPostProcessor requestDataValueProcessorPostProcessor() {
	return new BeanDefinitionRegistryPostProcessor() {
	
		@Override
		public void postProcessBeanFactory(
				ConfigurableListableBeanFactory beanFactory)
				throws BeansException {}
	
		@Override
		public void postProcessBeanDefinitionRegistry(
				BeanDefinitionRegistry registry) throws BeansException {
			registry.removeBeanDefinition("requestDataValueProcessor");
			registry.registerBeanDefinition("requestDataValueProcessor",
					new RootBeanDefinition(
							ConversationIdRequestProcessor.class));
		}
	
	};
	}
	*/
}






