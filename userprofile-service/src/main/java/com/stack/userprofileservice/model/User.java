package com.stack.userprofileservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.kafka.annotation.KafkaListener;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Document(collection = "registered")
public class User {
    @Id
    private String username;
    private String password;
    private String email;
    private String phone;
    private String country;
}
