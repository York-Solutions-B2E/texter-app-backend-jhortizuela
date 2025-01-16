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
        if (event.getUserId() == null || event.getId() == null) {
            System.out.println("Invalid event data, skipping.");
            return;
        }

        // Find the existing message using userId and messageId
        Optional<Message> optionalMessage = messageRepository.findByUserIdAndId(event.getUserId(), event.getId());

        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();

            // If the message exists and has not been marked as "READ", update the status
            if (message.getStatus() != MessageStatus.READ) {
                message.setStatus(MessageStatus.READ);
                messageRepository.save(message); // Save the updated message
                System.out.println("Updated message status to READ");
            } else {
                System.out.println("Message already marked as READ, no update needed.");
            }
        } else {
            System.out.println("Message not found, skipping the status update.");
        }


    }
}
