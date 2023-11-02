package com.cit.festival.room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cit.festival.exception.NotFoundException;
import com.cit.festival.exception.RoomException;
import com.cit.festival.festival.Festival;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.hotel.HotelDTO;
import com.cit.festival.hotel.HotelRepository;
import com.cit.festival.hotel.HotelService;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.image.ImageRepository;
import com.cit.festival.image.ImageService;
import com.cit.festival.schedule.Schedule;
import com.cit.festival.tour.Tour;

import jakarta.transaction.Transactional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ImageService imageService;

    @Transactional
    public RoomDTO add(Room room, String imageName) {

        Integer hotel_id = room.getHotel().getId();
        Optional<Hotel> hotelDB = hotelRepository.findById(hotel_id);
        if (!hotelDB.isPresent()) {
            throw new NotFoundException("Khách sạn không tồn tại");
        }

        List<Room> rooms = roomRepository.findByHotelId(hotel_id);
        for (Room roomHotel : rooms) {
            if (roomHotel.getName().equals(room.getName())) {
                throw new RoomException("Tên phòng đã tồn tại trong khách sạn");
            }
        }

        Optional<Image> image = imageService.findByName(imageName);
        if (!image.isPresent()) {
            throw new NotFoundException("Ảnh không tồn tại");
        }
        ImageDTO imageDTO = new ImageDTO(image.get().getId(), image.get().getName(), image.get().getType());
        //System.out.println("File: " + file);
        
        room.setHotel(hotelDB.get());
        room.setImage(image.get());
        Room roomDB = roomRepository.save(room);

        Image imgHotel = hotelDB.get().getImage();
        ImageDTO imgHDto = new ImageDTO(
            imgHotel.getId(), 
            imgHotel.getName(), 
            imgHotel.getType()
        );

        HotelDTO hotelDTO = new HotelDTO(
            hotelDB.get().getId(),
            hotelDB.get().getName(),
            hotelDB.get().getLocation(),
            hotelDB.get().getIntroduce(),
            hotelDB.get().getServices(),
            imgHDto
        );
        
        RoomDTO roomDTO = new RoomDTO(
            roomDB.getId(),
            roomDB.getHotel().getId(),
            roomDB.getName(), 
            roomDB.getPrice(), 
            roomDB.getServices(), 
            imageDTO,
            hotelDTO
        );

        return roomDTO;
    }

    @Transactional
    public List<RoomDTO> findAll() {

        List<Room> rooms = roomRepository.findAll();
        List<RoomDTO> roomDTOs = new ArrayList<>();
        for (Room room : rooms) {

            // List<Image> images = hotel.getImages();
            // List<ImageDTO> imageDTOs = new ArrayList<>();
            // for (Image image : images) {
            //     ImageDTO imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            //     imageDTOs.add(imageDTO);
            // }
            HotelDTO hotelDTO = hotelService.findById(room.getHotel().getId());
            Image image = room.getImage();
            ImageDTO imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            RoomDTO roomDTO = new RoomDTO(
                room.getId(),
                room.getHotel().getId(),
                room.getName(),
                room.getPrice(),
                room.getServices(),
                imageDTO,
                hotelDTO
            );
            roomDTOs.add(roomDTO);
        }
        return roomDTOs;
    }

    @Transactional
    public List<RoomDTO> findByHotelId(Integer hotel_id) {

        List<Room> rooms = roomRepository.findRoomsByHotelIdOrderByidAsc(hotel_id);
        List<RoomDTO> roomDTOs = new ArrayList<>();
        HotelDTO hotelDTO = hotelService.findById(hotel_id);
        for (Room room : rooms) {

            // List<Image> images = hotel.getImages();
            // List<ImageDTO> imageDTOs = new ArrayList<>();
            // for (Image image : images) {
            //     ImageDTO imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            //     imageDTOs.add(imageDTO);
            // }
            Image image = room.getImage();
            ImageDTO imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());

            RoomDTO roomDTO = new RoomDTO(
                room.getId(),
                room.getHotel().getId(),
                room.getName(),
                room.getPrice(),
                room.getServices(),
                imageDTO,
                hotelDTO
            );
            roomDTOs.add(roomDTO);
        }
        return roomDTOs;
    }

    @Transactional
    public RoomDTO findById(Integer id) {
        Optional<Room> roomDB = roomRepository.findById(id);
        
        if (!roomDB.isPresent()) {
            throw new NotFoundException("Không tìm thấy phòng");
        }
        HotelDTO hotelDTO = hotelService.findById(roomDB.get().getHotel().getId());

        Image image = roomDB.get().getImage();
        ImageDTO imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
        RoomDTO roomDTO = new RoomDTO(
            roomDB.get().getId(),
            roomDB.get().getHotel().getId(),
            roomDB.get().getName(),
            roomDB.get().getPrice(),
            roomDB.get().getServices(),
            imageDTO,
            hotelDTO
        );
        return roomDTO;
    }

    @Transactional
    public RoomDTO update(Room room, Integer room_id, String image_name) {

        Optional<Room> roomDB = roomRepository.findById(room_id);
        if (!roomDB.isPresent()) {
            throw new NotFoundException("Không tìm thấy phòng");
        }

        Optional<Hotel> hotelDB = hotelRepository.findById(room.getHotel().getId());
        if (!hotelDB.isPresent()) {
            throw new NotFoundException("Không tìm thấy khách sạn");
        }

        Optional<Image> new_image = imageService.findByName(image_name);
        Image imageUpdate = null;
        ImageDTO imageDTO = null;

        if (!new_image.isPresent()) {
            imageUpdate = roomDB.get().getImage();
            imageDTO = new ImageDTO(imageUpdate.getId(),imageUpdate.getName(), imageUpdate.getType());    
        }
        else {
            imageUpdate = new_image.get();
            imageDTO = new ImageDTO(new_image.get().getId(), new_image.get().getName(), new_image.get().getType());  
        }

        roomDB.get().setName(room.getName());
        roomDB.get().setPrice(room.getPrice());
        roomDB.get().setServices(room.getServices());
        roomDB.get().setImage(imageUpdate);
        roomDB.get().setHotel(hotelDB.get());
        roomDB.get().setImage(imageUpdate);

        Room roomSaved = roomRepository.save(roomDB.get());
        HotelDTO hotelDTO = hotelService.findById(roomSaved.getHotel().getId());

        RoomDTO roomDTO = new RoomDTO(
            roomDB.get().getId(),
            roomDB.get().getHotel().getId(),
            roomDB.get().getName(),
            roomDB.get().getPrice(),
            roomDB.get().getServices(),
            imageDTO,
            hotelDTO
        );

        return roomDTO;
    }
    
}
