package com.cit.festival.booktour;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cit.festival.festival.Festival;
import com.cit.festival.tour.Tour;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/user/booked")
@CrossOrigin(origins = "http://localhost:4200")
public class BookedTourResource {
    
    @Autowired
    private BookedTourService bookedTourService;

    @GetMapping
    public ResponseEntity<List<BookedTourDTO>> findAll() {

        List<BookedTourDTO> bookedTours = bookedTourService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(bookedTours);
    }


    @GetMapping("tourist/{touristId}")
    public ResponseEntity<List<BookedTourDTO>> findAllByTouristId(@PathVariable Integer touristId) {

        List<BookedTourDTO> bookedToursDTO = bookedTourService.findAllByTouristId(touristId);
        return ResponseEntity.status(HttpStatus.OK).body(bookedToursDTO);
    }

    @PostMapping
    public ResponseEntity<BookedTourDTO> add(@RequestBody @Valid BookedTour bookedTour) {

        BookedTourDTO bookedTourDB = bookedTourService.add(bookedTour);
        return ResponseEntity.status(HttpStatus.OK).body(bookedTourDB);
    }

    @PatchMapping("/{bookedtourId}")
    public ResponseEntity<BookedTourDTO> updateBookedTourIsCheckout(
        @PathVariable Integer bookedtourId,
        @RequestParam(value = "isCheckout") Boolean isCheckout
    ) {
        BookedTourDTO updatedBookedTour = bookedTourService.updateBookedTourCheckout(bookedtourId, isCheckout);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBookedTour);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<BookedTourDTO>> deleteById(
        @PathVariable Integer id,
        @RequestParam(value = "touristId") Integer touristId
    ) {
        List<BookedTourDTO> bookedToursDTO = bookedTourService.deleteById(id, touristId);
        
        //return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.OK).body(bookedToursDTO);
    }

}
