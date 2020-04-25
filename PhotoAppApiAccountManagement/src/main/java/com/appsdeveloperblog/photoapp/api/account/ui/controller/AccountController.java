package com.appsdeveloperblog.photoapp.api.account.ui.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

	@GetMapping(path = "/status/check")
	public String status() {
		return "working..";
	}
}
