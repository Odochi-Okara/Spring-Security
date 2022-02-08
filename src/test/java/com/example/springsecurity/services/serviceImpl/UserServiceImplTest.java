package com.example.springsecurity.services.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springsecurity.models.User;
import com.example.springsecurity.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Test
    void testFetchAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        when(this.userRepository.findAll()).thenReturn(userList);
        List<User> actualFetchAllUsersResult = this.userServiceImpl.fetchAllUsers();
        assertSame(userList, actualFetchAllUsersResult);
        assertTrue(actualFetchAllUsersResult.isEmpty());
        verify(this.userRepository).findAll();
    }

    @Test
    void testFetchAllUsers2() {
        when(this.userRepository.findAll()).thenThrow(new UsernameNotFoundException("Msg"));
        assertThrows(UsernameNotFoundException.class, () -> this.userServiceImpl.fetchAllUsers());
        verify(this.userRepository).findAll();
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        when(this.userRepository.save((User) any())).thenReturn(user);
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        this.userServiceImpl.createUser(user1);
        verify(this.userRepository).save((User) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
        assertEquals("secret", user1.getPassword());
    }

    @Test
    void testCreateUser2() {
        when(this.userRepository.save((User) any())).thenThrow(new UsernameNotFoundException("Msg"));
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        assertThrows(UsernameNotFoundException.class, () -> this.userServiceImpl.createUser(user));
        verify(this.userRepository).save((User) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
    }

    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(this.userRepository.findUserByUsername((String) any())).thenReturn(ofResult);
        assertSame(user, this.userServiceImpl.loadUserByUsername("janedoe"));
        verify(this.userRepository).findUserByUsername((String) any());
    }

    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        when(this.userRepository.findUserByUsername((String) any())).thenThrow(new UsernameNotFoundException("Msg"));
        assertThrows(UsernameNotFoundException.class, () -> this.userServiceImpl.loadUserByUsername("janedoe"));
        verify(this.userRepository).findUserByUsername((String) any());
    }

    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        when(this.userRepository.findUserByUsername((String) any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> this.userServiceImpl.loadUserByUsername("janedoe"));
        verify(this.userRepository).findUserByUsername((String) any());
    }
}

