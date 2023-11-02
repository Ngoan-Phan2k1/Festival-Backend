package com.cit.festival.booktour;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.cit.festival.festival.Festival;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.room.Room;
import com.cit.festival.tour.Tour;
import com.cit.festival.tourist.Tourist;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookedTour {
    
    @Id // Đánh dấu đây là ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // trường tăng tự động
    private Integer id;

    // @NotNull(message = "Booked cannot be empty")
    // private Integer booked;

    private Integer bookedAdult;

    private Integer bookedChild;
    private Integer bookedBaby;

    private String fullname;
    
    private String email;

    private String address;

    private String note;

    private Integer num_room;

    @NotEmpty(message = "Phone cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @Builder.Default
    @NotNull(message = "IsCheckout cannot be empty")
    private Boolean isCheckout = false;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false) // Tên cột khoá ngoại
    @NotNull(message = "Tour cannot be null")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "tourist_id", nullable = false) // Tên cột khoá ngoại
    @NotNull(message = "Tourist cannot be null")
    private Tourist tourist;

    @CreationTimestamp
    private LocalDateTime dateOfBooking; // Thêm trường này
}
