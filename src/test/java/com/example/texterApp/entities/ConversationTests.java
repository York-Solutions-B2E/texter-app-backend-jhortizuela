package com.example.texterApp.entities;

import com.example.texterApp.enums.MessageStatus;
import com.example.texterApp.repositories.ConversationRepository;
import com.example.texterApp.repositories.MessageRepository;
import com.example.texterApp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ConversationTests {
    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void testConversationEntity() {
        // Step 1: Create Users
        User user1 = new User();
        user1.setUsername("john_doe");

        User user2 = new User();
        user2.setUsername("jane_doe");

        // Step 2: Create Conversation and associate Users
        Conversation conversation = new Conversation();
        conversation.setUsers(List.of(user1, user2));

        // Step 3: Create and associate Messages
        Message message1 = new Message();
        message1.setText("Hello from John!");
        message1.setTimestamp(LocalDateTime.now());
        message1.setStatus(MessageStatus.SENT);
        message1.setConversation(conversation);

        Message message2 = new Message();
        message2.setText("Hi John, this is Jane!");
        message2.setTimestamp(LocalDateTime.now());
        message2.setStatus(MessageStatus.SENT);
        message2.setConversation(conversation);

        conversation.setMessages(List.of(message1, message2));

        // Step 4: Save the Conversation (cascade saves Users and Messages)
        conversationRepository.save(conversation);

        // Step 5: Retrieve and verify Conversation from the database
        Conversation retrievedConversation = conversationRepository.findById(conversation.getId()).orElse(null);

        // Assertions to verify Conversation
        assertNotNull(retrievedConversation, "Retrieved conversation should not be null");
        assertEquals(2, retrievedConversation.getUsers().size(), "Conversation should have two users");
        assertEquals(2, retrievedConversation.getMessages().size(), "Conversation should have two messages");

        // Assertions to verify Users
        List<User> retrievedUsers = retrievedConversation.getUsers();
        assertEquals("john_doe", retrievedUsers.get(0).getUsername());
        assertEquals("jane_doe", retrievedUsers.get(1).getUsername());

        // Assertions to verify Messages
        List<Message> retrievedMessages = retrievedConversation.getMessages();
        assertEquals("Hello from John!", retrievedMessages.get(0).getText());
        assertEquals("Hi John, this is Jane!", retrievedMessages.get(1).getText());
    }

}
