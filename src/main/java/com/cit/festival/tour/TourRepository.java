package com.cit.festival.tour;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
    
    List<Tour> findAllByOrderById();
    boolean existsById(Integer id);
    List<Tour> findAllByActiveIsTrueOrderById();

    List<Tour> findByToWhereAndFromDateGreaterThanEqualAndToDateLessThanEqualAndPriceAdultLessThanEqual(
        String toWhere, LocalDate fromDate, LocalDate toDate, Integer priceAdult
    );

    Optional<Tour> findByName(String name);

    // List<Tour> findByToWhereAndFromDateLessThanEqualAndToDateGreaterThanEqualAndPriceAdultLessThanEqual(
    //     String toWhere, LocalDate fromDate, LocalDate toDate, Integer priceAdult
    // );


    // @Query("SELECT t FROM Tour t WHERE (:toWhere IS NULL OR t.toWhere = :toWhere) " +
    //        "AND (:fromDate IS NULL OR t.fromDate >= :fromDate) " +
    //        "AND (:toDate IS NULL OR t.toDate <= :toDate) " +
    //        "AND (:priceAdult IS NULL OR t.priceAdult <= :priceAdult)")
    // List<Tour> findToursByConditions(
    //     @Param("toWhere") String toWhere,
    //     @Param("fromDate") LocalDate fromDate,
    //     @Param("toDate") LocalDate toDate,
    //     @Param("priceAdult") Integer priceAdult
    // );




    @Query("SELECT t FROM Tour t WHERE (:toWhere IS NULL OR t.toWhere = :toWhere) " +
       "AND (:fromDate IS NULL OR t.fromDate >= CAST(:fromDate AS java.sql.Date)) " +
       "AND (:toDate IS NULL OR t.toDate <= :toDate) " +
       "AND (:priceAdult IS NULL OR t.priceAdult <= :priceAdult)")
    List<Tour> findToursByConditions(
        @Param("toWhere") String toWhere,
        @Param("fromDate") LocalDate fromDate,
        @Param("toDate") LocalDate toDate,
        @Param("priceAdult") Integer priceAdult
    );

    // @Query("SELECT t FROM Tour t WHERE (:toWhere IS NULL OR t.toWhere = :toWhere) " +
    //    "AND (:fromDate IS NULL OR t.fromDate >= CAST(:fromDate AS java.sql.Date)) " +
    //    "AND (:toDate IS NULL OR t.toDate <= CAST(:toDate AS java.sql.Date)) " +
    //    "AND (:priceAdult IS NULL OR t.priceAdult <= :priceAdult)")
    // List<Tour> findToursByConditions(
    //     @Param("toWhere") String toWhere,
    //     @Param("fromDate") LocalDate fromDate,
    //     @Param("toDate") LocalDate toDate,
    //     @Param("priceAdult") Integer priceAdult
    // );




}
