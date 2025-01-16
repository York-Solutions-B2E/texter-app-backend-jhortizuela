package com.example.texterApp.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {
    private final KafkaTemplate<String, Event> kafkaTemplate;

    public KafkaController(KafkaTemplate<String, Event> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String topic, @RequestBody Event event) {
        kafkaTemplate.send(topic, event);
        return "Message sent to topic: " + topic;
    }
}
