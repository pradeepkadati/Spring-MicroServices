package com.appsdeveloperblog.photoapp.api.users.ui.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.photoapp.api.users.service.UsersService;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.UserResponseModel;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(path = "/users")
public class UserController {

	@Autowired
	private Environment env;

	@Autowired
	private UsersService usersService;

	@GetMapping(path = "/status/check")
	public String status() {
		return "working on port " + env.getProperty("local.server.port") + "with secret key "
				+ env.getProperty("token.secret");
	}

	@PostMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CreateUserResponseModel> createUser(
			@Valid @RequestBody CreateUserRequestModel userRequestModel) {

		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDetails = mapper.map(userRequestModel, UserDto.class);
		UserDto createdUser = usersService.createUser(userDetails);

		CreateUserResponseModel returnResponse = mapper.map(createdUser, CreateUserResponseModel.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(returnResponse);
	}

	@GetMapping(path = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {

		UserDto userDetails = usersService.getUserByUserId(userId);

		UserResponseModel returnValue = new ModelMapper().map(userDetails, UserResponseModel.class);

		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}

}
