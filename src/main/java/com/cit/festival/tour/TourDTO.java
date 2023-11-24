package com.cit.festival.tour;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.cit.festival.festival.Festival;
import com.cit.festival.hotel.HotelDTO;
import com.cit.festival.image.ImageDTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDTO {

    private Integer id;
    private Integer festival_id;
    private String name;
    private String fromWhere;
    private String toWhere;
    private String description;
    private LocalDate fromDate; // Ngày khởi hành
    private LocalDate toDate;   // Ngày về
    private Integer priceAdult;
    private Integer priceChild;
    private Integer priceBaby;
    private Integer capacity;
    private Integer booked;
    private boolean active;
    private ImageDTO imageDTO;
    List<HotelDTO> hotelDTOs;

    @AssertTrue(message = "fromDate must be before toDate")
    private boolean isFromBeforeTo() {
        // Kiểm tra nếu fromDate không lớn hơn toDate thì trả về true, ngược lại trả về false
        return fromDate == null || toDate == null || fromDate.isBefore(toDate);
    }
}
