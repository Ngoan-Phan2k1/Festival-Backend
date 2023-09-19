package com.cit.festival.tourist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cit.festival.exception.NotFoundException;

@RestController
@RequestMapping("/api/tourist")
public class TouristResource {
    
    @Autowired
    private TouristService touristService;

    @GetMapping
    public ResponseEntity<List<Tourist>> findAll() {
        List<Tourist> tourist = touristService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(tourist);
    }

    @GetMapping("{username}")
    public ResponseEntity<Tourist> findTouristByUserName(@PathVariable String username) {
        Tourist tourist = touristService.findByUserName(username);
        if (tourist != null){
            return ResponseEntity.status(HttpStatus.OK).body(tourist);
        }
        throw new NotFoundException("Username not found - " + username);
    }
}
