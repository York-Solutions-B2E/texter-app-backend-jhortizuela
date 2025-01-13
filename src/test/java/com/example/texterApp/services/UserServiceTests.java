package com.example.texterApp.services;

import com.example.texterApp.dtos.UserDTO;
import com.example.texterApp.entities.Message;
import com.example.texterApp.entities.User;
import com.example.texterApp.mappers.UserMapper;
import com.example.texterApp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindUserById() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("username");

        Message message = new Message();
        message.setId(1L);
        message.setUser(mockUser);
        message.setTimestamp(LocalDateTime.now());
        message.setText("message");

        mockUser.addMessage(message);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setId(userId);
        mockUserDTO.setUsername("username");

        when(userMapper.entityToDTO(mockUser)).thenReturn(mockUserDTO);

        UserDTO result = userService.findUserById(userId);

        assertEquals("username", result.getUsername());
    }

}
