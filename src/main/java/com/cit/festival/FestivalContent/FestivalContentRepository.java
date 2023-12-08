package com.cit.festival.FestivalContent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface FestivalContentRepository extends JpaRepository<FestivalContent, Integer> {

    @Query("SELECT fc FROM FestivalContent fc WHERE fc.festival.id = :festivalId ORDER BY fc.id")
    List<FestivalContent> findByFestivalIdAndOrderByContentId(@Param("festivalId") Integer festivalId);
    
}
