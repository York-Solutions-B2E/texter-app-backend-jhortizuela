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

    public ConversationDTO(Long id, List<UserDTO> users, List<MessageDTO> messages) {
        this.id = id;
        this.users = users;
        this.messages = messages;
    }

    public ConversationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }
}
