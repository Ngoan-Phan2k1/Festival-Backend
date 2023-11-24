package com.cit.festival.room;

import java.util.List;

import com.cit.festival.hotel.HotelDTO;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
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
public class RoomDTO {

    private Integer id;
    private Integer hotel_id;
    private String name;
    private Integer price;
    private List<String> services;
    private ImageDTO imageDTO;
    private HotelDTO hotelDTO;
    
}
