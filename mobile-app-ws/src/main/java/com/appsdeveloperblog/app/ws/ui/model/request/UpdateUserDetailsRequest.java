package com.appsdeveloperblog.app.ws.ui.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateUserDetailsRequest {

	@NotNull(message = "First Name can not be empty")
	@Size(min = 2, max = 8, message = "Invalid First Name")
	private String firstName;
	@NotNull(message = "Last Name can not be empty")
	private String lastName;

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

}
