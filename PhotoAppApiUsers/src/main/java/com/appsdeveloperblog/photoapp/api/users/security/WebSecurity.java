package com.appsdeveloperblog.photoapp.api.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.photoapp.api.users.service.UsersService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    
	private Environment environment;
	private UsersService userService; 
	private BCryptPasswordEncoder  bCryptPasswordEncoder;
	@Autowired
	public WebSecurity(Environment environment,UsersService userService,BCryptPasswordEncoder  bCryptPasswordEncoder) {
		this.environment = environment;
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		//http.authorizeRequests().antMatchers("/users/**").permitAll();
		//http.authorizeRequests().antMatchers("/**").hasIpAddress(environment.getProperty("gateway.ip"));
		http.authorizeRequests().antMatchers("/users/**").permitAll().and().addFilter(getAuthenticationFilter());
		http.headers().frameOptions().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}

	private AuthenticationFilter getAuthenticationFilter() throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService,environment,authenticationManager());
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		return authenticationFilter;
	}
}
