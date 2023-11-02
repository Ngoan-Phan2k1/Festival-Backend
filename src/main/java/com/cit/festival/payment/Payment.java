package com.cit.festival.payment;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import com.cit.festival.booktour.BookedTour;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Payment {

    @Id // Đánh dấu đây là ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // trường tăng tự động
    private Integer id;

    @NonNull
    @NotNull(message = "Amount cannot be null")
    private Integer amount;

    @NonNull
    @NotNull(message = " cannot be null")
    private String vnp_TxnRef;

    @CreationTimestamp
    private LocalDateTime dateOfCheckout; 

    @NonNull
    @OneToOne
    @JoinColumn(name = "bookedtour_id")
    @NotNull(message = "Booked cannot be null")
    private BookedTour bookedTour;

}
