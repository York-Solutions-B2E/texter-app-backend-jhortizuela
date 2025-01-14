package com.example.texterApp.kafka;

import com.example.texterApp.entities.Message;
import com.example.texterApp.enums.MessageStatus;
import com.example.texterApp.repositories.MessageRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final KafkaTemplate<String, ConsumerEvent> kafkaTemplate;
    private final MessageRepository messageRepository;

    public KafkaConsumer(KafkaTemplate<String, ConsumerEvent> kafkaTemplate, MessageRepository messageRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageRepository = messageRepository;
    }

    public void sendMessage(String topic, ConsumerEvent e) {
        kafkaTemplate.send(topic, e);
//        System.out.println("Message sent: " + e);
    }

    @KafkaListener(topics = "texter_events", groupId = "texter-group")
    public void listen(ConsumerRecord<String, ConsumerEvent> record) {
        ConsumerEvent event = record.value();

        Message message = new Message();
        message.setUser(event.getUser());
        message.setText(event.getMessage());
        message.setTimestamp(event.getTimestamp());
        message.setStatus(MessageStatus.READ);
        messageRepository.save(message);
    }
}
