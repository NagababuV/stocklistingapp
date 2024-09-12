package com.stack.authenticationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stack.authenticationservice.model.User;
import com.stack.authenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class KafkaConsumerService {


    private static Logger logger = Logger.getLogger(KafkaConsumerService.class.getName());

    private static final String topicName = "user-topic";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    public KafkaConsumerService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @KafkaListener(topics = topicName, groupId = "user-group")
    public void consume(String authInfo) {
        User userInfo;
        try {
            userInfo = objectMapper.readValue(authInfo, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfo.setUsername(userInfo.getUsername());
        userRepository.save(userInfo);
        logger.info(String.format("Message received -> %s", authInfo));
    }
}

