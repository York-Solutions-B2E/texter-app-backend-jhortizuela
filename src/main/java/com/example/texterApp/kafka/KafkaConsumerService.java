package com.example.texterApp.kafka;

import com.example.texterApp.entities.Conversation;
import com.example.texterApp.entities.Message;
import com.example.texterApp.enums.MessageStatus;
import com.example.texterApp.mappers.MessageMapper;
import com.example.texterApp.mappers.UserMapper;
import com.example.texterApp.repositories.ConversationRepository;
import com.example.texterApp.repositories.MessageRepository;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;
    public KafkaConsumerService(KafkaTemplate<String, Event> kafkaTemplate, MessageRepository messageRepository, ConversationRepository conversationRepository, MessageMapper messageMapper, UserMapper userMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    @KafkaListener(topics = "texter_events", groupId = "texter-group")
    public void listen(ConsumerRecord<String, Event> record) {
        System.out.println("Received record: " + record.value());  // Log the incoming message

        Conversation conversation = conversationRepository.getReferenceById(1L); // Assuming you're using a fixed ID here
        Event event = record.value();

        Message message = new Message();
        message.getUser().setId(event.getUserId());
        message.getUser().setUsername(event.getUsername());
        message.setText(event.getMessage());
        message.setTimestamp(event.getTimestamp());
        message.setStatus(MessageStatus.READ);
        messageRepository.save(message); // Save the message first

        conversation.addMessage(message); // Add the message to the conversation
        conversationRepository.save(conversation);
    }
}
