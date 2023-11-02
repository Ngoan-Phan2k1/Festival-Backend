package com.cit.festival.schedule;

import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleDTO {
    
    private Integer id;
    private Integer tour_id;
    private Integer day;
    private String morning;
    private String evening;
    private String night;
    private ImageDTO imageDTO;

}
