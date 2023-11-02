package com.cit.festival.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cit.festival.room.RoomDTO;
import com.cit.festival.tour.Tour;
import com.cit.festival.tour.TourDTO;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/schedule")
@CrossOrigin("*")
public class ScheduleResource {

    @Autowired
    private ScheduleService scheduleService;
    
    @PostMapping
    public ResponseEntity<ScheduleDTO> add(
        @RequestBody @Valid Schedule schedule,
        @RequestParam(value = "imageName", required = false) String imageName
    ) {

        ScheduleDTO scheduleDTO = scheduleService.add(schedule, imageName);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleDTO);
    }

    @GetMapping("/{tour_id}")
    public ResponseEntity<List<ScheduleDTO>> findByTourId(
        @PathVariable Integer tour_id
    ) {
        List<ScheduleDTO> scheduleDTOs = scheduleService.findByTourId(tour_id);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleDTOs);
    }

    @GetMapping("/scheduleId/{id}")
    public ResponseEntity<ScheduleDTO> findById(@PathVariable Integer id) {
        ScheduleDTO scheduleDTO = scheduleService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleDTO);
    }

    @PutMapping("/{schedule_id}")
    public ResponseEntity<ScheduleDTO> upDateSchedule(
        @PathVariable Integer schedule_id,
        @RequestBody @Valid Schedule schedule,
        @RequestParam(value = "image_name", required = false) String image_name
    ) {

        ScheduleDTO scheduleDTO = scheduleService.update(schedule, schedule_id, image_name);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleDTO);
    }
}
