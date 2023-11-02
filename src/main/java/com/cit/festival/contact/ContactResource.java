package com.cit.festival.contact;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cit.festival.booktour.BookedTour;
import com.cit.festival.booktour.BookedTourDTO;
import com.cit.festival.room.RoomDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user/contact")
@CrossOrigin(origins = "*")
public class ContactResource {
    
    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactDTO> add(@RequestBody @Valid Contact contact) {

        ContactDTO contactDTO = contactService.add(contact);
        return ResponseEntity.status(HttpStatus.OK).body(contactDTO);
    }

    @GetMapping("/{tourist_id}")
    public ResponseEntity<List<ContactDTO>> findByTouristId(
        @PathVariable Integer tourist_id
    ) {
        List<ContactDTO> contactDTOs = contactService.findAllByTouristId(tourist_id);
        return ResponseEntity.status(HttpStatus.OK).body(contactDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<ContactDTO>> deleteById(
        @PathVariable Integer id,
        @RequestParam(value = "tourist_id") Integer tourist_id
    ) {
        List<ContactDTO> contactDTOs = contactService.deleteById(id, tourist_id);
        
        //return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.OK).body(contactDTOs);
    }

}
