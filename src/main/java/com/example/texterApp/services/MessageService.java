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

    public MessageDTO deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message with id " + id + " not found"));

        // Change the status to 'DELETE'
        message.setStatus(MessageStatus.DELETE);
        messageRepository.save(message);  // Save the message to update the status

        // Do not modify the order of messages in the conversation
        Conversation conversation = message.getConversation();
        // No need to modify the messages list; it will remain the same

        // Create the event to be sent to Kafka
        Event event = new Event();
        event.setId(message.getId());  // Set the message ID
        event.setMessage(message.getText());  // Optionally, include the message text
        event.setTimestamp(LocalDateTime.now());  // Set the timestamp
        event.setUsername(message.getUser().getUsername());  // Set the username
        event.setUserId(message.getUser().getId());  // Set the user ID
        event.setStatus("DELETED");  // Set the new status as "DELETED"

        // Send the event to Kafka
        kafkaProducerService.sendMessage(event);

        // Return the updated message DTO (optional)
        return messageMapper.entityToDto(message);
    }

    public MessageDTO updateMessage(Long id, MessageDTO messageDTO) {
        Message message = messageRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Message with id " + id + " not found"));

        // Check if the new text is different from the existing text
        if (!messageDTO.getText().equals(message.getText())) {
            // Update the message text
            message.setText(messageDTO.getText());

            // Save the updated message to the repository
            messageRepository.save(message);

            // Create the event to be sent to Kafka
            Event event = new Event();
            event.setId(message.getId());  // Set the message ID
            event.setMessage(message.getText());  // Set the updated message text
            event.setTimestamp(LocalDateTime.now());  // Set the current timestamp
            event.setUsername(message.getUser().getUsername());  // Set the username
            event.setUserId(message.getUser().getId());  // Set the user ID
            event.setStatus(message.getStatus().name());  // Set the current status

            // Send the event to Kafka
            kafkaProducerService.sendMessage(event);
        }

        // Return the updated message as a DTO
        return messageMapper.entityToDto(message);
    }

}
