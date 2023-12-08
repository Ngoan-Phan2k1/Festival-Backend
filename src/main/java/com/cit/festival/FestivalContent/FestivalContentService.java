package com.cit.festival.FestivalContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cit.festival.StringUtils;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.festival.Festival;
import com.cit.festival.festival.FestivalRepositoty;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.image.ImageService;
import com.cit.festival.schedule.Schedule;
import com.cit.festival.schedule.ScheduleDTO;
import com.cit.festival.tour.Tour;

import jakarta.transaction.Transactional;



@Service
public class FestivalContentService {

    private final FestivalContentRepository festivalContentRepository;
    private final FestivalRepositoty festivalRepositoty;
    private final ImageService imageService;

    public FestivalContentService(
        FestivalContentRepository festivalContentRepository,
        FestivalRepositoty festivalRepositoty,
        ImageService imageService
    ) {
        this.festivalContentRepository = festivalContentRepository;
        this.festivalRepositoty = festivalRepositoty;
        this.imageService = imageService;
    }

    @Transactional
    public FestivalContentDTO add(FestivalContent festivalContent, String imageName) {

        festivalRepositoty
            .findById(festivalContent.getFestival().getId())
            .orElseThrow(() -> new NotFoundException("Không tìm thấy lễ hội"));

        Optional<Image> image = imageService.findByName(imageName);
        Image imageDB = null;
        ImageDTO imageDTO = null;

        if (image.isPresent()) {
            //schedule.setImage(image.get());
            imageDB = image.get();
            imageDTO = StringUtils.createImageDTO(imageDB);
        }
        festivalContent.setImage(imageDB);
        FestivalContent festivalContentDB = festivalContentRepository.save(festivalContent);
        return StringUtils.createFestivalContentDTO(festivalContentDB, imageDTO);

    }

    @Transactional
    public FestivalContentDTO findById(Integer id) {

        Optional<FestivalContent> optFestivalContent = festivalContentRepository.findById(id);
        FestivalContent festivalContent = optFestivalContent.orElseThrow(() -> new NotFoundException("Không tìm thấy nội dung lễ hội"));
        Image image = festivalContent.getImage();
        ImageDTO imageDTO = null;
        if (image != null) {
            imageDTO = StringUtils.createImageDTO(image);
        }

        return StringUtils.createFestivalContentDTO(festivalContent, imageDTO);
    }

    @Transactional
    public List<FestivalContentDTO> findByFestivalId(Integer festivalId) {

        List<FestivalContent> festivalContents = festivalContentRepository.findByFestivalIdAndOrderByContentId(festivalId);
        List<FestivalContentDTO> festivalContentDTOs = new ArrayList<>();

        festivalContentDTOs = festivalContents.stream()
            .map(festivalContent -> {
                Image image = festivalContent.getImage();
                ImageDTO imageDTO = null;
                if (image != null) {
                    imageDTO = StringUtils.createImageDTO(image);
                }

                return StringUtils.createFestivalContentDTO(festivalContent, imageDTO);

            })
            .collect(Collectors.toList());

        return festivalContentDTOs;
    }

    
    @Transactional
    public FestivalContentDTO update(FestivalContent festivalContent, Integer festivalContentId, String imageName) {

        Optional<FestivalContent> optFestivalContent = festivalContentRepository.findById(festivalContentId);
        FestivalContent festivalContentDB = optFestivalContent.orElseThrow(() -> new NotFoundException("Không tìm thấy nội dung bài viết"));

        Optional<Festival> optFestival = festivalRepositoty.findById(festivalContent.getFestival().getId());

        Festival festivalDB = optFestival.orElseThrow(() -> new NotFoundException("Không tìm thấy lễ hội"));

        Optional<Image> optImageDB = imageService.findByName(imageName);
        Image imageUpdate = null;
        ImageDTO imageDTO = null;

        if (optImageDB.isPresent()) {
            imageUpdate = optImageDB.get();
            imageDTO = StringUtils.createImageDTO(imageUpdate);
            
        }
        else {

            imageUpdate = festivalContentDB.getImage();
            if (imageUpdate == null) {
                imageDTO = null;
            } 
            else {
                imageDTO = StringUtils.createImageDTO(imageUpdate);
            }
                  
        }

        festivalContentDB.setName(festivalContent.getName());
        festivalContentDB.setContent(festivalContent.getContent());
        festivalContentDB.setFestival(festivalDB);
        festivalContentDB.setImage(imageUpdate);

        FestivalContent festivalContentUpdate = festivalContentRepository.save(festivalContentDB);
        return StringUtils.createFestivalContentDTO(festivalContentUpdate, imageDTO);
    }

    @Transactional
    public List<FestivalContentDTO> deleteById(Integer festivalId, Integer festivalContentId) {

        Optional<FestivalContent> optFestivalContent = festivalContentRepository.findById(festivalContentId);
        FestivalContent festivalContentDB = optFestivalContent.orElseThrow(() -> new NotFoundException("Không tìm thấy nội dung bài viết"));

        Optional<Festival> optFestival = festivalRepositoty.findById(festivalId);

        Festival festivalDB = optFestival.orElseThrow(() -> new NotFoundException("Không tìm thấy lễ hội"));
        festivalContentRepository.delete(festivalContentDB);
        return findByFestivalId(festivalId);
    }

}
