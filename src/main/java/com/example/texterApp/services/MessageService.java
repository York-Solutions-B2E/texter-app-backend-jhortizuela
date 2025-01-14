package com.example.texterApp.services;

import com.example.texterApp.dtos.MessageDTO;
import com.example.texterApp.entities.Conversation;
import com.example.texterApp.entities.Message;
import com.example.texterApp.enums.MessageStatus;
import com.example.texterApp.mappers.MessageMapper;
import com.example.texterApp.repositories.ConversationRepository;
import com.example.texterApp.repositories.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final ConversationRepository conversationRepository;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;

    public MessageService(ConversationRepository conversationRepository, MessageMapper messageMapper, MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageMapper = messageMapper;
        this.messageRepository = messageRepository;
    }

    public MessageDTO postMessageToConversation(Long conversationId, MessageDTO messageDTO) {
        //find conversation
        //add message to conversation
        Conversation conversation = conversationRepository.getReferenceById(conversationId);

        if (conversation == null) {
            throw new EntityNotFoundException("Conversation with id " + conversationId + " not found");
        }

        conversation.addMessage(messageMapper.dtoToEntity(messageDTO));
        conversationRepository.save(conversation);

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
}
