package com.example.texterApp.controllers;

import com.example.texterApp.dtos.ConversationDTO;
import com.example.texterApp.entities.Conversation;
import com.example.texterApp.entities.Message;
import com.example.texterApp.entities.User;
import com.example.texterApp.enums.MessageStatus;
import com.example.texterApp.mappers.ConversationMapper;
import com.example.texterApp.repositories.ConversationRepository;
import com.example.texterApp.repositories.MessageRepository;
import com.example.texterApp.repositories.UserRepository;
import com.example.texterApp.services.ConversationService;
import org.apache.zookeeper.server.controller.ControllerService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConversationController.class)
public class ConversationControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ControllerService controllerService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private MessageRepository messageRepository;
    @MockBean
    private ConversationRepository conversationRepository;
    @MockBean
    private ConversationMapper conversationMapper;
    @MockBean
    private ConversationService conversationService;


    @Test
    public void testGetConversationById() throws Exception {
        User user1 = new User();
        user1.setUsername("janika");

        User user2 = new User();
        user2.setUsername("ashton");
        userRepository.save(user1);
        userRepository.save(user2);

        Message message1 = new Message();
        message1.setText("Hi ashton");
        message1.setUser(user1);
        message1.setStatus(MessageStatus.READ);

        Message message2 = new Message();
        message2.setText("Hi janika");
        message2.setUser(user2);
        message2.setStatus(MessageStatus.READ);

        Conversation conversation = new Conversation();
        conversation.setId(1L);
        conversation.setUsers(List.of(user1, user2));

        message1.setConversation(conversation);
        message2.setConversation(conversation);
        messageRepository.save(message1);
        messageRepository.save(message2);

        conversationRepository.save(conversation);


        ConversationDTO mockConversation = conversationMapper.entityToDto(conversation);

        when(conversationService.getConversationById(1L)).thenReturn(mockConversation);

        mockMvc.perform(get("/api/conversations/{id}", 1L))
                .andExpect(status().isOk());
    }
}
