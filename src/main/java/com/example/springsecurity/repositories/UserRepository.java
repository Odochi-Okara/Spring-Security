package com.example.springsecurity.repositories;

import com.example.springsecurity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findUserByUsername (final String username);
}
