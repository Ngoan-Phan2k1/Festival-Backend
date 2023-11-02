package com.cit.festival.room;

import java.util.ArrayList;
import java.util.List;

import com.cit.festival.booktour.BookedTour;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.image.Image;
import com.cit.festival.tour.Tour;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {

    @Id // Đánh dấu đây là ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // trường tăng tự động
    private Integer id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Price cannot be empty")
    private Integer price;

    @NotNull(message = "Services cannot be null")
    private List<String> services;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", nullable = true)
    private Image image;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false) // Tên cột khoá ngoại
    @NotNull(message = "Hotel cannot be null")
    private Hotel hotel;
    
}
