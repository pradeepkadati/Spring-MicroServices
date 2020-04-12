package com.appsdeveloperblog.app.ws.ui.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDetailsRequest {
	
	@NotNull(message = "First Name can not be empty")
	@Size(min = 2,max = 8,message = "Invalid First Name")
	private String firstName;
	@NotNull(message = "Last Name can not be empty")
	private String lastName;
	@NotNull(message = "Email can not be empty")
	@Email
	private String email;
	private String userId;
	@NotNull(message = "Password can not be empty")
	@Size(min = 6,max = 8,message = "Invalid Password")
	private String password;
	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
