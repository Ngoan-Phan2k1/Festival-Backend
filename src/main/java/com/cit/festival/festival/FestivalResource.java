package com.cit.festival.festival;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cit.festival.exception.NotFoundException;
import com.cit.festival.tour.Tour;
import com.cit.festival.tour.TourDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/festival")
@CrossOrigin("*")
public class FestivalResource {

    // @Autowired
    private FestivalService festivalService;
    public FestivalResource(FestivalService festivalService) {
        this.festivalService = festivalService;
    }
    
    @PostMapping
    public ResponseEntity<FestivalDTO> add(
        @RequestBody @Valid Festival festival,
        @RequestParam(value = "imageName", required = false) String imageName
    ) {

        FestivalDTO festivalDTO = festivalService.add(festival, imageName);
        return ResponseEntity.status(HttpStatus.OK).body(festivalDTO);
    }

    @GetMapping("{festivalId}")
    public ResponseEntity<FestivalDTO> findById(@PathVariable Integer festivalId) {
        FestivalDTO festivalDTO = festivalService.findById(festivalId);
        return ResponseEntity.status(HttpStatus.OK).body(festivalDTO);
    }

    @GetMapping("searchName")
    public ResponseEntity<List<FestivalDTO>> findBySearchName(
        @RequestParam(value = "search", required = false) List<String> searchName
    ) {
        List<FestivalDTO> festivalDTOs = festivalService.findBySearchName(searchName);
        return ResponseEntity.status(HttpStatus.OK).body(festivalDTOs);
    }

    @GetMapping
    public ResponseEntity<List<FestivalDTO>> findAll() {
        List<FestivalDTO> festivalDTOs = festivalService.findFestivals();
        return ResponseEntity.status(HttpStatus.OK).body(festivalDTOs);
    }

    @PutMapping("/{festivalId}")
    public ResponseEntity<FestivalDTO> update(
        @PathVariable Integer festivalId,
        @RequestBody @Valid Festival festival,
        @RequestParam(value = "imageName", required = false) String imageName
    ) {

        FestivalDTO festivalDTO = festivalService.update(festival, festivalId, imageName);
        return ResponseEntity.status(HttpStatus.OK).body(festivalDTO);
    }

    @DeleteMapping("/{festivalId}")
    public ResponseEntity<List<FestivalDTO>> deleteById(@PathVariable Integer festivalId) {
        List<FestivalDTO> festivalDTOs = festivalService.deleteById(festivalId);
        return ResponseEntity.status(HttpStatus.OK).body(festivalDTOs);
    }
    
}
