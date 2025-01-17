package com.example.texterApp.mappers;

import com.example.texterApp.dtos.ConversationDTO;
import com.example.texterApp.entities.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationMapper INSTANCE = Mappers.getMapper(ConversationMapper.class);

    ConversationDTO entityToDto(Conversation conversation);
    Conversation dtoToEntity(ConversationDTO conversationDTO);
}
