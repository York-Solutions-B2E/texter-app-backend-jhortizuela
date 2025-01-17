package com.example.texterApp.repositories;

import com.example.texterApp.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
//    Message findByUserIdAndTimestamp(Long userId, LocalDateTime timestamp);

    Optional<Message> findByUserIdAndTimestamp(Long userId, LocalDateTime timestamp);

    Optional<Message> findByTextAndUserIdAndTimestamp(String text, Long id, LocalDateTime timestamp);

    Optional<Message> findByUserIdAndId(Long userId, Long attr0);
}
