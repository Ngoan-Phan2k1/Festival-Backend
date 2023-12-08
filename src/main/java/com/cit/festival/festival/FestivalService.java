package com.cit.festival.festival;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cit.festival.StringUtils;
import com.cit.festival.FestivalContent.FestivalContent;
import com.cit.festival.FestivalContent.FestivalContentDTO;
import com.cit.festival.FestivalContent.FestivalContentService;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.hotel.HotelDTO;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.image.ImageService;
import com.cit.festival.tour.Tour;
import com.cit.festival.tour.TourDTO;

import jakarta.transaction.Transactional;

@Service
public class FestivalService {
    
    private final FestivalRepositoty festivalRepositoty;
    private final ImageService imageService;
    private final FestivalContentService festivalContentService;

    public FestivalService(
        FestivalRepositoty festivalRepositoty,
        ImageService imageService,
        FestivalContentService festivalContentService
    ) {
        this.festivalRepositoty = festivalRepositoty;
        this.imageService = imageService;
        this.festivalContentService = festivalContentService;
    }

    @Transactional
    public FestivalDTO add(Festival festival, String imageName) {

        Optional<Image> optImage = imageService.findByName(imageName);
        Image image = optImage.orElseThrow(() -> new NotFoundException("Ảnh không tồn tại"));

        festival.setImage(image);
        Festival festivalDB = festivalRepositoty.save(festival);
        List<FestivalContentDTO> festivalContentDTOs = new ArrayList<>();
        ImageDTO imageDTO = StringUtils.createImageDTO(festivalDB.getImage());
        return StringUtils.createFestivalDTO(festivalDB, imageDTO, festivalContentDTOs);
    }

    @Transactional
    public FestivalDTO findById(Integer id) {
        Optional<Festival> optFestival = festivalRepositoty.findById(id);
        Festival festival = optFestival.orElseThrow(() -> new NotFoundException("Lễ hội không tồn tại"));           
        ImageDTO imageDTO = StringUtils.createImageDTO(festival.getImage());
        List<FestivalContentDTO> festivalContentDTOs = festivalContentService.findByFestivalId(id);
        return StringUtils.createFestivalDTO(festival, imageDTO, festivalContentDTOs);
    }

    @Transactional
    public List<FestivalDTO> findFestivals() {
        List<Festival> festivals = festivalRepositoty.findAllByOrderById();
        List<FestivalDTO> festivalDTOs = new ArrayList<>();

        festivalDTOs = festivals.stream()
            .map(festival -> {

                Image image = festival.getImage();
                ImageDTO imageDTO = StringUtils.createImageDTO(image);
                List<FestivalContentDTO> festivalContentDTOs = festivalContentService.findByFestivalId(festival.getId());

                return StringUtils.createFestivalDTO(festival, imageDTO, festivalContentDTOs);    
            })
            .collect(Collectors.toList());

        return festivalDTOs;
    }

    @Transactional
    public FestivalDTO update(Festival festival, Integer festivalId, String imageName) {

        Optional<Festival> optFestival = festivalRepositoty.findById(festivalId);
        Festival festivalDB = optFestival.orElseThrow(() -> new NotFoundException("Không tìm thấy lễ hội"));

        Optional<Image> newImage = imageService.findByName(imageName);
        Image imageUpdate = null;
        ImageDTO imageDTO = null;
        
        if (!newImage.isPresent()) {
            imageUpdate = festivalDB.getImage();  //Lấy lại ảnh cũ nếu ko update ảnh mới
            imageDTO = StringUtils.createImageDTO(imageUpdate);
        }
        else {
            imageUpdate = newImage.get();
            imageDTO = StringUtils.createImageDTO(imageUpdate);
        }

        festivalDB.setBlogName(festival.getBlogName());
        festivalDB.setDateOfPost(festival.getDateOfPost());
        festivalDB.setImage(imageUpdate);

        List<FestivalContentDTO> festivalContentDTOs = festivalContentService.findByFestivalId(festivalDB.getId());

        Festival festivalSave = festivalRepositoty.save(festivalDB);
        return StringUtils.createFestivalDTO(festivalSave, imageDTO, festivalContentDTOs);
    }

    @Transactional
    public List<FestivalDTO> deleteById(Integer festivalId) {

        Optional<Festival> optFestival = festivalRepositoty.findById(festivalId);
        Festival festivalDB = optFestival.orElseThrow(() -> new NotFoundException("Không tìm thấy lễ hội"));
        List<FestivalContentDTO> festivalContentDTOs = festivalContentService.findByFestivalId(festivalId);
        festivalContentDTOs.stream()
            .forEach(content -> festivalContentService.deleteById(festivalId, content.getId()));

        festivalRepositoty.deleteById(festivalId);
        return findFestivals();
    }

    @Transactional
    public List<FestivalDTO> findBySearchName(List<String> searchTerm) {
        //Optional<Festival> optFestival = festivalRepositoty.findByBlogNameContainingIgnoreCase(searchTerm);
        List<FestivalDTO> festivalDTOs = new ArrayList<>();
        festivalDTOs = searchTerm.stream()
                    .map(search -> {
                        Optional<Festival> optFestival = festivalRepositoty.findByBlogNameContainingIgnoreCase(search);
                        Festival festivalDB = optFestival.orElseThrow(() -> new NotFoundException("Không tìm thấy lễ hội"));
                        ImageDTO imageDTO = StringUtils.createImageDTO(festivalDB.getImage());
                        List<FestivalContentDTO> festivalContentDTOs = festivalContentService.findByFestivalId(festivalDB.getId());
                        return StringUtils.createFestivalDTO(festivalDB, imageDTO, festivalContentDTOs);   
                    })
                    .collect(Collectors.toList());
        
        return festivalDTOs;
        
    }
    
}

