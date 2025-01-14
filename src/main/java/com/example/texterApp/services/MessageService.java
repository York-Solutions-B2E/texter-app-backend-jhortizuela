package com.example.texterApp.services;

import com.example.texterApp.dtos.MessageDTO;
import com.example.texterApp.entities.Conversation;
import com.example.texterApp.mappers.MessageMapper;
import com.example.texterApp.repositories.ConversationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final ConversationRepository conversationRepository;
    private final MessageMapper messageMapper;

    public MessageService(ConversationRepository conversationRepository, MessageMapper messageMapper) {
        this.conversationRepository = conversationRepository;
        this.messageMapper = messageMapper;
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
}
