package com.cit.festival.payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cit.festival.booktour.BookedTour;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT p FROM Payment p WHERE p.vnp_TxnRef = :vnpTxnRef")
    Optional<Payment> findByVnpTxnRef(@Param("vnpTxnRef") String vnpTxnRef);

    @Query("SELECT p FROM Payment p JOIN p.bookedTour bt WHERE bt.id = :bookedTourId")
    Optional<Payment> findByBookedTourId(@Param("bookedTourId") Integer bookedTourId);

    // @Query("SELECT p FROM Payment p WHERE p.dateOfCheckout BETWEEN :fromDate AND :toDate")
    @Query("SELECT p FROM Payment p WHERE DATE(p.dateOfCheckout) BETWEEN :fromDate AND :toDate")
    List<Payment> findAllPaymentsInDateRange(
        @Param("fromDate") LocalDate fromDate, 
        @Param("toDate") LocalDate toDate);
}

