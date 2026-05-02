package com.devops.portfolio.service;

import com.devops.portfolio.entity.User;
import com.devops.portfolio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUser() {
        User user = userService.registerUser("testuser", "test@example.com", "password123", "Test User");
        
        assertNotNull(user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getFullName());
        assertTrue(user.getEnabled());
    }

    @Test
    public void testRegisterUserDuplicateUsername() {
        userService.registerUser("testuser", "test@example.com", "password123", "Test User");
        
        assertThrows(RuntimeException.class, () -> {
            userService.registerUser("testuser", "other@example.com", "password123", "Other User");
        });
    }

    @Test
    public void testAuthenticateUser() {
        userService.registerUser("testuser", "test@example.com", "password123", "Test User");
        
        Optional<User> authenticatedUser = userService.authenticateUser("testuser", "password123");
        assertTrue(authenticatedUser.isPresent());
        assertEquals("testuser", authenticatedUser.get().getUsername());
    }

    @Test
    public void testAuthenticateUserWrongPassword() {
        userService.registerUser("testuser", "test@example.com", "password123", "Test User");
        
        Optional<User> authenticatedUser = userService.authenticateUser("testuser", "wrongpassword");
        assertFalse(authenticatedUser.isPresent());
    }
}
