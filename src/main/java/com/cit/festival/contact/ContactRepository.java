package com.cit.festival.contact;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cit.festival.booktour.BookedTour;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    
    List<Contact> findAllByTouristId(Integer touristId);
    Optional<Contact> findAllByRoomIdAndTouristId(Integer roomId, Integer touristId);
}
