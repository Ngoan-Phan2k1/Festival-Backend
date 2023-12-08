package com.cit.festival.FestivalContent;

import java.time.LocalDateTime;

import com.cit.festival.image.ImageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FestivalContentDTO {

    private Integer id;
    private Integer festival_id;
    private String name;
    private String content;
    private ImageDTO imageDTO;
    
}
