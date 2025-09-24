package com.example.usermanagement.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreatedEvent {
    private String email;
    private String fullName;
    private String phoneNumber;
}
