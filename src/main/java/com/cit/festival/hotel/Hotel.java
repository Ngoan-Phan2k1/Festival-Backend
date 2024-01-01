package com.cit.festival.hotel;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ManyToAny;

import com.cit.festival.image.Image;
import com.cit.festival.room.Room;
import com.cit.festival.tour.Tour;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
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
public class Hotel {

    @Id // Đánh dấu đây là ID /////////sddddddddddddddddddddddddddddddddddddddddadsadsadsaddddddddddd
    @GeneratedValue(strategy = GenerationType.IDENTITY) // trường tăng tự động
    private Integer id;

    @NotNull(message = "Name cannot be null")
    @Column(unique = true)
    private String name;
    
    @NotNull(message = "Location cannot be null")
    private String location;

    @NotNull(message = "Introduce cannot be null")
    @Column(length = 1000, nullable = false)
    private String introduce;

    @Column(name = "is_deleted", nullable = true)
    private boolean isDeleted;

    // @NotNull(message = "Services cannot be null")
    // private List<String> services;

    // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "hotel_id")
    // @NotNull(message = "rooms cannot be null")
    // private List<Room> rooms;

    // @JsonIdentityInfo(
    //     generator = ObjectIdGenerators.PropertyGenerator.class,
    //     property = "id")


    // @JsonIgnore
    // @ManyToMany(mappedBy = "hotels")
    // private List<Tour> tours = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", nullable = true)
    //@NotNull(message = "rooms cannot be null")
    private Image image;

    // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "hotel_id")
    // @NotNull(message = "images cannot be null")
    // private List<Image> images;

}   
