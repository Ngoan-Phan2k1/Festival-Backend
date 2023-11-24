package com.cit.festival.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cit.festival.booktour.BookedTour;
import com.cit.festival.booktour.BookedTourDTO;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.hotel.HotelDTO;
import com.cit.festival.hotel.HotelService;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.room.RoomDTO;
import com.cit.festival.room.RoomService;
import com.cit.festival.tour.Tour;
import com.cit.festival.tour.TourDTO;
import com.cit.festival.tour.TourService;
import com.cit.festival.tourist.Tourist;
import com.cit.festival.tourist.TouristRepository;
import com.cit.festival.tourist.TouristService;

import jakarta.transaction.Transactional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TouristService touristService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelService hotelService;
    
    @Transactional
    public ContactDTO add(Contact contact) {

        // Optional<Contact> contactDB = contactRepository.findAllByRoomIdAndTouristId(contact.getRoom().getId(), contact.getTourist().getId());

        // if (contactDB.isPresent()) {
        //     throw new NotFoundException("Phòng đang trong quá trình liên hệ với quý khách");
        // }

        // RoomDTO roomDTO = roomService.findById(contact.getRoom().getId());
        // Optional<Tourist> tourist = touristService.findById(contact.getTourist().getId());

        // if (!tourist.isPresent()) {
        //     throw new NotFoundException("Không tìm thấy người đặt" );
        // }
        // Contact contactSaved = contactRepository.save(contact);

        // ContactDTO contactDTO = new ContactDTO(
        //     contactSaved.getId(),
        //     contactSaved.getTourist().getId(),
        //     contactSaved.getFullname(),
        //     contactSaved.getEmail(),
        //     contactSaved.getPhone(),
        //     roomDTO
        // );

        // return contactDTO;

        return null;
        
    }


    @Transactional
    public List<ContactDTO> findAllByTouristId(Integer tourist_id) {

        List<Contact> contacts = contactRepository.findAllByTouristId(tourist_id);
        List<ContactDTO> contactDTOs = new ArrayList<>();

        for (Contact contact: contacts) {
            Integer hotel_id = contact.getRoom().getHotel().getId();
            Image image = contact.getRoom().getImage();
            ImageDTO imageDTO = null;
            if (image != null) {
                imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            }
            HotelDTO hotelDTO = hotelService.findById(hotel_id);
            RoomDTO roomDTO = new RoomDTO(
                contact.getRoom().getId(),
                contact.getRoom().getHotel().getId(),
                contact.getRoom().getName(),
                contact.getRoom().getPrice(),
                contact.getRoom().getServices(),
                imageDTO,
                hotelDTO
            );

            ContactDTO contactDTO = new ContactDTO(
                contact.getId(),
                contact.getTourist().getId(),
                contact.getFullname(),
                contact.getEmail(),
                contact.getPhone(),
                roomDTO
            );
            contactDTOs.add(contactDTO);
        }

        return contactDTOs;
    }

    @Transactional
    public List<ContactDTO> deleteById(Integer id, Integer tourist_id) {

        contactRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy liên hệ"));
        
        contactRepository.deleteById(id);
        return findAllByTouristId(tourist_id);
    }
}
