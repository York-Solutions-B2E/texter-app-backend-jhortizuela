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
        user1.setUsername("ashton");

        User user2 = new User();
        user2.setUsername("janika");

        // Save users to repository
        userRepository.saveAll(Arrays.asList(user1, user2));

        // Create Conversation
        Conversation conversation = new Conversation();
        conversation.addUser(user1);
        conversation.addUser(user2);

        // Save conversation to repository
        conversationRepository.save(conversation);

        // Create Messages
        Message message1 = new Message();
        message1.setText("Hi Janika, how's your project going?");
        message1.setTimestamp(LocalDateTime.now());
        message1.setStatus(MessageStatus.SENT);
        message1.setUser(user1);
        message1.setConversation(conversation);

        Message message2 = new Message();
        message2.setText("Yo yo yo i'm killing it!");
        message2.setTimestamp(LocalDateTime.now().plusMinutes(1));
        message2.setStatus(MessageStatus.SENT);
        message2.setUser(user2);
        message2.setConversation(conversation);

        Message message3 = new Message();
        message3.setText("It's going perfect!");
        message3.setTimestamp(LocalDateTime.now().plusMinutes(2));
        message3.setStatus(MessageStatus.SENT);
        message3.setUser(user1);
        message3.setConversation(conversation);

        // Add messages to the conversation
        conversation.addMessage(message1);
        conversation.addMessage(message2);
        conversation.addMessage(message3);

        // Save updated conversation (and its messages)
        conversationRepository.save(conversation);
    }


}
