package com.example.usermanagement.dto;

import com.example.usermanagement.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;

}
