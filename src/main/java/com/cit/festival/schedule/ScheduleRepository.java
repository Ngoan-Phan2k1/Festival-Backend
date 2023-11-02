package com.cit.festival.schedule;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cit.festival.room.Room;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    
     List<Schedule> findByTourId(Integer tour_id);
     List<Schedule> findByTourIdOrderByDayAsc(Integer tourId);
     boolean existsByTourIdAndDay(Integer tourId, Integer day);
}
