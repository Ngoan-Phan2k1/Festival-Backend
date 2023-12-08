package com.cit.festival.tour;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cit.festival.StringUtils;
import com.cit.festival.exception.NotFoundException;
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


@Service
public class TourService {
    
    private final TourRepository tourRepository;
    private final FestivalService festivalService;
    private final FestivalRepositoty festivalRepositoty;
    private final HotelRepository hotelRepository;
    private final HotelService hotelService;
    private final ImageService imageService;

    public TourService(
        TourRepository tourRepository,
        FestivalService festivalService,
        FestivalRepositoty festivalRepositoty,
        HotelRepository hotelRepository,
        HotelService hotelService,
        ImageService imageService
    ) {
        this.tourRepository = tourRepository;
        this.festivalService = festivalService;
        this.festivalRepositoty = festivalRepositoty;
        this.hotelRepository = hotelRepository;
        this.hotelService = hotelService;
        this.imageService = imageService;
    }

    @Transactional
    public Tour addTour(Tour tour, String imageName) {
        // festivalService.findById(tour.getFestival().getId())
        //     .orElseThrow(() -> new NotFoundException("Không tìm thấy lễ hội"));

        Optional<Image> optImage = imageService.findByName(imageName);
        Image image = optImage.orElseThrow(() -> new NotFoundException("Ảnh không tồn tại"));

        //List<Hotel> hotels = tour.getHotels();
        Hotel hotel = tour.getHotel();
        Optional<Hotel> optHotelDB = hotelRepository.findById(hotel.getId());
        Hotel hotelDB = optHotelDB.orElseThrow(() -> new NotFoundException("Khách sạn không tồn tại"));

        // List<Hotel> hotels_save = new ArrayList<Hotel>();

        // hotels_save = hotels.stream()
        //     .map(hotel -> {

        //         Optional<Hotel> hotelDB = hotelRepository.findById(hotel.getId());
        //         if (!hotelDB.isPresent()) {
        //             throw new NotFoundException("Khách sạn không tồn tại id " + hotel.getId());
        //         }
        //         return hotelDB.get();
        //     })
        //     .collect(Collectors.toList());
     
        tour.setImage(image);
        tour.setHotel(hotelDB);
        return tourRepository.save(tour);
    }


    public TourDTO add(Tour tour, String imageName) {

        // festivalService
        //     .findById(tour.getFestival().getId())
        //     .orElseThrow(() -> new NotFoundException("Không tìm thấy lễ hội"));

        //tour.setFestival(festival.get()); //sử dụng trong ngữ cảnh bidirectional
        Optional<Tour> checkTour = tourRepository.findByName(tour.getName());
        if (checkTour.isPresent()) {
            throw new NotFoundException("Tour đã tồn tại");
        }

        Optional<Image> optImage = imageService.findByName(imageName);
        Image image = optImage.orElseThrow(() -> new NotFoundException("Ảnh không tồn tại"));

        //List<Hotel> hotels = tour.getHotels();
        Hotel hotel = tour.getHotel();
        Optional<Hotel> optHotel = hotelRepository.findById(hotel.getId());
        Hotel hotelDB = optHotel.orElseThrow(() -> new NotFoundException("Khách sạn không tồn tại"));
        HotelDTO hotelDTO = hotelService.findById(hotel.getId());

        // List<Hotel> hotels_save = new ArrayList<Hotel>();
        // List<HotelDTO> hotelDTOs = new ArrayList<HotelDTO>();

        // hotels_save = hotels.stream()
        //     .map(hotel -> {

        //         Optional<Hotel> optHotel = hotelRepository.findById(hotel.getId());
        //         Hotel hotelDB = optHotel.orElseThrow(() -> new NotFoundException("Khách sạn không tồn tại id "));
                
        //         HotelDTO hotelDTO = hotelService.findById(hotel.getId());
        //         hotelDTOs.add(hotelDTO);
        //         return hotelDB;
        //     })
        //     .collect(Collectors.toList());
        
        //Image image = optImage.get();
        ImageDTO imageDTO = StringUtils.createImageDTO(image);
        
        tour.setImage(image);
        //tour.setHotels(hotels_save);
        tour.setHotel(hotelDB);
        Tour tourDB = tourRepository.save(tour);
        TourDTO tourDTO =  StringUtils.createTourDTO(tourDB, imageDTO, hotelDTO);
        return tourDTO;
    }

    @Transactional
    public List<TourDTO> findToursActive() {

        List<Tour> tours = tourRepository.findAllByActiveIsTrueOrderById();
        List<TourDTO> tourDTOs = new ArrayList<>();

        tourDTOs = tours.stream()
            .map(tour -> {

                // List<HotelDTO> hotelDTOs = tour.getHotels().stream()
                //     .map(hotel -> hotelService.findById(hotel.getId()))
                //     .collect(Collectors.toList());

                HotelDTO hotelDTO = hotelService.findById(tour.getHotel().getId());

                Image image = tour.getImage();
                ImageDTO imageDTO = StringUtils.createImageDTO(image);


                return StringUtils.createTourDTO(tour, imageDTO, hotelDTO);
            })
            .collect(Collectors.toList());

        return tourDTOs;
    }

    @Transactional
    public List<TourDTO> findTours() {

        List<Tour> tours = tourRepository.findAllByOrderById();
        List<TourDTO> tourDTOs = new ArrayList<>();

        tourDTOs = tours.stream()
            .map(tour -> {

                // List<HotelDTO> hotelDTOs = tour.getHotels().stream()
                //     .map(hotel -> hotelService.findById(hotel.getId()))
                //     .collect(Collectors.toList());

                HotelDTO hotelDTO = hotelService.findById(tour.getHotel().getId());

                Image image = tour.getImage();
                ImageDTO imageDTO = StringUtils.createImageDTO(image);

                return StringUtils.createTourDTO(tour, imageDTO, hotelDTO);    
            })
            .collect(Collectors.toList());

        return tourDTOs;
    }

    @Transactional
    public Optional<TourDTO> findById(Integer id) {
        
        Optional<Tour> optTour = tourRepository.findById(id);
        if (optTour.isPresent()) {
            Tour tour = optTour.get();
            // List<Hotel> hotels = tour.getHotels();
            // List<HotelDTO> hotelDTOs = new ArrayList<HotelDTO>();
            Hotel hotel = tour.getHotel();
            HotelDTO hotelDTO = hotelService.findById(hotel.getId());

            // hotelDTOs = hotels.stream()
            //     .map(hotel -> hotelService.findById(hotel.getId()))
            //     .collect(Collectors.toList());

            Image image = tour.getImage();
            ImageDTO imageDTO = StringUtils.createImageDTO(image);

            TourDTO tourDTO =  StringUtils.createTourDTO(tour, imageDTO, hotelDTO);
            return Optional.of(tourDTO);
        }
        return Optional.empty();
    }

    @Transactional
    public TourDTO updateTourBooked(Integer tourId, Integer newBookedValue) {

        Optional<Tour> optTour = tourRepository.findById(tourId);
        Tour tour = optTour.orElseThrow(() -> new NotFoundException("Không tìm thấy tour"));
        tour.setBooked(newBookedValue);
        Tour tourDB = tourRepository.save(tour);
        Image image = tourDB.getImage();
        ImageDTO imageDTO = null;
        if (image != null) {
            imageDTO = StringUtils.createImageDTO(image);
        }

        // List<Hotel> hotels = tourDB.getHotels();
        // List<HotelDTO> hotelDTOs = new ArrayList<>();

        Hotel hotel = tourDB.getHotel();
        HotelDTO hotelDTO = hotelService.findById(hotel.getId());

        // hotelDTOs = hotels.stream()
        //         .map(hotel -> hotelService.findById(hotel.getId()))
        //         .collect(Collectors.toList());

        TourDTO tourDTO =  StringUtils.createTourDTO(tourDB, imageDTO, hotelDTO);
        
        return tourDTO;
    }

    @Transactional
    public TourDTO updateTourActive(Integer tourId, boolean active) {
        
        Optional<Tour> optTour = tourRepository.findById(tourId);
        Tour tour = optTour.orElseThrow(() -> new NotFoundException("Không tìm thấy tour"));
        tour.setActive(active);
        Tour tourDB = tourRepository.save(tour);
        Image image = tourDB.getImage();
        ImageDTO imageDTO = null;
        if (image != null) {
            imageDTO = StringUtils.createImageDTO(image);
        }

        // List<Hotel> hotels = tourDB.getHotels();
        // List<HotelDTO> hotelDTOs = new ArrayList<>();

        Hotel hotel = tourDB.getHotel();
        HotelDTO hotelDTO = hotelService.findById(hotel.getId());

        // hotelDTOs = hotels.stream()
        //     .map(hotel -> hotelService.findById(hotel.getId()))
        //     .collect(Collectors.toList());

        TourDTO tourDTO =  StringUtils.createTourDTO(tourDB, imageDTO, hotelDTO);

        return tourDTO;
    }

    @Transactional
    public TourDTO update(Tour tour, Integer tour_id, String image_name) {

        Optional<Tour> optTour = tourRepository.findById(tour_id);
        Tour tourDB = optTour.orElseThrow(() -> new NotFoundException("Không tìm thấy tour"));

        // Optional<Festival> optFestival = festivalRepositoty.findById(tour.getFestival().getId());
        // Festival festival = optFestival.orElseThrow(() -> new NotFoundException("Không tìm thấy Lễ hội"));

        //List<Hotel> hotels = tour.getHotels();
        Hotel hotel = tour.getHotel();
        Optional<Hotel> optHotel = hotelRepository.findById(hotel.getId());
        Hotel hotelDB = optHotel.orElseThrow(() -> new NotFoundException("Khách sạn không tồn tại"));
        HotelDTO hotelDTO = hotelService.findById(hotelDB.getId());

        // List<Hotel> hotels_save = new ArrayList<Hotel>();
        // List<HotelDTO> hotelDTOs = new ArrayList<HotelDTO>();

        // hotels_save = hotels.stream()
        //     .map(hotel -> {

        //         Optional<Hotel> optHotel = hotelRepository.findById(hotel.getId());
        //         Hotel hotelDB = optHotel.orElseThrow(() -> new NotFoundException("Khách sạn không tồn tại id "));
           
        //         HotelDTO hotelDTO = hotelService.findById(hotel.getId());
        //         hotelDTOs.add(hotelDTO);
        //         return hotelDB;
        //     })
        //     .collect(Collectors.toList());

        Optional<Image> new_image = imageService.findByName(image_name);
        Image imageUpdate = null;
        ImageDTO imageDTO = null;
        //Tour tourDB = optTour.get();
        if (!new_image.isPresent()) {
            imageUpdate = tourDB.getImage();  //Lấy lại ảnh cũ nếu ko update ảnh mới
            imageDTO = StringUtils.createImageDTO(imageUpdate);
        }
        else {
            imageUpdate = new_image.get();
            imageDTO = StringUtils.createImageDTO(imageUpdate);
        }
        
        tourDB.setName(tour.getName());
        tourDB.setFromWhere(tour.getFromWhere());
        tourDB.setDescription(tour.getDescription());
        tourDB.setToWhere(tour.getToWhere());
        tourDB.setFromDate(tour.getFromDate());
        tourDB.setToDate(tour.getToDate());
        tourDB.setPriceAdult(tour.getPriceAdult());
        tourDB.setPriceChild(tour.getPriceChild());
        tourDB.setPriceBaby(tour.getPriceBaby());
        tourDB.setCapacity(tour.getCapacity());
        tourDB.setBooked(tour.getBooked());
        //tourDB.setFestival(festival);
        //tourDB.setHotels(hotels_save);
        tourDB.setHotel(hotelDB);
        tourDB.setImage(imageUpdate);

        Tour tour_save = tourRepository.save(tourDB);
        TourDTO tourDTO =  StringUtils.createTourDTO(tour_save, imageDTO, hotelDTO);
        return tourDTO;
    }


    @Transactional
    public List<TourDTO> findByCondition(
        String toWhere, 
        LocalDate fromDate, 
        LocalDate toDate, 
        Integer priceAdult
    ) {

        List<Tour> tours = tourRepository.findByToWhereAndFromDateGreaterThanEqualAndToDateLessThanEqualAndPriceAdultLessThanEqual(toWhere, fromDate, toDate, priceAdult);
        
        //List<Tour> tours = tourRepository.findAll();
        List<TourDTO> tourDTOs = new ArrayList<>();

        tourDTOs = tours.stream()
            .map(tour -> {

                // List<HotelDTO> hotelDTOs = tour.getHotels().stream()
                //     .map(hotel -> hotelService.findById(hotel.getId()))
                //     .collect(Collectors.toList());

                HotelDTO hotelDTO = hotelService.findById(tour.getHotel().getId());

                Image image = tour.getImage();
                ImageDTO imageDTO = StringUtils.createImageDTO(image);

                return StringUtils.createTourDTO(tour, imageDTO, hotelDTO);    
            })
            .collect(Collectors.toList());

        return tourDTOs;
    }
    

    public void delete(Integer id) {
        tourRepository.deleteById(id);
    }

}
