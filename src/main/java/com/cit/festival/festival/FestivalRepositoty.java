package com.cit.festival.festival;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface FestivalRepositoty extends JpaRepository<Festival, Integer> {

    @Query("SELECT f FROM Festival f ORDER BY f.id")
    List<Festival> findAllByOrderById();
    // Optional<Festival> findByName(String name);

    // @Query("SELECT f FROM Festival f WHERE f.fromDate >= :fromDate AND f.toDate <= :toDate")
    // List<Festival> findFestivalsWithinDateRange(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    // @Query("SELECT f FROM Festival f WHERE LOWER(f.blogName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    // Optional<Festival> findByBlogNameContainingIgnoreCase(String searchTerm);

    @Query("SELECT f FROM Festival f WHERE LOWER(f.blogName) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', '%'), '%'))")
    Optional<Festival> findByBlogNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    


}
