package com.example.texterApp;

import com.example.texterApp.entities.Conversation;
import com.example.texterApp.entities.Message;
import com.example.texterApp.entities.User;
import com.example.texterApp.enums.MessageStatus;
import com.example.texterApp.repositories.ConversationRepository;
import com.example.texterApp.repositories.MessageRepository;
import com.example.texterApp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public DatabaseSeeder(UserRepository userRepository,
                          ConversationRepository conversationRepository,
                          MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void run(String... args) {
        // Create Users
        User user1 = new User();
        user1.setUsername("john_doe");

        User user2 = new User();
        user2.setUsername("jhortizu");

        // Save users to repository
        userRepository.saveAll(Arrays.asList(user1, user2));

        // Create Conversation
        Conversation conversation = new Conversation();
        conversation.setUsers(Arrays.asList(user1, user2));

        // Save conversation to repository
        conversationRepository.save(conversation);

        // Create Messages
        Message message1 = new Message();
        message1.setText("Hi Jane, how are you?");
        message1.setTimestamp(LocalDateTime.now());
        message1.setStatus(MessageStatus.SENT);
        message1.setUser(user1);
        message1.setConversation(conversation);

        Message message2 = new Message();
        message2.setText("Hi John! I'm good, how about you?");
        message2.setTimestamp(LocalDateTime.now().plusMinutes(1));
        message2.setStatus(MessageStatus.SENT);
        message2.setUser(user2);
        message2.setConversation(conversation);

        Message message3 = new Message();
        message3.setText("I'm doing well, thanks for asking!");
        message3.setTimestamp(LocalDateTime.now().plusMinutes(2));
        message3.setStatus(MessageStatus.SENT);
        message3.setUser(user1);
        message3.setConversation(conversation);

        messageRepository.saveAll(Arrays.asList(message1, message2, message3));

        // Add messages to the conversation
        conversation.addMessage(message1);
        conversation.addMessage(message2);
        conversation.addMessage(message3);

        // Save messages to repository

        // Update users with the conversation
        user1.setConversations(Arrays.asList(conversation));
        user2.setConversations(Arrays.asList(conversation));

        userRepository.saveAll(Arrays.asList(user1, user2));
    }
}
