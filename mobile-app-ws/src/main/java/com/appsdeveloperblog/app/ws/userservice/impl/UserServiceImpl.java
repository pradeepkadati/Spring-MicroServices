package com.appsdeveloperblog.app.ws.userservice.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequest;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;
import com.appsdeveloperblog.app.ws.userservice.UserService;

@Service
public class UserServiceImpl implements UserService {

	Map<String, UserRest> users = new HashMap<>();

	private Utils utils;

	public UserServiceImpl() {
		super();

	}

	@Autowired
	public UserServiceImpl(Utils utils) {
		super();
		this.utils = utils;
	}

	@Override
	public UserRest createUser(UserDetailsRequest request) {

		UserRest user = new UserRest();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());

		String userId = utils.generateUserId();
		user.setUserId(userId);
		users.put(userId, user);

		return user;
	}
}
