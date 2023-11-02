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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cit.festival.exception.NotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/festival")
@CrossOrigin("*")
public class FestivalResource {

    @Autowired
    private FestivalService festivalService;
    
    @PostMapping
    public ResponseEntity<Festival> add(@RequestBody @Valid Festival festival) {

        Festival dbFestival = festivalService.add(festival);
        return ResponseEntity.status(HttpStatus.OK).body(dbFestival);
    }

    @GetMapping("{festivalId}")
    public ResponseEntity<Festival> findById(@PathVariable Integer festivalId) {

        Optional<Festival> dbFestival = festivalService.findById(festivalId);

        if (dbFestival.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(dbFestival.get());
        }
        throw new NotFoundException("Festival id is not found - " + festivalId);
    }

    @GetMapping("date-range")
    public ResponseEntity<List<Festival>> findByDateRange(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate
    ) {
        List<Festival> festivals = festivalService.findByDateRange(fromDate, toDate);
        return ResponseEntity.status(HttpStatus.OK).body(festivals);
    }
    
}
