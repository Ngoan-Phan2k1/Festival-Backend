package com.cit.festival.tour;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cit.festival.exception.NotFoundException;
import com.cit.festival.festival.Festival;
import com.cit.festival.schedule.Schedule;
import com.cit.festival.schedule.ScheduleDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tour")
@CrossOrigin("*")
public class TourResource {
    
    @Autowired
    private TourService tourService;

    @PostMapping
    public ResponseEntity<TourDTO> add(
        @RequestBody @Valid Tour tour,
        @RequestParam(value = "imageName", required = false) String imageName
    ) {

        TourDTO tourDTO = tourService.add(tour, imageName);
        return ResponseEntity.status(HttpStatus.OK).body(tourDTO);
    }

    @PostMapping("/test")
    public ResponseEntity<Tour> addTour(
        @RequestBody @Valid Tour tour,
        @RequestParam(value = "imageName", required = false) String imageName
        //@RequestParam(value = "hotel_id",required = true) Integer hotel_id
    ) {

        Tour tourDB = tourService.addTour(tour, imageName);
        return ResponseEntity.status(HttpStatus.OK).body(tourDB);
    }

    @GetMapping
    public ResponseEntity<List<TourDTO>> findAll() {

        List<TourDTO> tourDTOs = tourService.findTours();
        return ResponseEntity.status(HttpStatus.OK).body(tourDTOs);
    }

    @GetMapping("{tourId}")
    public ResponseEntity<TourDTO> findById(@PathVariable Integer tourId) {

        TourDTO tourDTO = tourService.findById(tourId);

        if (tourDTO == null) {
            throw new NotFoundException("Không tìm thấy tour");
           
        }
        return ResponseEntity.status(HttpStatus.OK).body(tourDTO);
    }

    @PatchMapping("/{tourId}/{booked}")
    public ResponseEntity<TourDTO> updateTourBooked(
        @PathVariable Integer tourId,
        @PathVariable Integer booked
    ) {
        TourDTO updatedTour = tourService.updateTourBooked(tourId, booked);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTour);
    }

    @PutMapping("/{tour_id}")
    public ResponseEntity<TourDTO> update(
        @PathVariable Integer tour_id,
        @RequestBody @Valid Tour tour,
        @RequestParam(value = "image_name", required = false) String image_name
    ) {

        TourDTO tourDTO = tourService.update(tour, tour_id, image_name);
        return ResponseEntity.status(HttpStatus.OK).body(tourDTO);
    }

    @DeleteMapping("/{tour_id}")
    public ResponseEntity<?> delete(
        @PathVariable Integer tour_id
    ) {
        tourService.delete(tour_id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
