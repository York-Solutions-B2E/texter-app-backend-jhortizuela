package com.example.texterApp.entities;

import com.example.texterApp.enums.MessageStatus;
import com.example.texterApp.repositories.ConversationRepository;
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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MessageTests {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Test
    public void testMessageEntity() {
        // Create a new User
        User user = new User();
        user.setUsername("john_doe");
        userRepository.save(user);

        // Create a new Message
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 13, 12, 32, 54);
        Message message = new Message();
        message.setText("Hello, World!");
        message.setTimestamp(dateTime);
        message.setUser(user);
        message.setStatus(MessageStatus.SENT);

        Conversation conversation = new Conversation();
        conversationRepository.save(conversation);

        message.setConversation(conversation);

        // Persist the Message
        messageRepository.save(message);

        // Retrieve the Message from the database
        Message retrievedMessage = messageRepository.findById(message.getId()).get();

        // Assertions
        assertNotNull(retrievedMessage);
        assertEquals("Hello, World!", retrievedMessage.getText());
        assertEquals(dateTime, retrievedMessage.getTimestamp());
        assertEquals(user.getId(), retrievedMessage.getUser().getId());
    }
}
