package com.appsdeveloperblog.app.ws.ui.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.exception.UserServiceException;
import com.appsdeveloperblog.app.ws.ui.model.request.UpdateUserDetailsRequest;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequest;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;
import com.appsdeveloperblog.app.ws.userservice.UserService;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {

	
	Map<String, UserRest> users = new HashMap<>();
	@Autowired
	private UserService userService; 
	
	@GetMapping()
	public String getUsers(@RequestParam(name= "page",defaultValue = "1") int page ,
			@RequestParam(name= "limit",defaultValue = "50") int limit ) {
		return "get Users was called with page "+ page + " and limit " + limit;
	}
	
	@GetMapping(path = "/{userId}",produces = {MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE})
	public  ResponseEntity<UserRest>  getUser(@PathVariable String userId) {
		
		if (userId != null) {
			throw new UserServiceException("User exception is thrown");
		}
		
		if (users.containsKey(userId)) {
		 return new ResponseEntity<>(users.get(userId),HttpStatus.OK);
		 
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);	
		}
		
	}
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<UserRest> CreateUser(@Valid @RequestBody UserDetailsRequest userRequest) {
		
		UserRest user = userService.createUser(userRequest);
		
		ResponseEntity<UserRest> response = new ResponseEntity<>(user,HttpStatus.OK);
		
		return response;
	}
	
	@PutMapping(path = "/{userId}",consumes = {MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE})
	public  ResponseEntity<UserRest> UpdateUser(@PathVariable String userId,@Valid @RequestBody UpdateUserDetailsRequest userRequest) {
		
		if (users.containsKey(userId)) {
			
			UserRest user = users.get(userId);
			
			user.setFirstName(userRequest.getFirstName());
			user.setLastName(userRequest.getLastName());
			
			 return new ResponseEntity<>(user,HttpStatus.OK);
			 
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);	
		}
		
		
	}
	
	@DeleteMapping(path = "/{userId}")
	public ResponseEntity<Void> DeleteUser(@PathVariable String userId) {
		
		users.remove(userId);
		
		return ResponseEntity.noContent().build();
	}
}
