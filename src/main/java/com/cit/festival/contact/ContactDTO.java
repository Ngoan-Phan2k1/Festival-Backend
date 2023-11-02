package com.cit.festival.contact;

import com.cit.festival.room.Room;
import com.cit.festival.room.RoomDTO;
import com.cit.festival.tourist.Tourist;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    private Integer id;
    private Integer tourist_id;
    private String fullname;
    private String email;
    private String phone;
    private RoomDTO roomDTO;

}
