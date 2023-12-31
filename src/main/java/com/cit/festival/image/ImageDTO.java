package com.cit.festival.image;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ImageDTO {
    
    private Long id;
    private String name; 
    private String type;

    // @Lob
    // private byte[] imageData;
}
