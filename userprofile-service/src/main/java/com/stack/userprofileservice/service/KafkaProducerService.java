package com.stack.userprofileservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stack.userprofileservice.dto.AuthRequest;
import com.stack.userprofileservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    @Autowired
    private ObjectMapper objectMapper;

    @Value("user-topic")
    String topicName;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sentAuthInfoToKafka(User user) {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(user.getUsername());
        authRequest.setPassword(user.getPassword());
        Message<AuthRequest> message = MessageBuilder.withPayload(authRequest).setHeader(KafkaHeaders.TOPIC, topicName).build();
        kafkaTemplate.send(message);
    }
}
