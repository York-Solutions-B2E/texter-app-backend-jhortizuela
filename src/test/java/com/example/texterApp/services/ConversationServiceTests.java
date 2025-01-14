package com.example.texterApp.services;

import com.example.texterApp.dtos.ConversationDTO;
import com.example.texterApp.dtos.MessageDTO;
import com.example.texterApp.entities.Conversation;
import com.example.texterApp.entities.Message;
import com.example.texterApp.entities.User;
import com.example.texterApp.mappers.ConversationMapper;
import com.example.texterApp.mappers.UserMapper;
import com.example.texterApp.repositories.ConversationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConversationServiceTests {
    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private ConversationMapper conversationMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ConversationService conversationService;

    @Test
    public void testFindByConversationId() {


        Long conversationId = 1L;

        // Create users
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("john_doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("jhortizu");

        // Create messages
        Message message1 = new Message();
        message1.setId(1L);
        message1.setText("Hi Jane, how are you?");
        message1.setUser(user1);

        Message message2 = new Message();
        message2.setId(2L);
        message2.setText("Hi John! I'm good, how about you?");
        message2.setUser(user2);

        // Create conversation
        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setUsers(Arrays.asList(user1, user2));
        conversation.setMessages(Arrays.asList(message1, message2));

        MessageDTO messageDTO1 = new MessageDTO();
        messageDTO1.setId(message1.getId());
        messageDTO1.setText(message1.getText());

        MessageDTO messageDTO2 = new MessageDTO();
        messageDTO2.setId(message2.getId());
        messageDTO2.setText(message2.getText());

        // Mock the DTO returned by the mapper
        ConversationDTO mockConversationDTO = new ConversationDTO();
        mockConversationDTO.setId(conversationId);
        mockConversationDTO.setUsers(Arrays.asList(userMapper.entityToDTO(user1), userMapper.entityToDTO(user2)));
        mockConversationDTO.setMessages(Arrays.asList(messageDTO1, messageDTO2));

        // Mock repository and mapper
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(conversationMapper.entityToDto(conversation)).thenReturn(mockConversationDTO);

        // Call service method
        ConversationDTO result = conversationService.getConversationById(conversationId);

        // Assert results
        assertNotNull(result);
        assertEquals(conversationId, result.getId());
        assertEquals(2, result.getUsers().size());
        assertEquals(2, result.getMessages().size());
        assertEquals("Hi Jane, how are you?", result.getMessages().get(0).getText());

        // Verify interactions
        verify(conversationRepository, times(1)).findById(conversationId);
        verify(conversationMapper, times(1)).entityToDto(conversation);
    }
}
