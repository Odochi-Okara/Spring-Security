package com.example.springsecurity.controllers;

import com.example.springsecurity.models.User;
import com.example.springsecurity.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	public UserController(final UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/get-users")
	public ResponseEntity<List<User>> fetchAllUsers () {
		List<User> userList = userService.fetchAllUsers();

		return new ResponseEntity(userList, HttpStatus.OK);
	}
}
