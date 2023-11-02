package com.cit.festival.hotel;

import java.util.List;

import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.room.Room;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
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
public class HotelDTO {
    
    private Integer id;
    private String name;
    private String location;
    private String introduce;
    private List<String> services;
    //private List<Room> rooms;
    private ImageDTO imageDTO;

}
