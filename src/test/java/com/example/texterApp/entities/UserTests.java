package com.example.texterApp.entities;

import com.example.texterApp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserEntity() {
        // Create a new User
        User user = new User();
        user.setUsername("john_doe");

        // Create Messages
        Message message1 = new Message();
        message1.setText("Hello, World!");
        message1.setTimestamp(new Timestamp(System.currentTimeMillis()));
        message1.setUser(user);

        Message message2 = new Message();
        message2.setText("Spring Boot is great!");
        message2.setTimestamp(new Timestamp(System.currentTimeMillis()));
        message2.setUser(user);

        // Add Messages to User
        user.setMessages(List.of(message1, message2));

        // Save User (and Messages due to CascadeType.ALL)
        userRepository.save(user);

        // Retrieve User from the database
        User retrievedUser = userRepository.findById(user.getId()).orElse(null);

        // Assertions
        assertNotNull(retrievedUser);
        assertEquals("john_doe", retrievedUser.getUsername());
        assertEquals(2, retrievedUser.getMessages().size());
    }
}
