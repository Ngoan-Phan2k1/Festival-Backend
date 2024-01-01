package com.cit.festival;

import java.util.List;

import com.cit.festival.FestivalContent.FestivalContent;
import com.cit.festival.FestivalContent.FestivalContentDTO;
import com.cit.festival.booktour.BookedTour;
import com.cit.festival.booktour.BookedTourDTO;
import com.cit.festival.festival.Festival;
import com.cit.festival.festival.FestivalDTO;
import com.cit.festival.hotel.Hotel;
import com.cit.festival.hotel.HotelDTO;
import com.cit.festival.image.Image;
import com.cit.festival.image.ImageDTO;
import com.cit.festival.payment.Payment;
import com.cit.festival.payment.PaymentResponseDTO;
import com.cit.festival.room.Room;
import com.cit.festival.room.RoomDTO;
import com.cit.festival.schedule.Schedule;
import com.cit.festival.schedule.ScheduleDTO;
import com.cit.festival.tour.Tour;
import com.cit.festival.tour.TourDTO;
import com.cit.festival.tourist.Tourist;
import com.cit.festival.tourist.TouristDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public final class StringUtils {

    public static ImageDTO createImageDTO(
        Image image
    ) {
        return ImageDTO.builder()
            .id(image.getId())
            .name(image.getName())
            .type(image.getType())
            .build();
    }

    public static TourDTO createTourDTO(
        Tour tour,
        ImageDTO imageDTO,
        //List<HotelDTO> hotelDTOs
        HotelDTO hotelDTO,
        FestivalDTO festivalDTO
    ) {
        return TourDTO.builder()
            .id(tour.getId())
            //.festival_id(tour.getFestival().getId())
            .name(tour.getName())
            .fromWhere(tour.getFromWhere())
            .toWhere(tour.getToWhere())
            .description(tour.getDescription())
            .fromDate(tour.getFromDate())
            .toDate(tour.getToDate())
            .priceAdult(tour.getPriceAdult())
            .priceChild(tour.getPriceChild())
            .priceBaby(tour.getPriceBaby())
            .capacity(tour.getCapacity())
            .booked(tour.getBooked())
            .active(tour.getActive())
            .imageDTO(imageDTO)
            //.hotelDTOs(hotelDTOs)
            .hotelDTO(hotelDTO)
            .festivalDTO(festivalDTO)
            .build();
    }
    
    public static BookedTourDTO createBookedTourDTO(
        BookedTour bookedTour, 
        TourDTO tourDTO) 
    {
        return BookedTourDTO.builder()
            .booked_id(bookedTour.getId())
            .tourist_id(bookedTour.getTourist().getId())
            .bookedAdult(bookedTour.getBookedAdult())
            .bookedChild(bookedTour.getBookedChild())
            .bookedBaby(bookedTour.getBookedBaby())
            .fullname(bookedTour.getFullname())
            .email(bookedTour.getEmail())
            .address(bookedTour.getAddress())
            .note(bookedTour.getNote())
            //.num_room(bookedTour.getNum_room())
            .phone(bookedTour.getPhone())
            .isCheckout(bookedTour.getIsCheckout())
            .status(bookedTour.getStatus())
            .dateOfBooking(bookedTour.getDateOfBooking())
            .tourDto(tourDTO)
            //.roomDtO(roomDTO)
            .isDeleted(bookedTour.isDeleted())
            .build();    
    }

    public static HotelDTO createHotelDTO(
        Hotel hotel,
        ImageDTO imageDTO
    ) {
        return HotelDTO.builder()
            .id(hotel.getId())
            .name(hotel.getName())
            .location(hotel.getLocation())
            .introduce(hotel.getIntroduce())
            //.services(hotel.getServices())
            .imageDTO(imageDTO)
            .build();
    }

    public static RoomDTO createRoomDTO(
        Room room,
        HotelDTO hotelDTO,
        ImageDTO imageDTO
    ) {
        return RoomDTO.builder()
            .id(room.getId())
            .hotel_id(room.getHotel().getId())
            .name(room.getName())
            .price(room.getPrice())
            .services(room.getServices())
            .imageDTO(imageDTO)
            .hotelDTO(hotelDTO)
            .build();
    }

    public static TouristDTO createTouristDTO(
        Tourist tourist,
        ImageDTO imageDTO
    ) {
        return TouristDTO.builder()
            .id(tourist.getId())
            .fullname(tourist.getFullname())
            .username(tourist.getUser().getUsername())
            .email(tourist.getEmail())
            .imageDTO(imageDTO)
            .build();
    }

    public static ScheduleDTO createScheduleDTO(
        Schedule schedule,
        ImageDTO imageDTO
    ) {
        return ScheduleDTO.builder()
            .id(schedule.getId())
            .tour_id(schedule.getTour().getId())
            .day(schedule.getDay())
            .morning(schedule.getMorning())
            .evening(schedule.getEvening())
            .night(schedule.getNight())
            .imageDTO(imageDTO)
            .build();
    }

    public static PaymentResponseDTO createPaymentResponseDTO(
        Payment payment,
        TourDTO tourDTO
    ) {
        return PaymentResponseDTO.builder()
                    .id(payment.getId())
                    .amount(payment.getAmount())
                    .dateOfCheckout(payment.getDateOfCheckout())
                    .tourDTO(tourDTO)
                    .build();
    }

    public static FestivalDTO createFestivalDTO(
        Festival festival,
        ImageDTO imageDTO,
        List<FestivalContentDTO> festivalContentDTOs
    ) {
        return FestivalDTO.builder()
                    .id(festival.getId())
                    .blogName(festival.getBlogName())
                    .dateOfPost(festival.getDateOfPost())
                    .imageDTO(imageDTO)
                    .festivalContentDTOs(festivalContentDTOs)
                    .build();
    }

    public static FestivalContentDTO createFestivalContentDTO(
        FestivalContent festivalContent,
        ImageDTO imageDTO
    ) {
        return FestivalContentDTO.builder()
                                .id(festivalContent.getId())
                                .festival_id(festivalContent.getFestival().getId())
                                .name(festivalContent.getName())
                                .content(festivalContent.getContent())
                                .imageDTO(imageDTO)
                                .build();
    }

}
