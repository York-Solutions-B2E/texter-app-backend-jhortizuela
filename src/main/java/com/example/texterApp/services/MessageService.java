package com.example.texterApp.services;

import com.example.texterApp.dtos.MessageDTO;
import com.example.texterApp.entities.Conversation;
import com.example.texterApp.entities.Message;
import com.example.texterApp.enums.MessageStatus;
import com.example.texterApp.kafka.Event;
import com.example.texterApp.kafka.KafkaProducerService;
import com.example.texterApp.mappers.MessageMapper;
import com.example.texterApp.mappers.UserMapper;
import com.example.texterApp.repositories.ConversationRepository;
import com.example.texterApp.repositories.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MessageService {
    private final ConversationRepository conversationRepository;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final UserMapper userMapper;

    @Autowired
    private final KafkaProducerService kafkaProducerService;

    public MessageService(ConversationRepository conversationRepository, MessageMapper messageMapper, MessageRepository messageRepository, UserMapper userMapper, KafkaProducerService kafkaProducerService) {
        this.conversationRepository = conversationRepository;
        this.messageMapper = messageMapper;
        this.messageRepository = messageRepository;
        this.userMapper = userMapper;
        this.kafkaProducerService = kafkaProducerService;
    }

    public MessageDTO postMessageToConversation(Long conversationId, MessageDTO messageDTO) {
        Conversation conversation = conversationRepository.getReferenceById(conversationId);

        if (conversation == null) {
            throw new EntityNotFoundException("Conversation with id " + conversationId + " not found");
        }

        // Create and save the new message
        Message newMessage = messageMapper.dtoToEntity(messageDTO);
        messageRepository.save(newMessage); // This should generate the id

        // Now, retrieve the generated id
        Long messageId = newMessage.getId();
        if (messageId == null) {
            throw new IllegalStateException("Message ID was not generated.");
        }

        messageDTO.setId(messageId);

        // Add the message to the conversation and save the conversation
        conversation.addMessage(newMessage);
        conversationRepository.save(conversation);

        // Create and send the event to Kafka with the generated message ID
        Event event = new Event();
        event.setMessage(messageDTO.getText());
        event.setTimestamp(LocalDateTime.now());
        event.setUsername(messageDTO.getUser().getUsername());
        event.setUserId(messageDTO.getUser().getId());
        event.setStatus("SENT");
        event.setId(messageId);  // Set the generated message ID to the event

        // Send the event to Kafka
        kafkaProducerService.sendMessage(event);

        return messageDTO;

    }

    public MessageDTO deleteMessage(Long id, MessageDTO messageDTO) {
        //find message
        //change status to delete
        //return message with deleted status

        Message message = messageRepository.getReferenceById(id);

        if (message == null) {
            throw new EntityNotFoundException("Message with id " + id + " not found");
        }

        message.setStatus(MessageStatus.DELETE);
        messageRepository.save(message);
        return messageDTO;
    }

    public MessageDTO updateMessage(Long id, MessageDTO messageDTO) {
        Message message = messageRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Message with id " + id + " not found"));

        // Only update the fields if they are provided in the DTO
        if (messageDTO.getText() != null) {
            message.setText(messageDTO.getText());
        }
        if (messageDTO.getStatus() != null) {
            message.setStatus(messageDTO.getStatus());
        }
        if (messageDTO.getTimestamp() != null) {
            message.setTimestamp(messageDTO.getTimestamp());
        }
        if (messageDTO.getUser() != null) {
            message.setUser(userMapper.dtoToEntity(messageDTO.getUser()));
        }

        // Save the updated message to the repository
        messageRepository.save(message);

        // Return the updated message as a DTO
        return messageMapper.entityToDto(message);
    }

}
