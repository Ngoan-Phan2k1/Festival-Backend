package com.cit.festival.tour;

import java.sql.Date;
import java.time.LocalDate;
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

import com.cit.festival.booktour.BookedTourDTO;
import com.cit.festival.exception.NotFoundException;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tour")
@CrossOrigin("*")
public class TourResource {
    
    @Autowired
    private TourService tourService;

    public TourResource(TourService tourService) {
        this.tourService = tourService;
    }

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

    @GetMapping("/search")
    public ResponseEntity<List<TourDTO>> findByCondition(
        @RequestParam(value = "toWhere", required = false) String toWhere,
        @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
        @RequestParam(value = "toDate", required = false) LocalDate toDate,
        @RequestParam(value = "priceAdult", required = false) Integer priceAdult
    ) {

        List<TourDTO> tourDTOs = tourService.findByCondition(toWhere, fromDate, toDate, priceAdult);
        return ResponseEntity.status(HttpStatus.OK).body(tourDTOs);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TourDTO>> findAllActive() {

        List<TourDTO> tourDTOs = tourService.findToursActive();
        return ResponseEntity.status(HttpStatus.OK).body(tourDTOs);
    }

    @GetMapping("{tourId}")
    public ResponseEntity<TourDTO> findById(@PathVariable Integer tourId) {

        Optional<TourDTO> tourDTO = tourService.findById(tourId);

        if (!tourDTO.isPresent()) {
            throw new NotFoundException("Không tìm thấy tour");
           
        }
        return ResponseEntity.status(HttpStatus.OK).body(tourDTO.get());
    }

    @GetMapping("festival/{festivalId}")
    public ResponseEntity<TourDTO> findByFestivalId(@PathVariable Integer festivalId) {

        Optional<TourDTO> tourDTO = tourService.findByFestivalId(festivalId);

        if (!tourDTO.isPresent()) {
            //throw new NotFoundException("Không tìm thấy tour");
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tourDTO.get());
    }

    @GetMapping("hotel/{hotelId}")
    public ResponseEntity<TourDTO> findByHotelId(@PathVariable Integer hotelId) {

        Optional<TourDTO> tourDTO = tourService.findByHotelId(hotelId);

        if (!tourDTO.isPresent()) {
            //throw new NotFoundException("Không tìm thấy tour");
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tourDTO.get());
    }

    @PatchMapping("/{tourId}/{booked}")
    public ResponseEntity<TourDTO> updateTourBooked(
        @PathVariable Integer tourId,
        @PathVariable Integer booked
    ) {
        TourDTO updatedTour = tourService.updateTourBooked(tourId, booked);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTour);
    }

    @PatchMapping("/{tour_id}")
    public ResponseEntity<TourDTO> updateTourActive(
        @PathVariable Integer tour_id,
        @RequestParam(value = "active", required = true) boolean active
    ) {
        TourDTO updatedTour = tourService.updateTourActive(tour_id, active);
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
    public ResponseEntity<List<TourDTO>> deleteById(
        @PathVariable Integer tour_id
    ) {
        List<TourDTO> tourDTOs = tourService.deleteById(tour_id);
        
        //return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.OK).body(tourDTOs);
    }
}
