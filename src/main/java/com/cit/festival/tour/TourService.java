package com.cit.festival.tour;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cit.festival.exception.NotFoundException;
import com.cit.festival.exception.TourException;
import com.cit.festival.festival.Festival;
import com.cit.festival.festival.FestivalRepositoty;
import com.cit.festival.festival.FestivalService;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.hotel.HotelDTO;
import com.cit.festival.hotel.HotelRepository;
import com.cit.festival.hotel.HotelService;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.image.ImageService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Service
public class TourService {
    
    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private FestivalService festivalService;

    @Autowired
    private FestivalRepositoty festivalRepositoty;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ImageService imageService;

    @Transactional
    public Tour addTour(Tour tour, String imageName) {
        Optional<Festival> festival = festivalService.findById(tour.getFestival().getId());
        if (!festival.isPresent()) {
            throw new NotFoundException("Không tìm thấy lễ hội");
        }

        Optional<Image> image = imageService.findByName(imageName);
        if (!image.isPresent()) {
            throw new NotFoundException("Ảnh không tồn tại");
        }

        List<Hotel> hotels = tour.getHotels();
        List<Hotel> hotels_save = new ArrayList<Hotel>();
        for (Hotel hotel : hotels) {

            Optional<Hotel> hotelDB = hotelRepository.findById(hotel.getId());
            if (!hotelDB.isPresent()) {
                throw new NotFoundException("Khách sạn không tồn tại id " + hotel.getId());
            }

            if (hotels_save.contains(hotelDB.get())) {
                throw new TourException("Khách sạn " + hotelDB.get().getName() + " đã được thêm vào tour");
            }

            hotels_save.add(hotelDB.get());
        }
     
        tour.setImage(image.get());
        tour.setHotels(hotels_save);
        return tourRepository.save(tour);
    }


    public TourDTO add(Tour tour, String imageName) {

        Optional<Festival> festival = festivalService.findById(tour.getFestival().getId());
        if (!festival.isPresent()) {
            throw new NotFoundException("Không tìm thấy lễ hội");
        }
        //tour.setFestival(festival.get()); //sử dụng trong ngữ cảnh bidirectional

        Optional<Image> image = imageService.findByName(imageName);
        if (!image.isPresent()) {
            throw new NotFoundException("Ảnh không tồn tại");
        }

        List<Hotel> hotels = tour.getHotels();
        List<Hotel> hotels_save = new ArrayList<Hotel>();
        List<HotelDTO> hotelDTOs = new ArrayList<HotelDTO>();
        for (Hotel hotel : hotels) {

            Optional<Hotel> hotelDB = hotelRepository.findById(hotel.getId());
            if (!hotelDB.isPresent()) {
                throw new NotFoundException("Khách sạn không tồn tại id " + hotel.getId());
            }
            
            HotelDTO hotelDTO = hotelService.findById(hotel.getId());
            hotelDTOs.add(hotelDTO);
            hotels_save.add(hotelDB.get());
        }

        ImageDTO imageDTO = new ImageDTO(image.get().getId(), image.get().getName(), image.get().getType());
        tour.setImage(image.get());
        tour.setHotels(hotels_save);
        Tour tourDB = tourRepository.save(tour);
        TourDTO tourDTO = new TourDTO(
            tourDB.getId(), 
            tourDB.getFestival().getId(), 
            tourDB.getName(), 
            tourDB.getFromWhere(),
            tourDB.getToWhere(), 
            tourDB.getDescription(),
            tourDB.getFromDate(),
            tourDB.getToDate(),
            tourDB.getPriceAdult(),
            tourDB.getPriceChild(),
            tourDB.getPriceBaby(),
            tourDB.getCapacity(),
            tourDB.getBooked(),
            imageDTO,
            hotelDTOs
        );
        return tourDTO;
    }

    @Transactional
    public List<TourDTO> findTours() {

        List<Tour> tours = tourRepository.findAllByOrderById();
        List<TourDTO> tourDTOs = new ArrayList<>();
        for (Tour tour : tours) {

            List<Hotel> hotels = tour.getHotels();
            List<HotelDTO> hotelDTOs = new ArrayList<>();
            for (Hotel hotel : hotels) {
                HotelDTO hotelDTO = hotelService.findById(hotel.getId());
                hotelDTOs.add(hotelDTO);
            }

            Image image = tour.getImage();
            ImageDTO imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            TourDTO tourDTO = new TourDTO(
                tour.getId(), 
                tour.getFestival().getId(), 
                tour.getName(), 
                tour.getFromWhere(),
                tour.getToWhere(), 
                tour.getDescription(),
                tour.getFromDate(),
                tour.getToDate(),
                tour.getPriceAdult(),
                tour.getPriceChild(),
                tour.getPriceBaby(),
                tour.getCapacity(),
                tour.getBooked(),
                imageDTO,
                hotelDTOs
            );
            tourDTOs.add(tourDTO);
        }
        return tourDTOs;
    }

    @Transactional
    public TourDTO findById(Integer id) {
        
        Optional<Tour> tourDB = tourRepository.findById(id);
        if (tourDB.isPresent()) {
            List<Hotel> hotels = tourDB.get().getHotels();
            List<HotelDTO> hotelDTOs = new ArrayList<HotelDTO>();
            for (Hotel hotel : hotels) {

                HotelDTO hotelDTO = hotelService.findById(hotel.getId());
                hotelDTOs.add(hotelDTO);
            }

            Image image = tourDB.get().getImage();
            ImageDTO imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            TourDTO tourDTO = new TourDTO(
                tourDB.get().getId(), 
                tourDB.get().getFestival().getId(), 
                tourDB.get().getName(), 
                tourDB.get().getFromWhere(),
                tourDB.get().getToWhere(), 
                tourDB.get().getDescription(),
                tourDB.get().getFromDate(),
                tourDB.get().getToDate(),
                tourDB.get().getPriceAdult(),
                tourDB.get().getPriceChild(),
                tourDB.get().getPriceBaby(),
                tourDB.get().getCapacity(),
                tourDB.get().getBooked(),
                imageDTO,
                hotelDTOs
            );
            return tourDTO;
        }
        return null;
    }

    @Transactional
    public TourDTO updateTourBooked(Integer tourId, Integer newBookedValue) {

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy tour"));   

        tour.setBooked(newBookedValue);
        Tour tourDB = tourRepository.save(tour);
        Image image = tourDB.getImage();
        ImageDTO imageDTO = null;
        if (image != null) {
            imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
        }

        List<Hotel> hotels = tourDB.getHotels();
        List<HotelDTO> hotelDTOs = new ArrayList<>();
        for (Hotel hotel : hotels) {
            HotelDTO hotelDTO = hotelService.findById(hotel.getId());
            hotelDTOs.add(hotelDTO);
        }

        TourDTO tourDTO = new TourDTO(
            tourDB.getId(), 
            tourDB.getFestival().getId(), 
            tourDB.getName(), 
            tourDB.getFromWhere(),
            tourDB.getToWhere(), 
            tourDB.getDescription(),
            tourDB.getFromDate(),
            tourDB.getToDate(),
            tourDB.getPriceAdult(),
            tourDB.getPriceChild(),
            tourDB.getPriceBaby(),
            tourDB.getCapacity(),
            tourDB.getBooked(),
            imageDTO,
            hotelDTOs
        );

        return tourDTO;
    }

    @Transactional
    public TourDTO update(Tour tour, Integer tour_id, String image_name) {

        Optional<Tour> tourDB = tourRepository.findById(tour_id);
        if (!tourDB.isPresent()) {
            throw new NotFoundException("Không tìm thấy tour");
        }

        Optional<Festival> festivalDB = festivalRepositoty.findById(tour.getFestival().getId());
        if (!festivalDB.isPresent()) {
            throw new NotFoundException("Không tìm thấy Lễ hội");
        }

        List<Hotel> hotels = tour.getHotels();
        List<Hotel> hotels_save = new ArrayList<Hotel>();
        List<HotelDTO> hotelDTOs = new ArrayList<HotelDTO>();
        for (Hotel hotel : hotels) {

            Optional<Hotel> hotelDB = hotelRepository.findById(hotel.getId());
            if (!hotelDB.isPresent()) {
                throw new NotFoundException("Khách sạn không tồn tại id " + hotel.getId());
            }

            if (hotels_save.contains(hotelDB.get())) {
                throw new TourException("Khách sạn " + hotelDB.get().getName() + " đã được thêm vào tour");
            }

            HotelDTO hotelDTO = hotelService.findById(hotel.getId());
            hotelDTOs.add(hotelDTO);
            hotels_save.add(hotelDB.get());
        }

        Optional<Image> new_image = imageService.findByName(image_name);
        Image imageUpdate = null;
        ImageDTO imageDTO = null;
        if (!new_image.isPresent()) {
            imageUpdate = tourDB.get().getImage();  //Lấy lại ảnh cũ nếu ko update ảnh mới
            imageDTO = new ImageDTO(imageUpdate.getId(), imageUpdate.getName(), imageUpdate.getType());
        }
        else {
            imageUpdate = new_image.get();
            imageDTO = new ImageDTO(new_image.get().getId(), new_image.get().getName(), new_image.get().getType());  
        }
        
        tourDB.get().setName(tour.getName());
        tourDB.get().setFromWhere(tour.getFromWhere());
        tourDB.get().setDescription(tour.getDescription());
        tourDB.get().setToWhere(tour.getToWhere());
        tourDB.get().setFromDate(tour.getFromDate());
        tourDB.get().setToDate(tour.getToDate());
        tourDB.get().setPriceAdult(tour.getPriceAdult());
        tourDB.get().setPriceChild(tour.getPriceChild());
        tourDB.get().setPriceBaby(tour.getPriceBaby());
        tourDB.get().setCapacity(tour.getCapacity());
        tourDB.get().setBooked(tour.getBooked());
        tourDB.get().setFestival(festivalDB.get());
        tourDB.get().setHotels(hotels_save);
        tourDB.get().setImage(imageUpdate);


        Tour tour_save = tourRepository.save(tourDB.get());
        TourDTO tourDTO = new TourDTO(
            tour_save.getId(), 
            tour_save.getFestival().getId(), 
            tour_save.getName(), 
            tour_save.getFromWhere(),
            tour_save.getToWhere(), 
            tour_save.getDescription(),
            tour_save.getFromDate(),
            tour_save.getToDate(),
            tour_save.getPriceAdult(),
            tour_save.getPriceChild(),
            tour_save.getPriceBaby(),
            tour_save.getCapacity(),
            tour_save.getBooked(),
            imageDTO,
            hotelDTOs
        );
        return tourDTO;
    }

    public void delete(Integer id) {
        tourRepository.deleteById(id);
    }

}
