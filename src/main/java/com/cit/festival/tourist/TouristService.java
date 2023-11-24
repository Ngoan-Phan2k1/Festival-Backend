package com.cit.festival.tourist;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cit.festival.StringUtils;
import com.cit.festival.booktour.BookedTour;
import com.cit.festival.booktour.BookedTourRepository;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.image.ImageService;
import com.cit.festival.user.User;
import com.cit.festival.user.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class TouristService {
    
    private final TouristRepository touristRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final BookedTourRepository bookedTourRepository;

    public TouristService(
        TouristRepository touristRepository,
        ImageService imageService,
        UserRepository userRepository,
        BookedTourRepository bookedTourRepository
    ) {
        this.touristRepository = touristRepository;
        this.imageService = imageService;
        this.userRepository = userRepository;
        this.bookedTourRepository = bookedTourRepository;
    }

    @Transactional
    public TouristDTO findByUserName(String username) {
        Tourist tourist = touristRepository.findTouristByUserName(username);
        if (tourist != null) {
            Image image = tourist.getImage();
            ImageDTO imageDTO = null;
            if (image != null) {
                imageDTO = StringUtils.createImageDTO(image);
            }
            TouristDTO touristDTO = StringUtils.createTouristDTO(tourist, imageDTO);
            return touristDTO;
        }
        return null;
    }

    @Transactional
    public TouristDTO findById(Integer id) {
        Optional<Tourist> optTourist = touristRepository.findById(id);
        if (optTourist.isPresent()) {
            Tourist tourist = optTourist.get();

            Image image = tourist.getImage();
            ImageDTO imageDTO = null;
            if (image != null) {
                imageDTO = StringUtils.createImageDTO(image);
            }

            TouristDTO touristDTO = StringUtils.createTouristDTO(tourist, imageDTO);
            return touristDTO;
        }

        return null;
    }

    @Transactional
    public List<TouristDTO> findAll() {

        List<Tourist> tourists = touristRepository.findAll();
        List<TouristDTO> touristDTOs = new ArrayList<>();
        touristDTOs = tourists.stream()
            .map(tourist -> {

                Image image = tourist.getImage();
                ImageDTO imageDTO = null;
                if (image != null) {
                    imageDTO = StringUtils.createImageDTO(image);
                }

                return StringUtils.createTouristDTO(tourist, imageDTO);
            })
            .collect(Collectors.toList());

        return touristDTOs;
    }

    @Transactional
    public TouristDTO update(TouristPut tourist, Integer tourist_id, String image_name) {

        Optional<Tourist> optTourist = touristRepository.findById(tourist_id);
        Tourist touristDB = optTourist.orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        Optional<Image> new_image = imageService.findByName(image_name);
        Image imageUpdate = null;
        ImageDTO imageDTO = null;
        if (!new_image.isPresent() && touristDB.getImage() != null) {
            imageUpdate = touristDB.getImage();  //Lấy lại ảnh cũ nếu ko update ảnh mới
            imageDTO = StringUtils.createImageDTO(imageUpdate);
        }
        else if (new_image.isPresent()) {
            imageUpdate = new_image.get();
            imageDTO = StringUtils.createImageDTO(imageUpdate);
        }

        touristDB.setFullname(tourist.getFullname());
        touristDB.setEmail(tourist.getEmail());
        touristDB.setImage(imageUpdate);
        
        User user = touristDB.getUser();
        user.setUsername(tourist.getUsername());
        userRepository.save(user);
        Tourist tourist_saved = touristRepository.save(touristDB);
        TouristDTO touristDTO = StringUtils.createTouristDTO(tourist_saved, imageDTO);
        
        return touristDTO;
    }

    @Transactional
    public List<TouristDTO> deleteById(Integer id) {

        Optional<Tourist> optTourist = touristRepository.findById(id);
        Tourist tourist = optTourist.orElseThrow(() -> new NotFoundException("Không tìm thấy tourist"));
        
        List<BookedTour> bookedTours =  bookedTourRepository.findAllByTouristId(id);
        bookedTours.stream()
            .map(booked -> {
                booked.setTourist(null);
                return bookedTourRepository.save(booked);
            })
            .collect(Collectors.toList());
        
        userRepository.deleteById(tourist.getUser().getId());
        touristRepository.deleteById(id);
        return findAll();
    }

}
