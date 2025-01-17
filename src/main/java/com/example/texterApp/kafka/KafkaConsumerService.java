package com.example.texterApp.kafka;

import com.example.texterApp.entities.Conversation;
import com.example.texterApp.entities.Message;
import com.example.texterApp.entities.User;
import com.example.texterApp.enums.MessageStatus;
import com.example.texterApp.mappers.MessageMapper;
import com.example.texterApp.mappers.UserMapper;
import com.example.texterApp.repositories.ConversationRepository;
import com.example.texterApp.repositories.MessageRepository;
import com.example.texterApp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.getUser;

@Service
public class KafkaConsumerService {
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public KafkaConsumerService(KafkaTemplate<String, Event> kafkaTemplate, MessageRepository messageRepository, ConversationRepository conversationRepository, MessageMapper messageMapper, UserMapper userMapper, UserRepository userRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    @KafkaListener(topics = "texter_events", groupId = "texter-group")
    public void listen(ConsumerRecord<String, Event> record) {
        Event event = record.value();
        System.out.println("Received event: " + event);  // Log the incoming event

        // Check if the event has the required fields


        // Find the existing message using userId and messageId
        Optional<Message> optionalMessage = messageRepository.findByUserIdAndId(event.getUserId(), event.getId());

        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();

            // Check the status from the event and update the message accordingly
            if (event.getStatus() != null) {
                // If the status is "DELETED", update the status to DELETED
                if (event.getStatus().equals("DELETED")) {
                    if (message.getStatus() != MessageStatus.DELETE) {
                        message.setStatus(MessageStatus.DELETE); // Set to DELETED status
                        messageRepository.save(message);  // Save the updated message
                        System.out.println("Updated message status to DELETED");
                    } else {
                        System.out.println("Message already marked as DELETED, no update needed.");
                    }
                }
                // If the status is "READ", update the status to READ
                else if (event.getStatus().equals("READ")) {
                    if (message.getStatus() != MessageStatus.READ) {
                        message.setStatus(MessageStatus.READ); // Set to READ status
                        messageRepository.save(message);  // Save the updated message
                        System.out.println("Updated message status to READ");
                    } else {
                        System.out.println("Message already marked as READ, no update needed.");
                    }
                }
                else if (event.getStatus().equals("SENT")) {
                    message.setStatus(MessageStatus.READ);
                    messageRepository.save(message);
                }
//                else if (!optionalMessage.get().getText().equals(event.getMessage())) {
//                    // Update the existing message
//                    optionalMessage.get().setText(event.getMessage());
//                    optionalMessage.get().setTimestamp(event.getTimestamp()); // Update timestamp if necessary
//                    messageRepository.save(optionalMessage.get());
//                    System.out.println("Updated existing message: ");
//                }
            }
        } else {
            convertEventToMessage(event);
            System.out.println("CONSUMED MESSAGE");
        }
    }

    private Message convertEventToMessage(Event event) {
        Message message = new Message();

        // Set the message content
        message.setText(event.getMessage());

        // Convert timestamp to LocalDateTime
        message.setTimestamp(event.getTimestamp());

        // Set status
        if (event.getStatus() != null) {
            message.setStatus(MessageStatus.READ);
        }

        // Create or find the User
        User user = userRepository.findById(event.getUserId())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setId(event.getUserId());
                    newUser.setUsername(event.getUsername());
                    return userRepository.save(newUser);
                });
        message.setUser(user);

        messageRepository.save(message);
        return message;
    }

}
