package com.example.texterApp.dtos;

import com.example.texterApp.entities.Message;
import com.example.texterApp.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDTO {
    public Long id;
    public List<UserDTO> users;
    public List<MessageDTO> messages;
}
