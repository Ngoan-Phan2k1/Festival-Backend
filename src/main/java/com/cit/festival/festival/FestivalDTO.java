package com.cit.festival.festival;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cit.festival.FestivalContent.FestivalContentDTO;
import com.cit.festival.image.ImageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FestivalDTO {
    
    private Integer id;
    private String blogName;
    private LocalDate dateOfPost;
    private ImageDTO imageDTO;
    private List<FestivalContentDTO> festivalContentDTOs = new ArrayList<>();
}
