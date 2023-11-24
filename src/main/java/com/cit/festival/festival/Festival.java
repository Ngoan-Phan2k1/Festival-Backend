package com.cit.festival.festival;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.cit.festival.tour.Tour;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Festival {

    @Id // Đánh dấu đây là ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // trường tăng tự động
    private Integer id;

    @NotEmpty(message = "Festival name cannot be empty")
    private String name;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "fromDate cannot be null")
    private LocalDate fromDate; // Ngày bắt đầu lễ hội (dương lịch)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "toDate cannot be null")
    private LocalDate toDate;   // Ngày kết thúc lễ hội (dương lịch)

    //Bidirectional
    // @JsonIgnore
    // @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Tour> tours = new ArrayList<>();

    @AssertTrue(message = "fromDate must be before toDate")
    private boolean isFromBeforeTo() {
        // Kiểm tra nếu fromDate không lớn hơn toDate thì trả về true, ngược lại trả về false
        return fromDate == null || toDate == null || fromDate.isBefore(toDate);
    }
    
}
