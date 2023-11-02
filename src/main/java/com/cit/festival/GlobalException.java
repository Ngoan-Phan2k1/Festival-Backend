package com.cit.festival;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cit.festival.exception.AuthenticationException;
import com.cit.festival.exception.BookedTourException;
import com.cit.festival.exception.HotelException;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.exception.PaymentException;
import com.cit.festival.exception.RoomException;
import com.cit.festival.exception.TourException;

@RestControllerAdvice
public class GlobalException {

    //Xử lý ngoại lệ field không hợp lệ
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public Map<String, String> handleValidationExceptions(
    // MethodArgumentNotValidException ex) {
    //     Map<String, String> errors = new HashMap<>();
    //     ex.getBindingResult().getAllErrors().forEach((error) -> {
    //         String fieldName = ((FieldError) error).getField();
    //         String errorMessage = error.getDefaultMessage();
    //         errors.put(fieldName, errorMessage);
    //     });
    //     return errors;
    // }
    
    //Xử lý ngoại lệ field không hợp lệ
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        ErrorResponse errorMessage = new ErrorResponse("Validation failed", details, HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString()));
    }

    @ExceptionHandler(BookedTourException.class)
    public ResponseEntity<ErrorResponse> handleBookedTourException(BookedTourException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePaymentException(PaymentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString()));
    }

    @ExceptionHandler(HotelException.class)
    public ResponseEntity<ErrorResponse> handleHotelException(HotelException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString()));
    }

    @ExceptionHandler(RoomException.class)
    public ResponseEntity<ErrorResponse> handleRoomException(RoomException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString()));
    }

    @ExceptionHandler(TourException.class)
    public ResponseEntity<ErrorResponse> handleRoomException(TourException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString()));
    }

    //Xử lý bất cứ ngoại lệ nào khác
    // @ExceptionHandler
    // public ResponseEntity<ErrorResponse> handleException(Exception exc) {

    //     ErrorResponse error = new ErrorResponse();
    //     error.setStatus(HttpStatus.BAD_REQUEST.value());
    //     error.setMessage(exc.getMessage());
    //     error.setTimeStamp(System.currentTimeMillis());
    //     return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    // }
    
}
