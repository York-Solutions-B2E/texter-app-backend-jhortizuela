package com.example.texterApp.controllers;

import com.example.texterApp.dtos.MessageDTO;
import com.example.texterApp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/conversations/{conversationId}")
    public ResponseEntity<MessageDTO> postMessageToConversation(
            @PathVariable Long conversationId, @RequestBody MessageDTO messageDTO) {
        MessageDTO createdMessage = messageService.postMessageToConversation(conversationId, messageDTO);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }
}