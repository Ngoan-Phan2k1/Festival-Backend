package com.cit.festival.hotel;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import com.cit.festival.exception.HotelException;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.schedule.Schedule;
import com.cit.festival.tour.Tour;
import com.cit.festival.tour.TourDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hotel")
@CrossOrigin("*")
public class HotelResource {
    
    private HotelService hotelService;
    private HotelRepository hotelRepository;

    public HotelResource(
        HotelService hotelService,
        HotelRepository hotelRepository
    ) {
        this.hotelService = hotelService;
        this.hotelRepository = hotelRepository;
    }

    @GetMapping
    public ResponseEntity<List<HotelDTO>> findAll() {

        List<HotelDTO> hotelDTOs = hotelService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(hotelDTOs);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> findById(
        @PathVariable Integer hotelId
    ) {

        HotelDTO hotelDTO = hotelService.findById(hotelId);
        if (hotelDTO == null) {
            throw new NotFoundException("Không tìm thấy khách sạn");
        }
        return ResponseEntity.status(HttpStatus.OK).body(hotelDTO);
    }

    @GetMapping("/test/{hotelId}")
    public ResponseEntity<Hotel> findByHotelId(
        @PathVariable Integer hotelId
    ) {

        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
        if (!hotel.isPresent()) {
            throw new NotFoundException("Không tìm thấy khách sạn");
        }
        return ResponseEntity.status(HttpStatus.OK).body(hotel.get());
    }

    // @GetMapping("/criteria")
    // public ResponseEntity<List<Hotel>> findByCriteria(
    //     @RequestParam(value = "name", required = false) String name,
    //     @RequestParam(value = "introduce", required = false) String introduce,
    //     @RequestParam(value = "services", required = false) List<String> services
    // ) {
    //     //System.out.println("Service: " + services);
    //     List<Hotel> hotels = hotelService.findHotelsByCriteria(name);
    //     return ResponseEntity.status(HttpStatus.OK).body(hotels);

    //     //return ResponseEntity.status(HttpStatus.OK).build();
    // }

    @GetMapping("/criteria")
    public ResponseEntity<Hotel> findByName(
        @RequestParam(value = "name") String name
    ) {

        Optional<Hotel> hotel = hotelService.findByName(name);

        if (hotel.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(hotel.get());
        }
        throw new NotFoundException("Không tìm thấy khách sạn");
    }

    @PostMapping
    public ResponseEntity<HotelDTO> add(
        @RequestBody @Valid Hotel hotel,
        @RequestParam(value = "imageName", required = false) String imageName
    ) {

        HotelDTO hotelDB = hotelService.add(hotel, imageName);
        return ResponseEntity.status(HttpStatus.OK).body(hotelDB);
    }

    @DeleteMapping("{hotelId}")
    public ResponseEntity<List<Hotel>> deleteById(@PathVariable Integer hotelId) {

        List<Hotel> hotels = hotelService.deleteById(hotelId);
        return ResponseEntity.status(HttpStatus.OK).body(hotels);
    }

    @PutMapping("/{hotel_id}")
    public ResponseEntity<HotelDTO> upDateSchedule(
        @PathVariable Integer hotel_id,
        @RequestBody @Valid Hotel hotel,
        @RequestParam(value = "image_name", required = false) String image_name
    ) {
        HotelDTO hotelDTO = hotelService.update(hotel, hotel_id, image_name);
        return ResponseEntity.status(HttpStatus.OK).body(hotelDTO);
    }

}
