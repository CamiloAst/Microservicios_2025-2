package com.example.usermanagement.events;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordChangedEvent {
    private String email;
    private Instant changedAt;
}
