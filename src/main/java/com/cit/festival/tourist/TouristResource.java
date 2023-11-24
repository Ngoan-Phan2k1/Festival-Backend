package com.cit.festival.tourist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cit.festival.exception.NotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tourist")
@CrossOrigin("*")
public class TouristResource {
    
    private TouristService touristService;

    public TouristResource(TouristService touristService) {
        this.touristService = touristService;
    }

    @GetMapping
    public ResponseEntity<List<TouristDTO>> findAll() {
        List<TouristDTO> touristDTOs = touristService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(touristDTOs);
    }

    @GetMapping("username/{username}")
    public ResponseEntity<TouristDTO> findTouristByUserName(@PathVariable String username) {
        TouristDTO touristDTO = touristService.findByUserName(username);
        if (touristDTO != null){
            return ResponseEntity.status(HttpStatus.OK).body(touristDTO);
        }
        throw new NotFoundException("Không tìm thấy người dùng - " + username);
    }

    @GetMapping("{tourist_id}")
    public ResponseEntity<TouristDTO> findTouristById(@PathVariable int tourist_id) {
        TouristDTO touristDTO = touristService.findById(tourist_id);
        if (touristDTO  == null) {
            throw new NotFoundException("Không tìm thấy người dùng");
           
        }
        return ResponseEntity.status(HttpStatus.OK).body(touristDTO);
    }

    @PutMapping("/{tourist_id}")
    public ResponseEntity<TouristDTO> update(
        @PathVariable Integer tourist_id,
        @RequestBody @Valid TouristPut tourist,
        @RequestParam(value = "image_name", required = false) String image_name
    ) {
        
        TouristDTO touristDTO = touristService.update(tourist, tourist_id, image_name);
        return ResponseEntity.status(HttpStatus.OK).body(touristDTO);
    }

    @DeleteMapping("/{tourist_id}")
    public ResponseEntity<List<TouristDTO>> deleteById(
        @PathVariable Integer tourist_id
    ) {
        List<TouristDTO> touristDTOs = touristService.deleteById(tourist_id);
        
        return ResponseEntity.status(HttpStatus.OK).body(touristDTOs);
    }
}
