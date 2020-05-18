package com.appsdeveloperblog.photoapp.api.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.appsdeveloperblog.photoapp.api.users.service.UsersService;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.LoginRequestModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UsersService usersService;
	private Environment environment;
	
	public AuthenticationFilter(UsersService usersService,Environment environment,AuthenticationManager authenticationManager) {
		this.usersService = usersService;
		this.environment = environment;
		super.setAuthenticationManager(authenticationManager);
	}
	
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			LoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(),
					LoginRequestModel.class);
			
			return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
					creds.getEmail(), creds.getPassword(),new ArrayList<>()) );
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		
		String username = (( User) auth.getPrincipal()).getUsername();
		UserDto userDetails = usersService.getUserDetailsByEmail(username);
		
		String token = Jwts.builder().setSubject(userDetails.getUserId())
				       .setExpiration(new Date(System.currentTimeMillis() 
				    		   + Long.parseLong(environment.getProperty("token.expiration.value"))))
				       .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				       .compact();
		System.out.println("------------token.expiration.value-- "+Long.parseLong(environment.getProperty("token.expiration.value")));
                System.out.println("------------token.secret-- "+environment.getProperty("token.secret"));
		response.addHeader("token", token);
		response.addHeader("userId", userDetails.getUserId());		
	}
}
