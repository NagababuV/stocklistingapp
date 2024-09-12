package com.stack.userprofileservice.service;


import com.stack.userprofileservice.dto.UserRequest;
import com.stack.userprofileservice.model.User;
import com.stack.userprofileservice.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String saveUser(UserRequest userRequest) {
        User user = User.build(userRequest.getUsername(), userRequest.getPassword(), userRequest.getEmail(), userRequest.getPhone(), userRequest.getCountry());
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "User already exists";
        }
        userRepository.save(user);
        kafkaProducerService.sentAuthInfoToKafka(user);
        return "User saved";
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
