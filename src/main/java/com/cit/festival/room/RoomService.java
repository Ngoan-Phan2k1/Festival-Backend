package com.cit.festival.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.cit.festival.StringUtils;
import com.cit.festival.booktour.BookedTour;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.exception.RoomException;

import com.cit.festival.hotel.Hotel;
import com.cit.festival.hotel.HotelDTO;
import com.cit.festival.hotel.HotelRepository;
import com.cit.festival.hotel.HotelService;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.image.ImageService;

import jakarta.transaction.Transactional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final HotelService hotelService;
    private final ImageService imageService;

    public RoomService(
        RoomRepository roomRepository,
        HotelRepository hotelRepository,
        HotelService hotelService,
        ImageService imageService
    ) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.hotelService = hotelService;
        this.imageService = imageService;
    }

    @Transactional
    public RoomDTO add(Room room, String imageName) {

        Integer hotel_id = room.getHotel().getId();
        Optional<Hotel> optHotel = hotelRepository.findById(hotel_id);
        Hotel hotel = optHotel.orElseThrow(() -> new NotFoundException("Khách sạn không tồn tại"));

        List<Room> rooms = roomRepository.findByHotelId(hotel_id);

        boolean checkRoom = rooms.stream()
            .anyMatch(roomHotel -> roomHotel.getName().equals(room.getName()));
        
        if (checkRoom) {
            throw new RoomException("Tên phòng đã tồn tại trong khách sạn");
        }

        Optional<Image> optImage = imageService.findByName(imageName);
        Image image = optImage.orElseThrow(() -> new NotFoundException("Ảnh không tồn tại"));
        ImageDTO imageDTO = StringUtils.createImageDTO(image);
        
        room.setHotel(hotel);
        room.setImage(image);
        Room roomDB = roomRepository.save(room);

        Image imgHotel = hotel.getImage();

        ImageDTO imgHDto = StringUtils.createImageDTO(imgHotel);
        HotelDTO hotelDTO = StringUtils.createHotelDTO(hotel, imgHDto);
        RoomDTO roomDTO = StringUtils.createRoomDTO(roomDB, hotelDTO, imageDTO);
        return roomDTO;
    }

    @Transactional
    public List<RoomDTO> findAll() {

        List<Room> rooms = roomRepository.findAll();
        List<RoomDTO> roomDTOs = new ArrayList<>();
        roomDTOs = rooms.stream()
            .map(room -> {

                HotelDTO hotelDTO = hotelService.findById(room.getHotel().getId());
                Image image = room.getImage();
                ImageDTO imageDTO = StringUtils.createImageDTO(image);

                return StringUtils.createRoomDTO(room, hotelDTO, imageDTO);    
            })
            .collect(Collectors.toList());
        return roomDTOs;
    }

    @Transactional
    public List<RoomDTO> findByHotelId(Integer hotel_id) {

        List<Room> rooms = roomRepository.findRoomsByHotelIdOrderByidAsc(hotel_id);
        List<RoomDTO> roomDTOs = new ArrayList<>();
        HotelDTO hotelDTO = hotelService.findById(hotel_id);

        roomDTOs = rooms.stream()
            .map(room -> {
                Image image = room.getImage();
                ImageDTO imageDTO = StringUtils.createImageDTO(image);
                
                return StringUtils.createRoomDTO(room, hotelDTO, imageDTO);
            })
            .collect(Collectors.toList());

        return roomDTOs;
    }

    @Transactional
    public RoomDTO findById(Integer id) {
        Optional<Room> optRoom = roomRepository.findById(id);
        Room room = optRoom.orElseThrow(() -> new NotFoundException("Không tìm thấy phòng"));
        
        HotelDTO hotelDTO = hotelService.findById(room.getHotel().getId());
        Image image = room.getImage();
        ImageDTO imageDTO = StringUtils.createImageDTO(image);
        RoomDTO roomDTO = StringUtils.createRoomDTO(room, hotelDTO, imageDTO);

        return roomDTO;
    }

    @Transactional
    public RoomDTO update(Room room, Integer room_id, String image_name) {

        Optional<Room> optRoom = roomRepository.findById(room_id);
        Room roomDB = optRoom.orElseThrow(() -> new NotFoundException("Không tìm thấy phòng"));

        Optional<Hotel> optHotel = hotelRepository.findById(room.getHotel().getId());
        Hotel hotelDB =  optHotel.orElseThrow(() -> new NotFoundException("Không tìm thấy khách sạn"));

        Optional<Image> new_image = imageService.findByName(image_name);
        Image imageUpdate = null;
        ImageDTO imageDTO = null;

        if (!new_image.isPresent()) {
            imageUpdate = roomDB.getImage();
            imageDTO = StringUtils.createImageDTO(imageUpdate);

        }
        else {
            imageUpdate = new_image.get();
            imageDTO = StringUtils.createImageDTO(imageUpdate);
            
        }

        roomDB.setName(room.getName());
        roomDB.setPrice(room.getPrice());
        roomDB.setServices(room.getServices());
        roomDB.setImage(imageUpdate);
        roomDB.setHotel(hotelDB);
        roomDB.setImage(imageUpdate);

        Room roomSaved = roomRepository.save(roomDB);
        HotelDTO hotelDTO = hotelService.findById(roomSaved.getHotel().getId());

        RoomDTO roomDTO = StringUtils.createRoomDTO(roomDB, hotelDTO, imageDTO);
        return roomDTO;
    }

    @Transactional
    public List<RoomDTO> deleteById(Integer hotel_id ,Integer room_id) {

        hotelRepository
            .findById(hotel_id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy khách sạn"));

        roomRepository
            .findById(room_id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy phòng"));;

        roomRepository.deleteById(room_id);
        return findByHotelId(hotel_id);
    }
    
}
