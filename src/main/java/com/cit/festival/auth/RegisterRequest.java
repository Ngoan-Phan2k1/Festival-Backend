package com.cit.festival.auth;

import com.cit.festival.role.RoleEnum;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "Name cannot be empty")
    private String fullname;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email invalid")
    private String email;
    
    @Column(unique = true)
    private String username;
    private String password;

    @NotEmpty(message = "Phone cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be a 10-digit number")
    private String phone;
    //private RoleEnum role;
    
}
