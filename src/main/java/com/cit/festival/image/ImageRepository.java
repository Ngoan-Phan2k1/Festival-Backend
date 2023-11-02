package com.cit.festival.image;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cit.festival.hotel.Hotel;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    
    Optional<Image> findByName(String fileName);

    // @Query("SELECT i FROM Image i WHERE i.hotel.id = :hotelId")
    // List<Image> findAllByHotelId(@Param("hotelId") Integer hotelId);
    
}   
