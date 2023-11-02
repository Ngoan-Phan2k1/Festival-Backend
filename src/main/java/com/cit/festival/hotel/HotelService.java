package com.cit.festival.hotel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ImageService imageService;

    @Transactional
    public List<HotelDTO> findAll() {
        List<Hotel> hotels = hotelRepository.findAll();
        List<HotelDTO> hotelDTOs = new ArrayList<>();
        for (Hotel hotel : hotels) {

            // List<Image> images = hotel.getImages();
            // List<ImageDTO> imageDTOs = new ArrayList<>();
            // for (Image image : images) {
            //     ImageDTO imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            //     imageDTOs.add(imageDTO);
            // }
            Image image = hotel.getImage();
            ImageDTO imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            HotelDTO hotelDTO = new HotelDTO(
                hotel.getId(),
                hotel.getName(),
                hotel.getLocation(),
                hotel.getIntroduce(),
                hotel.getServices(),
                //hotel.getRooms(),
                imageDTO
            );
            hotelDTOs.add(hotelDTO);
        }

        return hotelDTOs;
    }
    
    @Transactional
    public HotelDTO findById(Integer id) {
        
        Optional<Hotel> hotelDB = hotelRepository.findById(id);
        if (hotelDB.isPresent()) {

            // List<Image> images = hotelDB.get().getImages();
            // List<ImageDTO> imageDTOs = new ArrayList<>();
            // for (Image image : images) {
            //     ImageDTO imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            //     imageDTOs.add(imageDTO);
            // }
            Image image = hotelDB.get().getImage();
            ImageDTO imageDTO = null;
            if (image != null){
                imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            }
              

            HotelDTO hotelDTO = new HotelDTO(
                hotelDB.get().getId(),
                hotelDB.get().getName(),
                hotelDB.get().getLocation(),
                hotelDB.get().getIntroduce(),
                hotelDB.get().getServices(),
                //hotelDB.get().getRooms(),
                imageDTO
            );
            return hotelDTO;
           
        }
        return null;
    }

    public Optional<Hotel> findByName(String name) {
        return hotelRepository.findByName(name);
    }

    //@Transactional
    public HotelDTO add(Hotel hotel, String imageName) {
        Boolean hotelDB = hotelRepository.existsByName(hotel.getName());
        if (hotelDB) {
            throw new HotelException("Khách sạn đã tồn tại");
        }

        Optional<Image> image = imageService.findByName(imageName);
        if (!image.isPresent()) {
            throw new NotFoundException("Ảnh không tồn tại");
        }
        ImageDTO imageDTO = new ImageDTO(image.get().getId(), image.get().getName(), image.get().getType());
        hotel.setImage(image.get());
        Hotel hotelSave = hotelRepository.save(hotel);
        HotelDTO hotelDTO = new HotelDTO(
           hotelSave.getId(),
           hotelSave.getName(),
           hotelSave.getLocation(),
           hotelSave.getIntroduce(),
           hotelSave.getServices(),
           imageDTO
        );
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

        Optional<Hotel> hotelDB = hotelRepository.findById(hotel_id);
        if (!hotelDB.isPresent()) {
            throw new NotFoundException("Không tìm thấy khách sạn");
        }

        Optional<Image> new_image = imageService.findByName(image_name);
        Image imageUpdate = null;
        ImageDTO imageDTO = null;
        if (!new_image.isPresent()) {
            imageUpdate = hotelDB.get().getImage();  //Lấy lại ảnh cũ nếu ko update ảnh mới
            imageDTO = new ImageDTO(imageUpdate.getId(), imageUpdate.getName(), imageUpdate.getType());
        }
        else {
            imageUpdate = new_image.get();
            imageDTO = new ImageDTO(new_image.get().getId(), new_image.get().getName(), new_image.get().getType());  
        }
        
        hotelDB.get().setName(hotel.getName());
        hotelDB.get().setIntroduce(hotel.getIntroduce());
        hotelDB.get().setLocation(hotel.getLocation());
        hotelDB.get().setServices(hotel.getServices());
        hotelDB.get().setTours(hotel.getTours());
        hotelDB.get().setImage(imageUpdate);

        Hotel hotel_save = hotelRepository.save(hotelDB.get());
        HotelDTO hotelDTO = new HotelDTO(
           hotel_save.getId(),
           hotel_save.getName(),
           hotel_save.getLocation(),
           hotel_save.getIntroduce(),
           hotel_save.getServices(),
           imageDTO
        );
        return hotelDTO;
    }

}
