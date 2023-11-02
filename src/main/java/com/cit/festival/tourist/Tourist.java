package com.cit.festival.tourist;

import com.cit.festival.image.Image;
import com.cit.festival.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // Tạo bảng trong CSDL
public class Tourist {
    
    @Id // Đánh dấu đây là ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // trường tăng tự động
    private Integer id;

    @NotEmpty(message = "Name cannot be empty")
    private String fullname;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email invalid")
    private String email;

    //@NotEmpty(message = "Phone cannot be empty")
    // @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại không hợp lệ")
    // private String phone;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", nullable = true)
    private Image image;

    @OneToOne
    @JoinColumn(name = "fk_user_id")
    private User user;

}
