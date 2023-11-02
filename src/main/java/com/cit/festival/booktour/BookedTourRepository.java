package com.cit.festival.booktour;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookedTourRepository extends JpaRepository<BookedTour, Integer> {
    
    Optional<BookedTour> findByTourIdAndTouristId(Integer tourId, Integer touristId);
    List<BookedTour> findAllByTouristId(Integer touristId);
}

