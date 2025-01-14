package com.example.texterApp.controllers;

import com.example.texterApp.dtos.ConversationDTO;
import com.example.texterApp.repositories.ConversationRepository;
import com.example.texterApp.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ConversationService conversationService;

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDTO> getConversationById(@PathVariable Long id) {
        ConversationDTO conversationDTO = conversationService.getConversationById(id);
        return ResponseEntity.ok(conversationDTO);
    }
}
