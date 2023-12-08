package com.cit.festival.booktour;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cit.festival.StringUtils;
import com.cit.festival.exception.BookedTourException;
import com.cit.festival.exception.NotFoundException;
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
    
    private final BookedTourRepository bookedTourRepository;
    private final PaymentRepository paymentRepository;
    private final TouristService touristService;
    private final TourService tourService;
    private final RoomService roomService;

    public BookedTourService(
        BookedTourRepository bookedTourRepository,
        PaymentRepository paymentRepository,
        TouristService touristService,
        TourRepository tourRepository,
        TourService tourService,
        RoomService roomService
    ) {
        this.bookedTourRepository = bookedTourRepository;
        this.paymentRepository = paymentRepository;
        this.touristService = touristService;
        this.tourService = tourService;
        this.roomService = roomService;
    }

    @Transactional
    public BookedTourDTO add(BookedTour bookedTour) {

        Optional<TourDTO> optTour = tourService.findById(bookedTour.getTour().getId());
        TourDTO tourDTO = optTour.orElseThrow(() -> new NotFoundException("Không tìm thấy tour"));

        TouristDTO touristDTO = touristService.findById(bookedTour.getTourist().getId());

        if (touristDTO == null) {
            throw new NotFoundException("Không tìm thấy người đặt" );
        }

        // bookedTourRepository
        //     .findByTourIdAndTouristIdAndIsDeletedFalse(tourDTO.getId(), touristDTO.getId())
        //     .orElseThrow(() -> new NotFoundException("Bạn đã đặt tour này, vui lòng kiểm tra lại"));
        Optional<BookedTour> bookedTourDBCheck = bookedTourRepository.findByTourIdAndTouristIdAndIsDeletedFalse(tourDTO.getId(), touristDTO.getId());
        if (bookedTourDBCheck.isPresent()) {
            throw new NotFoundException("Bạn đã đặt tour này, vui lòng kiểm tra lại");
        }

        int bookedAdult = bookedTour.getBookedAdult();
        int bookedChild = bookedTour.getBookedChild();
        int bookedBaby = bookedTour.getBookedBaby();

        int bookedTotal = bookedAdult + bookedChild + bookedBaby;

        int capacity = tourDTO.getCapacity();
        int currentBooked = tourDTO.getBooked();

        int canBooked = capacity - currentBooked;

        if (bookedTotal > capacity || bookedTotal + currentBooked > capacity) {
            throw new BookedTourException("Xin lỗi quý khách, số chỗ hiện tại chỉ còn " + canBooked );
        }

        TourDTO tourUpdate = tourService.updateTourBooked(tourDTO.getId(), bookedTotal + currentBooked);
        //RoomDTO roomDTO = roomService.findById(bookedTour.getRoom().getId());

        bookedTour.setDeleted(false);
        BookedTour bookedTourDB = bookedTourRepository.save(bookedTour);
        
        BookedTourDTO bookedTourDTO = StringUtils
            .createBookedTourDTO(bookedTourDB, tourUpdate);

        return bookedTourDTO;
    }

    @Transactional
    public List<BookedTourDTO> findAll() {
        List<BookedTour> bookedTours = bookedTourRepository.findAllByIsDeletedFalse();
        List<BookedTourDTO> bookedTourDTOs = new ArrayList<>();

        bookedTourDTOs = bookedTours.stream()
            .map(bookedTour -> {
                Optional <TourDTO> optTourDTO = tourService.findById(bookedTour.getTour().getId());
                //RoomDTO roomDTO = roomService.findById(bookedTour.getRoom().getId());

                Integer touristId;
                if (bookedTour.getTourist() != null) {
                    touristId = bookedTour.getTourist().getId();
                }
                else {
                    touristId = null;
                }
                return StringUtils.createBookedTourDTO(bookedTour, optTourDTO.get());
            })
            .collect(Collectors.toList());

        return bookedTourDTOs;
    }

    @Transactional
    public List<BookedTourDTO> findAllByTouristId(Integer touristId) {

        List<BookedTour> bookedTours = bookedTourRepository.findAllByTouristIdAndIsDeletedFalse(touristId);
        List<BookedTourDTO> bookedTourDTOs = new ArrayList<>();

        bookedTourDTOs = bookedTours.stream()
            .map(bookedTour -> {

                Optional <TourDTO> optTourDTO = tourService.findById(bookedTour.getTour().getId());
                //RoomDTO roomDTO = roomService.findById(bookedTour.getRoom().getId());
                Integer tourist_id = bookedTour.getTourist().getId();

                // Integer tourist_id;
                // if (bookedTour.getTourist() != null) {
                //     tourist_id = bookedTour.getTourist().getId();
                // }
                // else {
                //     tourist_id = null;
                // }
                
                return StringUtils
                .createBookedTourDTO(bookedTour, optTourDTO.get());    
            })
            .collect(Collectors.toList());

        return bookedTourDTOs;
    }

    @Transactional
    public BookedTourDTO updateBookedTourCheckout(Integer bookedtourId,  boolean isCheckout) {
        
        Optional<BookedTour> optBookedTour = bookedTourRepository.findById(bookedtourId);

        BookedTour bookedTour = optBookedTour.orElseThrow(() -> new NotFoundException("Không tìm thấy booked tour"));
        bookedTour.setIsCheckout(isCheckout);
        BookedTour bookedTourDB = bookedTourRepository.save(bookedTour);

        Optional <TourDTO> optTourDTO = tourService.findById(bookedTourDB.getTour().getId());
        //RoomDTO roomDTO = roomService.findById(bookedTour.getRoom().getId());
        
        Integer touristId = null;
        if (bookedTour.getTourist() != null) {
            touristId = bookedTour.getTourist().getId();
        }
        
        BookedTourDTO bookedTourDTO = StringUtils
            .createBookedTourDTO(bookedTour, optTourDTO.get());
        return bookedTourDTO;
    }

    @Transactional
    public BookedTourDTO updateBookedTourStatus(Integer bookedtour_id,  Integer status) {

        Optional<BookedTour> optBookedTour = bookedTourRepository.findById(bookedtour_id);
        BookedTour bookedTour = optBookedTour.orElseThrow(() -> new NotFoundException("Không tìm thấy booked tour"));
        
        bookedTour.setStatus(status);
        BookedTour bookedTourDB = bookedTourRepository.save(bookedTour);
        Optional <TourDTO> optTourDTO = tourService.findById(bookedTourDB.getTour().getId());
        //RoomDTO roomDTO = roomService.findById(bookedTour.getRoom().getId());

        // Integer tourist_id = null;
        // if (bookedTourDB.getTourist() != null) {
        //     tourist_id = bookedTourDB.getTourist().getId();
        // }

        BookedTourDTO bookedTourDTO = StringUtils
            .createBookedTourDTO(bookedTourDB, optTourDTO.get());
        return bookedTourDTO;
    }

    public List<BookedTourDTO> deleteById(Integer id, Integer touristId) {
        Optional<BookedTour> optBookedTour = bookedTourRepository.findById(id);
        BookedTour bookedTour = optBookedTour.orElseThrow(() -> new NotFoundException("Không tìm thấy tour đã đặt"));
     
        // paymentRepository
        //     .findByBookedTourId(id)
        //     .orElseThrow(() -> new BookedTourException("Tour này đã được thanh toán"));


        Optional<TourDTO> tourDB = tourService.findById(bookedTour.getTour().getId());
        Integer bookedAdult = bookedTour.getBookedAdult();
        Integer bookedChild = bookedTour.getBookedChild();
        Integer bookedBaby = bookedTour.getBookedBaby();
        Integer bookedTotal = bookedAdult + bookedChild + bookedBaby;

        Integer new_booked = tourDB.get().getBooked() - bookedTotal;

        tourService.updateTourBooked(bookedTour.getTour().getId(), new_booked);
        bookedTour.setDeleted(true);
        //bookedTourRepository.deleteById(id);
        bookedTourRepository.save(bookedTour);
        return findAllByTouristId(touristId);
    }

    public List<BookedTourDTO> adminDeleteById(Integer id) {

        Optional<BookedTour> optBookedTour = bookedTourRepository.findById(id);
        BookedTour bookedTour = optBookedTour.orElseThrow(() -> new NotFoundException("Không tìm thấy tour đã đặt"));

        Optional<TourDTO> tourDB = tourService.findById(bookedTour.getTour().getId());
        Integer bookedAdult = bookedTour.getBookedAdult();
        Integer bookedChild = bookedTour.getBookedChild();
        Integer bookedBaby = bookedTour.getBookedBaby();
        Integer bookedTotal = bookedAdult + bookedChild + bookedBaby;
        Integer new_booked = tourDB.get().getBooked() - bookedTotal;

        tourService.updateTourBooked(bookedTour.getTour().getId(), new_booked);

        Optional<Payment> optPayment = paymentRepository.findByBookedTourId(id);
        if (optPayment.isPresent()) {
            Payment payment = optPayment.get();
            payment.setBookedTour(null);
            paymentRepository.save(payment);
        }
        bookedTourRepository.deleteById(id);
        return findAll();
    }

    public BookedTourDTO findById(Integer id) {
        Optional<BookedTour> optBookedTour = bookedTourRepository.findById(id);
        BookedTour bookedTour = optBookedTour.orElseThrow(() -> new NotFoundException("Không tìm thấy tour đã đặt"));

        // Integer tourist_id = null;
        // if (bookedTour.getTourist() != null) {
        //     tourist_id = bookedTour.getTourist().getId();
        // }

        Optional <TourDTO> optTourDTO = tourService.findById(bookedTour.getTour().getId());
        //RoomDTO roomDTO = roomService.findById(bookedTour.getRoom().getId());

        BookedTourDTO bookedTourDTO = StringUtils
            .createBookedTourDTO(bookedTour, optTourDTO.get());
        
        return bookedTourDTO;
    }

}
