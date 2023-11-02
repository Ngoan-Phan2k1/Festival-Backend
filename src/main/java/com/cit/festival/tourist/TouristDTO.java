package com.cit.festival.tourist;

import com.cit.festival.image.ImageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TouristDTO {
    
    private Integer id;
    private String fullname;
    private String username;
    private String email;
    //private String phone;
    private ImageDTO imageDTO;

}
