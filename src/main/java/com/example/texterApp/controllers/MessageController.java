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

    @PutMapping("/{id}/delete")
    public ResponseEntity<MessageDTO> deleteMessage(@PathVariable Long id, @RequestBody MessageDTO messageDTO) {
        MessageDTO deletedMessage = messageService.deleteMessage(id, messageDTO);
        return new ResponseEntity<>(deletedMessage, HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<MessageDTO> updateMessage(@PathVariable Long id, @RequestBody MessageDTO messageDTO) {
        MessageDTO deletedMessage = messageService.updateMessage(id, messageDTO);
        return new ResponseEntity<>(deletedMessage, HttpStatus.OK);
    }
}