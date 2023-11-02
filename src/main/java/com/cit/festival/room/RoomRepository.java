package com.cit.festival.room;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cit.festival.payment.Payment;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    
    Boolean existsByName(String name);
    List<Room> findByHotelId(Integer hotel_id);

    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId ORDER BY r.id ASC")
    List<Room> findRoomsByHotelIdOrderByidAsc(@Param("hotelId") Integer hotelId);
}
