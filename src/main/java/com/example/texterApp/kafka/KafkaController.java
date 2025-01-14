package com.example.texterApp.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {
    private final KafkaConsumer consumerService;

    public KafkaController(KafkaConsumer consumerService) {
        this.consumerService = consumerService;
    }

    @PostMapping("/receive")
    public ResponseEntity<ConsumerEvent> sendMessages(@RequestBody ConsumerEvent e) {
        consumerService.sendMessage("texter_events", e);
        return ResponseEntity.ok(e);
    }
}
