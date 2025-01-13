package com.example.texterApp.entities;

import com.example.texterApp.repositories.MessageRepository;
import com.example.texterApp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MessageTests {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testMessageEntity() {
        // Create a new User
        User user = new User();
        user.setUsername("john_doe");
        userRepository.save(user);

        // Create a new Message
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Message message = new Message();
        message.setText("Hello, World!");
        message.setTimestamp(timestamp);
        message.setUser(user);

        // Persist the Message
        messageRepository.save(message);

        // Retrieve the Message from the database
        Message retrievedMessage = messageRepository.findById(message.getId()).get();

        // Assertions
        assertNotNull(retrievedMessage);
        assertEquals("Hello, World!", retrievedMessage.getText());
        assertEquals(timestamp, retrievedMessage.getTimestamp());
        assertEquals(user.getId(), retrievedMessage.getUser().getId());
    }
}
