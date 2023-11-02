package com.cit.festival.auth;

import java.util.Date;

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
    private String fullname;
    private String email;
    private Long tokenExpirationDate;
    
}
