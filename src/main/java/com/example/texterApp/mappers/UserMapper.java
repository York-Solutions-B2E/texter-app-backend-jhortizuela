package com.example.texterApp.mappers;

import com.example.texterApp.dtos.UserDTO;
import com.example.texterApp.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO entityToDTO(User user);
    User dtoToEntity(UserDTO userDTO);
}
