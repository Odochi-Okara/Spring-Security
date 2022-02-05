package com.example.springsecurity.services;

import com.example.springsecurity.models.User;


import java.util.List;

public interface UserService {
	void createUser(User user);

	List<User> fetchAllUsers();
}
