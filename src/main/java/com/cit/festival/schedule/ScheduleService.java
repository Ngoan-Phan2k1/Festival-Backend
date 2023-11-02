package com.cit.festival.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cit.festival.booktour.BookedTour;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.image.ImageRepository;
import com.cit.festival.image.ImageService;
import com.cit.festival.tour.Tour;
import com.cit.festival.tour.TourRepository;

import jakarta.transaction.Transactional;

@Service
public class ScheduleService {
    
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private ImageRepository imageRepository;

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
            imageDTO = new ImageDTO(image.get().getId(), image.get().getName(), image.get().getType());    
        }
         
        schedule.setImage(imageDB);
        Schedule scheduleDB = scheduleRepository.save(schedule);
        ScheduleDTO scheduleDTO = new ScheduleDTO(
            scheduleDB.getId(),
            scheduleDB.getTour().getId(),
            scheduleDB.getDay(),
            scheduleDB.getMorning(),
            scheduleDB.getEvening(),
            scheduleDB.getNight(),
            imageDTO
        );

        return scheduleDTO;
    }

    @Transactional
    public ScheduleDTO findById(Integer id) {

        Optional<Schedule> schedule = scheduleRepository.findById(id);
        if (!schedule.isPresent()) {
            throw new NotFoundException("Không tìm thấy lịch trình");
        }
        Image image = schedule.get().getImage();
        ImageDTO imageDTO = null;
        if (image != null) {
            imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
        }
        ScheduleDTO scheduleDTO = new ScheduleDTO(
            schedule.get().getId(),
            schedule.get().getTour().getId(),
            schedule.get().getDay(),
            schedule.get().getMorning(),
            schedule.get().getEvening(),
            schedule.get().getNight(),
            imageDTO
        );
        
        return scheduleDTO;

    }

    @Transactional
    public List<ScheduleDTO> findByTourId(Integer tour_id) {

        List<Schedule> schedules = scheduleRepository.findByTourIdOrderByDayAsc(tour_id);
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        for (Schedule schedule : schedules) {

            Image image = schedule.getImage();
            ImageDTO imageDTO = null;
            if (image != null) {
                imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            }
             
            ScheduleDTO scheduleDTO = new ScheduleDTO(
                schedule.getId(),
                schedule.getTour().getId(),
                schedule.getDay(),
                schedule.getMorning(),
                schedule.getEvening(),
                schedule.getNight(),
                imageDTO
            );
            scheduleDTOs.add(scheduleDTO);
        }

        return scheduleDTOs;
    }

    @Transactional
    public ScheduleDTO update(Schedule schedule, Integer schedule_id, String image_name) {

        Optional<Schedule> scheduleDB = scheduleRepository.findById(schedule_id);
        if (!scheduleDB.isPresent()) {
            throw new NotFoundException("Không tìm thấy lịch trình");
        }

        Optional<Tour> tourDB = tourRepository.findById(schedule.getTour().getId());
        if (!tourDB.isPresent()) {
            throw new NotFoundException("Không tìm thấy Tour");
        }

        Optional<Image> imageDB = imageService.findByName(image_name);
        Image imageUpdate = null;
        ImageDTO imageDTO = null;

        if (imageDB.isPresent()) {
            imageUpdate = imageDB.get();
            imageDTO = new ImageDTO(imageDB.get().getId(), imageDB.get().getName(), imageDB.get().getType());    
        }


        scheduleDB.get().setDay(schedule.getDay());
        scheduleDB.get().setMorning(schedule.getMorning());
        scheduleDB.get().setEvening(schedule.getEvening());
        scheduleDB.get().setNight(schedule.getNight());

        scheduleDB.get().setTour(tourDB.get());
        scheduleDB.get().setImage(imageUpdate);

        Schedule scheduleUpdate = scheduleRepository.save(scheduleDB.get());

        ScheduleDTO scheduleDTO = new ScheduleDTO(
            scheduleUpdate.getId(),
            scheduleUpdate.getTour().getId(),
            scheduleUpdate.getDay(),
            scheduleUpdate.getMorning(),
            scheduleUpdate.getEvening(),
            scheduleUpdate.getNight(),
            imageDTO
        );
        return scheduleDTO;
    }

}
