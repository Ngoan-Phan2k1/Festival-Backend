package com.cit.festival.schedule;

import com.cit.festival.hotel.Hotel;
import com.cit.festival.image.Image;
import com.cit.festival.tour.Tour;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Schedule {
    
    @Id // Đánh dấu đây là ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // trường tăng tự động
    private Integer id;

    @NotNull(message = "Day cannot be null")
    private Integer day;

    //@NotNull(message = "moring cannot be null")
    @Column(length =2000, nullable = true)
    private String morning;

    //@NotNull(message = "evening cannot be null")
    @Column(length = 2000, nullable = true)
    private String evening;

    //@NotNull(message = "night cannot be null")
    @Column(length = 2000, nullable = true)
    private String night;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false) // Tên cột khoá ngoại
    @NotNull(message = "Tour cannot be null")
    private Tour tour;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", nullable = true)
    private Image image;
}
