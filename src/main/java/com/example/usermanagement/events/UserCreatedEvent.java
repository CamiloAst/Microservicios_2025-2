package com.example.usermanagement.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreatedEvent {
    private String username;
    private String email;
    private String phoneNumber;
    private String timestamp;
}
