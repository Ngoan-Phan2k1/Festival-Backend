package com.cit.festival.tourist;

import com.cit.festival.image.ImageDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TouristPut {
    
    @NotEmpty(message = "Name cannot be empty")
    private String fullname;

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email invalid")
    private String email;
}
