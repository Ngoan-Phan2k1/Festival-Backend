package com.cit.festival.room;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.cit.festival.exception.NotFoundException;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/room")
@CrossOrigin("*")
public class RoomResource {
    
    @Autowired
    private RoomService roomService;

   public RoomResource(RoomService roomService) {
    this.roomService = roomService;
   }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> findAll() {
        List<RoomDTO> roomDTOs = roomService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(roomDTOs);
    }

    @GetMapping("find_room/{room_id}")
    public ResponseEntity<RoomDTO> findById(
        @PathVariable Integer room_id
    ) {
        RoomDTO roomDTO = roomService.findById(room_id);
        return ResponseEntity.status(HttpStatus.OK).body(roomDTO);
    }

    @GetMapping("/{hotel_id}")
    public ResponseEntity<List<RoomDTO>> findByHotelId(
        @PathVariable Integer hotel_id
    ) {
        List<RoomDTO> roomDTOs = roomService.findByHotelId(hotel_id);
        return ResponseEntity.status(HttpStatus.OK).body(roomDTOs);
    }

    @PostMapping
    public ResponseEntity<RoomDTO> add(
        @RequestBody @Valid Room room,
        @RequestParam(value = "imageName", required = false) String imageName
        //@RequestParam(value = "hotel_id", required = true) Integer hotel_id
        ) {
    
        RoomDTO roomDTO = roomService.add(room, imageName);
        return ResponseEntity.status(HttpStatus.OK).body(roomDTO);
    }

    @PutMapping("/{room_id}")
    public ResponseEntity<RoomDTO> upDateRoom(
        @PathVariable Integer room_id,
        @RequestBody @Valid Room room,
        @RequestParam(value = "image_name", required = false) String image_name
    ) {
        RoomDTO roomDTO = roomService.update(room, room_id, image_name);
        return ResponseEntity.status(HttpStatus.OK).body(roomDTO);
    }

    @DeleteMapping("/{room_id}")
    public ResponseEntity<List<RoomDTO>> deleteById(
        @PathVariable Integer room_id,
        @RequestParam(value = "hotel_id", required = true) Integer hotel_id
    ) {
        List<RoomDTO> roomDTOs = roomService.deleteById(hotel_id, room_id);
        return ResponseEntity.status(HttpStatus.OK).body(roomDTOs);
    }
}
