package com.cit.festival.image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cit.festival.StringUtils;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.hotel.HotelRepository;
import com.cit.festival.room.Room;
import com.cit.festival.room.RoomRepository;

import jakarta.transaction.Transactional;

@Service
public class ImageService {
    
    private final ImageRepository imageRepository;
    private final HotelRepository hotelRepository;

    public ImageService(
        ImageRepository imageRepository,
        HotelRepository hotelRepository,
        RoomRepository roomRepository
    ) {
        this.imageRepository = imageRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public String uploadHotelImage(MultipartFile file, Integer hotelId) throws IOException {
        
        Optional<Hotel> optHotel = hotelRepository.findById(hotelId);
        if (!optHotel.isPresent()) {
            throw new NotFoundException("Không tìm thấy khách sạn");
        }
        
        Image imageData = imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageConfig.compressImage(file.getBytes()))
                .build()
                );

        if (imageData != null) {

            Hotel hotelDB = optHotel.get();
            hotelDB.setImage(imageData);
            hotelRepository.save(hotelDB);
            return "File ảnh upload thành công : " + file.getOriginalFilename();
        }
        return null;
    }


    @Transactional
    public ImageDTO uploadImage(MultipartFile file) throws IOException {

        Optional<Image> optImage = imageRepository.findByName(file.getOriginalFilename());
        if (optImage.isPresent()) {
            throw new NotFoundException("Ảnh đã tồn tại");
        }
        Image imageData = imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageConfig.compressImage(file.getBytes()))
                .build()
            );
        if (imageData != null) {
            ImageDTO imageDTO = StringUtils.createImageDTO(imageData);

            return imageDTO;
            //return "File ảnh upload thành công : " + file.getOriginalFilename();
        }
        return null;
    }

    @Transactional
    public byte[] downloadImage(String fileName) {
        Optional<Image> dbImageData = imageRepository.findByName(fileName);
        if (!dbImageData.isPresent()) {
            throw new NotFoundException("Ảnh không tồn tại");
        }

        byte[] images=ImageConfig.decompressImage(dbImageData.get().getImageData());
        return images;
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    @Transactional
    public Optional<Image> findByName(String fileName) {
        return imageRepository.findByName(fileName);
    }

    @Transactional
    public void deleteById(Long id) {
        imageRepository.deleteById(id);
    }

}
