package com.cit.festival.tourist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristRepository extends JpaRepository<Tourist, Integer> {
    
    @Query("SELECT t FROM Tourist t JOIN t.user u WHERE u.username = :username")
    Tourist findTouristByUserName(@Param("username") String username);

}
