package com.cit.festival.FestivalContent;


import com.cit.festival.festival.Festival;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FestivalContent {

    @Id // Đánh dấu đây là ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id;

    //@NotNull(message = "Name cannot be null")
    private String name;

    @Column(length = 4000, nullable = true)
    private String content;

    @ManyToOne
    @JoinColumn(name = "festival_id", nullable = false)
    //@NotNull(message = "Festival cannot be null")
    private Festival festival;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", nullable = true)
    private Image image;
    
}
