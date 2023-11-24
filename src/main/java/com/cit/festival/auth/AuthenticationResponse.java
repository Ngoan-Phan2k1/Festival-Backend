package com.cit.festival.auth;

import java.util.Date;

import com.cit.festival.role.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Integer touristId;
    private String username;
    private RoleEnum role;
    private String fullname;
    private String email;
    private Long tokenExpirationDate;
    
}
