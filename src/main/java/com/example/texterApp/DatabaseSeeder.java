package com.example.texterApp;

import com.example.texterApp.entities.User;
import com.example.texterApp.entities.Message;
import com.example.texterApp.repositories.UserRepository;
import com.example.texterApp.repositories.MessageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public DatabaseSeeder(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void run(String... args) {
        // Check if data already exists to prevent duplicate entries
        if (userRepository.count() == 0) {
            seedUsers();
        }
    }

    private void seedUsers() {
        // Create sample users
        User user1 = new User();
        user1.setUsername("john_doe");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("jane_doe");
        userRepository.save(user2);

        // Create sample messages
        Message message1 = new Message();
        message1.setText("Hello, this is a message from John.");
        message1.setTimestamp(LocalDateTime.now());
        message1.setUser(user1);
        messageRepository.save(message1);

        Message message2 = new Message();
        message2.setText("Hi, this is a message from Jane.");
        message2.setTimestamp(LocalDateTime.now());
        message2.setUser(user2);
        messageRepository.save(message2);

        System.out.println("Sample data seeded.");
    }
}
