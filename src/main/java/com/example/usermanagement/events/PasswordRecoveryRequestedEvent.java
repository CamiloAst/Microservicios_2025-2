package com.example.usermanagement.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRecoveryRequestedEvent {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String timestamp;
}
