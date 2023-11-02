package com.cit.festival.booktour;

import java.time.LocalDateTime;
import java.util.List;

import com.cit.festival.hotel.HotelDTO;
import com.cit.festival.room.RoomDTO;
import com.cit.festival.tour.TourDTO;

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
public class BookedTourDTO {

    private Integer booked_id;
    private Integer bookedAdult;
    private Integer bookedChild;
    private Integer bookedBaby;

    private String fullname;
    private String email;
    private String address;
    private String note;
    private Integer num_room;
    private String phone;
    
    private boolean isCheckout;
    private LocalDateTime dateOfBooking;
    private TourDTO tourDto;
    private RoomDTO roomDtO;
}
