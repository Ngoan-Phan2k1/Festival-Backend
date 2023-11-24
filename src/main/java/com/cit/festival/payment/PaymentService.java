package com.cit.festival.payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cit.festival.StringUtils;
import com.cit.festival.booktour.BookedTour;
import com.cit.festival.booktour.BookedTourDTO;
import com.cit.festival.booktour.BookedTourRepository;
import com.cit.festival.booktour.BookedTourService;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.exception.PaymentException;
import com.cit.festival.hotel.HotelRepository;
import com.cit.festival.image.ImageService;
import com.cit.festival.room.RoomRepository;
import com.cit.festival.tour.TourDTO;
import com.cit.festival.tour.TourService;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookedTourService bookedTourService;
    private final TourService tourService;
    private final BookedTourRepository bookedTourRepository;

    public PaymentService(
        PaymentRepository paymentRepository,
        BookedTourService bookedTourService,
        TourService tourService,
        BookedTourRepository bookedTourRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.bookedTourService = bookedTourService;
        this.tourService = tourService;
        this.bookedTourRepository = bookedTourRepository;
    }

    public Payment add(Payment payment) {

        BookedTour bookedtour = bookedTourRepository
            .findById(payment.getBookedTour().getId())
            .orElseThrow(() -> new NotFoundException("Booked tour không tồn tại"));

        paymentRepository
            .findByVnpTxnRef(payment.getVnp_TxnRef())
            .orElseThrow(() -> new PaymentException("Thanh toán không hợp lệ"));
                        
        paymentRepository
            .findByBookedTourId(payment.getBookedTour().getId()) //Kiểm tra bảng payment bookedtourId đã được thanh toán chưa
            .orElseThrow(() -> new PaymentException("Thanh toán không hợp lệ"));
        
        bookedTourService.updateBookedTourCheckout(bookedtour.getId(), true);
        bookedTourService.updateBookedTourStatus(bookedtour.getId(), 2);
        return paymentRepository.save(payment);
    }

    public List<PaymentResponseDTO> findAll() {

        List<Payment> payments = paymentRepository.findAll();
        List<PaymentResponseDTO> paymentResponseDTOs = new ArrayList<>();

        paymentResponseDTOs = payments.stream()
            .map(payment -> {

                Optional <TourDTO> tourDTO = tourService
                    .findById(payment.getBookedTour().getTour().getId());

                return StringUtils.createPaymentResponseDTO(payment, tourDTO.get());    
            })
            .collect(Collectors.toList());
        return paymentResponseDTOs;
    }

    @Transactional
    public List<PaymentResponseDTO> findInDateRange(
        LocalDate fromDate, 
        LocalDate toDate
    ) {

        if (fromDate.isAfter(toDate)) {
            throw new PaymentException("Vui lòng chọn ngày hợp lệ.");
        }

        List<Payment> payments = paymentRepository.findAllPaymentsInDateRange(fromDate, toDate);
        List<PaymentResponseDTO> paymentResponseDTOs = new ArrayList<>();

        paymentResponseDTOs = payments.stream()
            .map(payment -> {

                Optional <TourDTO> tourDTO = tourService
                    .findById(payment.getBookedTour().getTour().getId());

                return StringUtils.createPaymentResponseDTO(payment, tourDTO.get());
            })
            .collect(Collectors.toList());
        return paymentResponseDTOs;
    }
    
}
