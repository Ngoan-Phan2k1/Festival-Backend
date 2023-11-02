package com.cit.festival.payment;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cit.festival.booktour.BookedTour;
import com.cit.festival.booktour.BookedTourService;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.exception.PaymentException;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookedTourService bookedTourService;

    public Payment add(Payment payment) {
        Optional<BookedTour> bookedtour = bookedTourService.findById(payment.getBookedTour().getId());
        Optional<Payment> paymentDB = paymentRepository.findByVnpTxnRef(payment.getVnp_TxnRef());

        if (paymentDB.isPresent()) { //Kiểm tra mã hóa đơn vnpay đã có trong db chưa
            throw new PaymentException("Thanh toán không hợp lệ");
        }

        Optional<Payment> paymentBookedTourId = paymentRepository.findByBookedTourId(payment.getBookedTour().getId());

        if (paymentBookedTourId.isPresent()) { //Kiểm tra bảng payment bookedtourId đã được thanh toán chưa
            throw new PaymentException("Thanh toán không hợp lệ");
        }

        if (!bookedtour.isPresent()) { //Kiểm tra bảng bookedtour đã có booked này chưa
            throw new NotFoundException("Booked tour không tồn tại");
        }
        bookedTourService.updateBookedTourCheckout(bookedtour.get().getId(), true);
        return paymentRepository.save(payment);
    }
    
}
