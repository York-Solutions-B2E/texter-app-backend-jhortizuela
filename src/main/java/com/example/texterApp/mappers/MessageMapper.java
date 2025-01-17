package com.example.texterApp.mappers;

import com.example.texterApp.dtos.MessageDTO;
import com.example.texterApp.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDTO entityToDto(Message message);
    Message dtoToEntity(MessageDTO messageDTO);
}
