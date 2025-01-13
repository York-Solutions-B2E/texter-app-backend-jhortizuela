package com.example.texterApp.controllers;

import com.example.texterApp.dtos.MessageDTO;
import com.example.texterApp.dtos.UserDTO;
import com.example.texterApp.entities.Message;
import com.example.texterApp.entities.User;
import com.example.texterApp.enums.MessageStatus;
import com.example.texterApp.mappers.UserMapper;
import com.example.texterApp.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    public void getUserById() throws Exception {
        Long userId = 1L;
        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setId(userId);
        mockUserDTO.setMessages(new ArrayList<>());
        mockUserDTO.setUsername("test");// Ensure the UserDTO is populated
        when(userService.getUserById(userId)).thenReturn(mockUserDTO);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andDo(print())  // Print the response to inspect the content
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))  // Check if the id is in the response
                .andExpect(jsonPath("$.username").value("test"));
    }
}
