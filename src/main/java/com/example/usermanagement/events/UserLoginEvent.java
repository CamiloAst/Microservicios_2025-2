package com.example.usermanagement.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginEvent {
    private String username;
    private String email;
    private String timestamp;
}