package com.cit.festival.hotel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cit.festival.festival.Festival;
import com.cit.festival.room.Room;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer>  {
    
    Boolean existsByName(String name);
    Boolean existsByNameAndIsDeletedFalse(String name);
    Optional<Hotel> findByName(String name);
    Optional<Hotel> findByNameAndIsDeletedFalse(String name);
    List<Hotel> findAllByIsDeletedFalseOrderById();
    @Query("SELECT h FROM Hotel h WHERE h.id = :id AND h.isDeleted = false")
    Optional<Hotel> findById(@Param("id") Integer id);

    

    // @Query("SELECT h FROM Hotel h " +
    //        "WHERE (:paramName IS NULL OR h.name LIKE %:paramName%) " +
    //        "AND (:paramIntroduce IS NULL OR h.introduce LIKE %:paramIntroduce%) " +
    //        "AND (:paramServices IS NULL OR h.services IN :paramServices)")
    // List<Hotel> findHotelsByCriteria(
    //     @Param("paramName") String paramName,
    //     @Param("paramIntroduce") String paramIntroduce,
    //     @Param("paramServices") List<String> paramServices
    // );

    // @Query("SELECT h FROM Hotel h " +
    //    "WHERE (:paramName IS NULL OR h.name LIKE %:paramName%) " +
    //    "AND (:paramIntroduce IS NULL OR h.introduce LIKE %:paramIntroduce%) " +
    //    "AND (:paramServices IS NULL OR :paramServices MEMBER OF h.services)")
    // List<Hotel> findHotelsByCriteria(
    //     @Param("paramName") String paramName,
    //     @Param("paramIntroduce") String paramIntroduce,
    //     @Param("paramServices") List<String> paramServices
    // );

    // @Query("SELECT h FROM Hotel h " +
    //    "WHERE (:paramName IS NULL OR h.name LIKE %:paramName%)")
    
    // @Query("SELECT h FROM Hotel h " +
    //    "WHERE h.name = :paramName")
    // List<Hotel> findHotelsByCriteria(
    //     @Param("paramName") String paramName
    // );

}
