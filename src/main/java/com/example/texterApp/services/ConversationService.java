package com.example.texterApp.services;

import com.example.texterApp.dtos.ConversationDTO;
import com.example.texterApp.dtos.MessageDTO;
import com.example.texterApp.entities.Conversation;
import com.example.texterApp.mappers.ConversationMapper;
import com.example.texterApp.repositories.ConversationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;
    private MessageDTO messageDTO;

    public ConversationService(ConversationRepository conversationRepository, ConversationMapper conversationMapper) {
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
    }

    @Transactional
    public ConversationDTO getConversationById(Long id) {
        Optional<Conversation> conversation = conversationRepository.findById(id);

        if(conversation.isPresent()) {
            return conversationMapper.entityToDto(conversation.get());
        } else {
            throw new EntityNotFoundException("Conversation with id " + id + " not found");
        }
    }

}
