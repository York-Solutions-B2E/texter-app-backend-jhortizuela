package com.example.texterApp.services;

import com.example.texterApp.dtos.UserDTO;
import com.example.texterApp.repositories.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getUserById(Long id) {
        return null;
    }

    public UserDTO findUserById(Long userId) {

    return null;
    }
}
