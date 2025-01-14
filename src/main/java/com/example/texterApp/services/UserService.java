package com.example.texterApp.services;

import com.example.texterApp.dtos.UserDTO;
import com.example.texterApp.entities.User;
import com.example.texterApp.mappers.UserMapper;
import com.example.texterApp.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return userMapper.entityToDTO(user.get());
        } else {
            throw new EntityNotFoundException("User not found with id " + userId);
        }
    }
}
