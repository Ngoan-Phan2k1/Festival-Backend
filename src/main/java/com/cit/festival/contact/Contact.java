package com.cit.festival.contact;

import com.cit.festival.room.Room;
import com.cit.festival.tourist.Tourist;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Contact {
    
    @Id // Đánh dấu đây là ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // trường tăng tự động
    private Integer id;

    @NotNull(message = "FullName cannot be null")
    private String fullname;

    @NotNull(message = "Email cannot be empty")
    @Email(message = "Email invalid")
    private String email;

    @NotEmpty(message = "Phone cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "tourist_id", nullable = false) // Tên cột khoá ngoại
    @NotNull(message = "Tourist cannot be null")
    private Tourist tourist;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false) // Tên cột khoá ngoại
    @NotNull(message = "Room cannot be null")
    private Room room;
}
