package com.cit.festival.hotel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cit.festival.StringUtils;
import com.cit.festival.exception.HotelException;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.exception.RoomException;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.image.ImageService;
import com.cit.festival.room.Room;
import com.cit.festival.room.RoomDTO;
import com.cit.festival.room.RoomRepository;
import com.cit.festival.schedule.Schedule;
import com.cit.festival.tour.Tour;

import jakarta.transaction.Transactional;

@Service
public class HotelService {
    
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ImageService imageService;

    public HotelService(
        HotelRepository hotelRepository,
        RoomRepository roomRepository,
        ImageService imageService
    ) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.imageService = imageService;
    }

    @Transactional
    public List<HotelDTO> findAll() {
        List<Hotel> hotels = hotelRepository.findAll();
        List<HotelDTO> hotelDTOs = new ArrayList<>();

        hotelDTOs = hotels.stream()
            .map(hotel -> {
                Image image = hotel.getImage();
                ImageDTO imageDTO = StringUtils.createImageDTO(image);

                return StringUtils.createHotelDTO(hotel, imageDTO);
            })
            .collect(Collectors.toList());

        return hotelDTOs;
    }
    
    @Transactional
    public HotelDTO findById(Integer id) {
        
        Optional<Hotel> optHotel = hotelRepository.findById(id);
        if (optHotel.isPresent()) {
            Hotel hotelDB = optHotel.get();
            Image image = hotelDB.getImage();
            ImageDTO imageDTO = null;
            if (image != null){
                imageDTO = StringUtils.createImageDTO(image);
            }

            HotelDTO hotelDTO = StringUtils.createHotelDTO(hotelDB, imageDTO);      
            return hotelDTO;
           
        }
        return null;
    }

    public Optional<Hotel> findByName(String name) {
        return hotelRepository.findByName(name);
    }

    @Transactional
    public HotelDTO add(Hotel hotel, String imageName) {
        Boolean hotelDB = hotelRepository.existsByName(hotel.getName());
        if (hotelDB) {
            throw new HotelException("Khách sạn đã tồn tại");
        }

        Optional<Image> optImage = imageService.findByName(imageName);
        if (!optImage.isPresent()) {
            throw new NotFoundException("Ảnh không tồn tại");
        }

        Image image = optImage.get();
        ImageDTO imageDTO = StringUtils.createImageDTO(image);

        hotel.setImage(image);
        Hotel hotelSave = hotelRepository.save(hotel);

        HotelDTO hotelDTO = StringUtils.createHotelDTO(hotelSave, imageDTO);

        return hotelDTO;
    }

    public List<Hotel> deleteById(Integer id) {
        Boolean hotelDB = hotelRepository.existsById(id);
        if (!hotelDB) {
            throw new HotelException("Không tìm thấy khách sạn");
        }
        hotelRepository.deleteById(id);
        return hotelRepository.findAll();
    }

    @Transactional
    public HotelDTO update(Hotel hotel, Integer hotel_id, String image_name) {

        Optional<Hotel> optHotel = hotelRepository.findById(hotel_id);
        if (!optHotel.isPresent()) {
            throw new NotFoundException("Không tìm thấy khách sạn");
        }

        Optional<Image> new_image = imageService.findByName(image_name);
        Image imageUpdate = null;
        ImageDTO imageDTO = null;
        Hotel hotelDB = optHotel.get();
        if (!new_image.isPresent()) {
            imageUpdate = hotelDB.getImage();  //Lấy lại ảnh cũ nếu ko update ảnh mới
            imageDTO = StringUtils.createImageDTO(imageUpdate);
        }
        else {
            imageUpdate = new_image.get();
            imageDTO = StringUtils.createImageDTO(imageUpdate);
            
        }
        
        hotelDB.setName(hotel.getName());
        hotelDB.setIntroduce(hotel.getIntroduce());
        hotelDB.setLocation(hotel.getLocation());
        //hotelDB.setServices(hotel.getServices());
        //hotelDB.setTours(hotel.getTours());
        hotelDB.setImage(imageUpdate);

        Hotel hotel_save = hotelRepository.save(hotelDB);

        HotelDTO hotelDTO = StringUtils.createHotelDTO(hotel_save, imageDTO);

        return hotelDTO;
    }

}
