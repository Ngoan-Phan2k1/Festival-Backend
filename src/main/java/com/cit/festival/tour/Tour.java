package com.cit.festival.tour;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.cit.festival.festival.Festival;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.image.Image;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // Tạo bảng trong CSDL
public class Tour {
    
    @Id // Đánh dấu đây là ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // trường tăng tự động
    private Integer id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "From cannot be empty")
    private String fromWhere;

    @NotEmpty(message = "To cannot be empty")
    private String toWhere;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "fromDate cannot be null")
    private LocalDate fromDate; // Ngày khởi hành

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "toDate cannot be null")
    private LocalDate toDate;   // Ngày về

    @NotNull(message = "Price Adult cannot be empty")
    private Integer priceAdult;

    @NotNull(message = "Price Child cannot be empty")
    private Integer priceChild;

    @NotNull(message = "Price Baby cannot be empty")
    private Integer priceBaby;

    @NotNull(message = "Capacity cannot be empty")
    private Integer capacity;
    
    @NotNull(message = "Booked cannot be empty")
    private Integer booked;

    @Builder.Default
    private Boolean active = true;


    // @ManyToOne
    // @JoinColumn(name = "festival_id", nullable = false) // Tên cột khoá ngoại
    // @NotNull(message = "Festival cannot be null")
    // private Festival festival;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", nullable = true)
    private Image image;

    // @ManyToMany
    // @JoinTable(
    //     name = "tour_hotel",
    //     joinColumns = @JoinColumn(name="tour_id"),
    //     inverseJoinColumns = @JoinColumn(name="hotel_id")
    // )
    // private List<Hotel> hotels = new ArrayList<>();


    @OneToOne
    @JoinColumn(name = "hotel_id", nullable = true)
    private Hotel hotel;

    @OneToOne
    @JoinColumn(name = "festival_id", nullable = true)
    private Festival festival;

    @Column(name = "is_deleted", nullable = true)
    private boolean isDeleted;
    
    // @Builder.Default
    // private boolean isFromBeforeTo = true;

    @AssertTrue(message = "fromDate must be before toDate")
    private boolean isFromBeforeTo() {
        // Kiểm tra nếu fromDate không lớn hơn toDate thì trả về true, ngược lại trả về false
        return fromDate == null || toDate == null || fromDate.isBefore(toDate);
    }
}
