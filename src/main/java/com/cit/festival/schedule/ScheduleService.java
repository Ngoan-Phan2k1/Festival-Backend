package com.cit.festival.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cit.festival.StringUtils;
import com.cit.festival.booktour.BookedTour;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.image.ImageRepository;
import com.cit.festival.image.ImageService;
import com.cit.festival.tour.Tour;
import com.cit.festival.tour.TourRepository;

import jakarta.transaction.Transactional;

@Service
public class ScheduleService {
    
    private final ScheduleRepository scheduleRepository;
    private final ImageService imageService;
    private final TourRepository tourRepository;
    private final ImageRepository imageRepository;

    public ScheduleService(
        ScheduleRepository scheduleRepository,
        ImageService imageService,
        TourRepository tourRepository,
        ImageRepository imageRepository
    ) {
        this.scheduleRepository = scheduleRepository;
        this.imageService = imageService;
        this.tourRepository = tourRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public ScheduleDTO add(Schedule schedule, String imageName) {

        Boolean tourDB = tourRepository.existsById(schedule.getTour().getId());
        if (!tourDB) {
            throw new NotFoundException("Tour không tồn tại");
        }

        Optional<Image> image = imageService.findByName(imageName);
        Image imageDB = null;
        ImageDTO imageDTO = null;

        Boolean checkSchedule = scheduleRepository.existsByTourIdAndDay(schedule.getTour().getId(), schedule.getDay());
        if (checkSchedule) {
            if (image.isPresent()) {
                imageService.deleteById(image.get().getId());
            }
            throw new NotFoundException("Lịch trình đã tồn tại");
        }

        if (image.isPresent()) {
            //schedule.setImage(image.get());
            imageDB = image.get();
            imageDTO = StringUtils.createImageDTO(imageDB);
           
        }
         
        schedule.setImage(imageDB);
        Schedule scheduleDB = scheduleRepository.save(schedule);
        ScheduleDTO scheduleDTO = StringUtils.createScheduleDTO(scheduleDB, imageDTO);
        return scheduleDTO;
    }

    @Transactional
    public ScheduleDTO findById(Integer id) {

        Optional<Schedule> optSchedule = scheduleRepository.findById(id);
        Schedule schedule = optSchedule.orElseThrow(() -> new NotFoundException("Không tìm thấy lịch trình"));
        Image image = schedule.getImage();
        ImageDTO imageDTO = null;
        if (image != null) {
            imageDTO = StringUtils.createImageDTO(image);
        }

        ScheduleDTO scheduleDTO = StringUtils.createScheduleDTO(schedule, imageDTO);
        
        return scheduleDTO;
    }

    @Transactional
    public List<ScheduleDTO> findByTourId(Integer tour_id) {

        List<Schedule> schedules = scheduleRepository.findByTourIdOrderByDayAsc(tour_id);
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

        scheduleDTOs = schedules.stream()
            .map(schedule -> {
                Image image = schedule.getImage();
                ImageDTO imageDTO = null;
                if (image != null) {
                    imageDTO = StringUtils.createImageDTO(image);
                }

                return StringUtils.createScheduleDTO(schedule, imageDTO);

            })
            .collect(Collectors.toList());

        return scheduleDTOs;
    }

    @Transactional
    public ScheduleDTO update(Schedule schedule, Integer schedule_id, String image_name) {

        Optional<Schedule> optScheduleDB = scheduleRepository.findById(schedule_id);
        Schedule scheduleDB = optScheduleDB.orElseThrow(() -> new NotFoundException("Không tìm thấy lịch trình"));

        Optional<Tour> optTourDB = tourRepository.findById(schedule.getTour().getId());
        if (!optTourDB.isPresent()) {
            throw new NotFoundException("Không tìm thấy Tour");
        }
        Tour tourDB = optTourDB.get();

        Optional<Image> optImageDB = imageService.findByName(image_name);
        Image imageUpdate = null;
        ImageDTO imageDTO = null;

        if (optImageDB.isPresent()) {
            imageUpdate = optImageDB.get();
            imageDTO = StringUtils.createImageDTO(imageUpdate);
            
        }
        else {

            imageUpdate = scheduleDB.getImage();
            if (imageUpdate == null) {
                imageDTO = null;
            } 
            else {
                imageDTO = StringUtils.createImageDTO(imageUpdate);
            }
                  
        }

        scheduleDB.setDay(schedule.getDay());
        scheduleDB.setMorning(schedule.getMorning());
        scheduleDB.setEvening(schedule.getEvening());
        scheduleDB.setNight(schedule.getNight());

        scheduleDB.setTour(tourDB);
        scheduleDB.setImage(imageUpdate);

        Schedule scheduleUpdate = scheduleRepository.save(scheduleDB);
        ScheduleDTO scheduleDTO = StringUtils.createScheduleDTO(scheduleUpdate, imageDTO);

        return scheduleDTO;
    }

}
