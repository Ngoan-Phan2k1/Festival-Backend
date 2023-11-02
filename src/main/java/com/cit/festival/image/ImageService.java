package com.cit.festival.image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cit.festival.exception.NotFoundException;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.hotel.HotelRepository;
import com.cit.festival.room.Room;
import com.cit.festival.room.RoomRepository;

import jakarta.transaction.Transactional;

@Service
public class ImageService {
    
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Transactional
    public String uploadHotelImage(MultipartFile file, Integer hotelId) throws IOException {
        

        Optional<Hotel> hotelDB = hotelRepository.findById(hotelId);
        if (!hotelDB.isPresent()) {
            throw new NotFoundException("Không tìm thấy khách sạn");
        }
        Image imageData = imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageConfig.compressImage(file.getBytes()))
                //.hotel(hotelDB.get())
                .build()
                );

        if (imageData != null) {

            //List<Image> images = hotelDB.get().getImages();
            //images.add(imageData);
            hotelDB.get().setImage(imageData);
            hotelRepository.save(hotelDB.get());
            return "File ảnh upload thành công : " + file.getOriginalFilename();
        }
        return null;
    }


    @Transactional
    public ImageDTO uploadImage(MultipartFile file) throws IOException {

        // Optional<Room> roomDB = roomRepository.findById(roomId);
        // if (!roomDB.isPresent()) {
        //     throw new NotFoundException("Không tìm thấy phòng");
        // }
        Optional<Image> dbImageData = imageRepository.findByName(file.getOriginalFilename());
        if (dbImageData.isPresent()) {
            throw new NotFoundException("Ảnh đã tồn tại");
        }
        Image imageData = imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageConfig.compressImage(file.getBytes()))
                .build()
                );
        if (imageData != null) {
            ImageDTO imageDTO = new ImageDTO(imageData.getId(), imageData.getName(), imageData.getType());
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

    // @Transactional
    // public List<ImageDTO> findByHotelId(Integer id) {
    //     List<Image> images = imageRepository.findAllByHotelId(id);
    //     List<ImageDTO> imageDTOs = new ArrayList<>();
    //     for (Image image : images) {
    //         ImageDTO imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType(), image.getImageData());
    //         imageDTOs.add(imageDTO);
    //     }

    //     return imageDTOs;
    // }
    
    // public List<byte[]> downloadAllImage() {

    //     List<Image> imagesDB = imageRepository.findAll();
    //     List<byte[]> images = new ArrayList<>();
    //     for (Image image : imagesDB) {
    //         byte[] imgbyte = downloadImage(image.getName());

    //         images.add(imgbyte);
    //     }
    //     return images;

    // }
}
