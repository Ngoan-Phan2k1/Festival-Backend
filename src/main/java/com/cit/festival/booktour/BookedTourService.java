package com.cit.festival.booktour;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cit.festival.exception.BookedTourException;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.festival.Festival;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.payment.Payment;
import com.cit.festival.payment.PaymentRepository;
import com.cit.festival.room.RoomDTO;
import com.cit.festival.room.RoomService;
import com.cit.festival.tour.Tour;
import com.cit.festival.tour.TourDTO;
import com.cit.festival.tour.TourRepository;
import com.cit.festival.tour.TourService;
import com.cit.festival.tourist.Tourist;
import com.cit.festival.tourist.TouristDTO;
import com.cit.festival.tourist.TouristService;

import jakarta.transaction.Transactional;

@Service
public class BookedTourService {
    
    @Autowired
    private BookedTourRepository bookedTourRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TouristService touristService;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourService tourService;

    @Autowired
    private RoomService roomService;

    public Optional<BookedTour> findById(Integer id) {
        return bookedTourRepository.findById(id);
    }

    @Transactional
    public BookedTourDTO add(BookedTour bookedTour) {

        TourDTO tour = tourService.findById(bookedTour.getTour().getId());
        //Optional<Tourist> tourist = touristService.findById(bookedTour.getTourist().getId());
        TouristDTO touristDTO = touristService.findById(bookedTour.getTourist().getId());

        if (tour == null) {
            throw new NotFoundException("Không tìm thấy tour" );
        }

        if (touristDTO == null) {
            throw new NotFoundException("Không tìm thấy người đặt" );
        }

        Optional<BookedTour> existBooked = bookedTourRepository.findByTourIdAndTouristId(tour.getId(), touristDTO.getId());
        if (existBooked.isPresent()) {
            throw new BookedTourException("Bạn đã đặt tour này, vui lòng kiểm tra lại");
        }

        int bookedAdult = bookedTour.getBookedAdult();
        int bookedChild = bookedTour.getBookedChild();
        int bookedBaby = bookedTour.getBookedBaby();

        int bookedTotal = bookedAdult + bookedChild + bookedBaby;

        int capacity = tour.getCapacity();
        int currentBooked = tour.getBooked();

        int canBooked = capacity - currentBooked;

        if (bookedTotal > capacity || bookedTotal + currentBooked > capacity) {
            throw new BookedTourException("Xin lỗi quý khách, số chỗ hiện tại chỉ còn " + canBooked );
        }

        TourDTO tourUpdate = tourService.updateTourBooked(tour.getId(), bookedTotal + currentBooked);
        RoomDTO roomDTO = roomService.findById(bookedTour.getRoom().getId());
        BookedTour bookedTourDB = bookedTourRepository.save(bookedTour);
        BookedTourDTO bookedTourDTO = new BookedTourDTO(
            bookedTourDB.getId(),
            bookedTourDB.getBookedAdult(),
            bookedTourDB.getBookedChild(),
            bookedTourDB.getBookedBaby(),
            bookedTourDB.getFullname(),
            bookedTourDB.getEmail(),
            bookedTourDB.getAddress(),
            bookedTour.getNote(),
            bookedTour.getNum_room(),
            bookedTourDB.getPhone(),
            bookedTourDB.getIsCheckout(),
            bookedTourDB.getDateOfBooking(),
            tourUpdate,
            roomDTO
        );

        return bookedTourDTO;
    }

    public List<BookedTourDTO> findAll() {
        List<BookedTour> bookedTours = bookedTourRepository.findAll();
        List<BookedTourDTO> bookedTourDTOs = new ArrayList<>();
        for (BookedTour bookedTour: bookedTours) {
            TourDTO tourDTO = tourService.findById(bookedTour.getTour().getId());
            RoomDTO roomDTO = roomService.findById(bookedTour.getRoom().getId());

             BookedTourDTO bookedTourDTO = new BookedTourDTO(
                bookedTour.getId(),
                bookedTour.getBookedAdult(),
                bookedTour.getBookedChild(),
                bookedTour.getBookedBaby(),
                bookedTour.getFullname(),
                bookedTour.getEmail(),
                bookedTour.getAddress(),
                bookedTour.getNote(),
                bookedTour.getNum_room(),
                bookedTour.getPhone(),
                bookedTour.getIsCheckout(),
                bookedTour.getDateOfBooking(),
                tourDTO,
                roomDTO
            );
            
            bookedTourDTOs.add(bookedTourDTO);
        }
        return bookedTourDTOs;
    }

    @Transactional
    public List<BookedTourDTO> findAllByTouristId(Integer touristId) {

        List<BookedTour> bookedTours = bookedTourRepository.findAllByTouristId(touristId);
        List<BookedTourDTO> bookedTourDTOs = new ArrayList<>();

        for (BookedTour bookedTour: bookedTours) {

            // Image image = bookedTour.getTour().getImage();
            // ImageDTO imageDTO = null;
            // if (image != null) {
            //     imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
            // }
            TourDTO tourDTO = tourService.findById(bookedTour.getTour().getId());
            RoomDTO roomDTO = roomService.findById(bookedTour.getRoom().getId());
            // TourDTO tourDTO = new TourDTO();
            // tourDTO.setId(bookedTour.getTour().getId());
            // tourDTO.setFestival_id(bookedTour.getTour().getFestival().getId());
            // tourDTO.setName(bookedTour.getTour().getName());
            // tourDTO.setFromWhere(bookedTour.getTour().getFromWhere());
            // tourDTO.setToWhere(bookedTour.getTour().getToWhere());
            // tourDTO.setDescription(bookedTour.getTour().getDescription());
            // tourDTO.setFromDate(bookedTour.getTour().getFromDate());
            // tourDTO.setToDate(bookedTour.getTour().getToDate());
            // tourDTO.setPrice(bookedTour.getTour().getPrice());
            // tourDTO.setCapacity(bookedTour.getTour().getCapacity());
            // tourDTO.setBooked(bookedTour.getTour().getBooked());
            // tourDTO.setImageDTO(imageDTO);

            BookedTourDTO bookedTourDTO = new BookedTourDTO(
                bookedTour.getId(),
                bookedTour.getBookedAdult(),
                bookedTour.getBookedChild(),
                bookedTour.getBookedBaby(),
                bookedTour.getFullname(),
                bookedTour.getEmail(),
                bookedTour.getAddress(),
                bookedTour.getNote(),
                bookedTour.getNum_room(),
                bookedTour.getPhone(),
                bookedTour.getIsCheckout(),
                bookedTour.getDateOfBooking(),
                tourDTO,
                roomDTO
            );
            
            bookedTourDTOs.add(bookedTourDTO);

        }

        return bookedTourDTOs;
    }

    @Transactional
    public BookedTourDTO updateBookedTourCheckout(Integer bookedtourId,  boolean isCheckout) {
        BookedTour bookedTour = bookedTourRepository.findById(bookedtourId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booked tour"));

        bookedTour.setIsCheckout(isCheckout);
        BookedTour bookedTourDB = bookedTourRepository.save(bookedTour);

        TourDTO tourDTO = tourService.findById(bookedTourDB.getTour().getId());
        RoomDTO roomDTO = roomService.findById(bookedTour.getRoom().getId());

        // Image image = bookedTourDB.getTour().getImage();
        // ImageDTO imageDTO = null;
        // if (image != null) {
        //     imageDTO = new ImageDTO(image.getId(), image.getName(), image.getType());
        // }
        
        // TourDTO tourDTO = new TourDTO();
        // tourDTO.setId(bookedTourDB.getTour().getId());
        // tourDTO.setFestival_id(bookedTourDB.getTour().getFestival().getId());
        // tourDTO.setName(bookedTourDB.getTour().getName());
        // tourDTO.setFromWhere(bookedTourDB.getTour().getFromWhere());
        // tourDTO.setToWhere(bookedTourDB.getTour().getToWhere());
        // tourDTO.setDescription(bookedTourDB.getTour().getDescription());
        // tourDTO.setFromDate(bookedTourDB.getTour().getFromDate());
        // tourDTO.setToDate(bookedTourDB.getTour().getToDate());
        // tourDTO.setPrice(bookedTourDB.getTour().getPrice());
        // tourDTO.setPriceAdult();
        // tourDTO.setCapacity(bookedTourDB.getTour().getCapacity());
        // tourDTO.setBooked(bookedTourDB.getTour().getBooked());
        // tourDTO.setImageDTO(imageDTO);

        BookedTourDTO bookedTourDTO = new BookedTourDTO(
            bookedTourDB.getId(),
            bookedTour.getBookedAdult(),
            bookedTour.getBookedChild(),
            bookedTour.getBookedBaby(),
            bookedTour.getFullname(),
            bookedTour.getEmail(),
            bookedTour.getAddress(),
            bookedTour.getNote(),
            bookedTour.getNum_room(),
            bookedTourDB.getPhone(),
            bookedTourDB.getIsCheckout(),
            bookedTourDB.getDateOfBooking(),
            tourDTO,
            roomDTO
        );

        return bookedTourDTO;
    }

    public List<BookedTourDTO> deleteById(Integer id, Integer touristId) {
        Optional<BookedTour> bookedTour = bookedTourRepository.findById(id);
        if (!bookedTour.isPresent()) {
            throw new NotFoundException("Không tìm thấy tour đã đặt");
        }
        Optional<Payment> payment = paymentRepository.findByBookedTourId(id);
        if (payment.isPresent()) {
            throw new BookedTourException("Tour này đã được thanh toán");
        }

        TourDTO tourDB = tourService.findById(bookedTour.get().getTour().getId());
        Integer bookedAdult = bookedTour.get().getBookedAdult();
        Integer bookedChild = bookedTour.get().getBookedChild();
        Integer bookedBaby = bookedTour.get().getBookedBaby();
        Integer bookedTotal = bookedAdult + bookedChild + bookedBaby;

        Integer new_booked = tourDB.getBooked() - bookedTotal;

        tourService.updateTourBooked(bookedTour.get().getTour().getId(), new_booked);
        bookedTourRepository.deleteById(id);
        return findAllByTouristId(touristId);
    }

}
