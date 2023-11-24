package com.cit.festival.payment;

import java.time.LocalDateTime;

import com.cit.festival.booktour.BookedTour;
import com.cit.festival.booktour.BookedTourDTO;
import com.cit.festival.tour.TourDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Integer id;
    private Integer amount;
    private LocalDateTime dateOfCheckout; 
    private TourDTO tourDTO;
}
