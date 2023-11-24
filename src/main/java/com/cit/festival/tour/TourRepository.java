package com.cit.festival.tour;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
    
    List<Tour> findAllByOrderById();
    boolean existsById(Integer id);
    List<Tour> findAllByActiveIsTrueOrderById();
}
