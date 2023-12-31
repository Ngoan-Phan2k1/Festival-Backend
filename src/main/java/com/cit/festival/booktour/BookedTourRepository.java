package com.cit.festival.booktour;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BookedTourRepository extends JpaRepository<BookedTour, Integer> {
    
    //Optional<BookedTour> findByTourIdAndTouristId(Integer tourId, Integer touristId);

    List<BookedTour> findAllByIsDeletedFalse();

    Optional<BookedTour> findByTourIdAndTouristIdAndIsDeletedFalse(Integer tourId, Integer touristId);


    //List<BookedTour> findAllByTouristId(Integer touristId);

    List<BookedTour> findAllByTouristIdAndIsDeletedFalse(Integer touristId);

    @Query("SELECT b FROM BookedTour b WHERE b.id = :id AND b.isDeleted = false")
    Optional<BookedTour> findById(@Param("id") Integer id);
}

