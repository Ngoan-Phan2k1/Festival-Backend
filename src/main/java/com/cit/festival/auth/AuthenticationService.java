package com.cit.festival.auth;

import java.util.Date;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cit.festival.config.JwtService;
import com.cit.festival.config.SecurityConstant;
import com.cit.festival.exception.AuthenticationException;
import com.cit.festival.exception.NotFoundException;
import com.cit.festival.role.RoleEnum;
import com.cit.festival.tourist.Tourist;
import com.cit.festival.tourist.TouristRepository;
import com.cit.festival.user.User;
import com.cit.festival.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TouristRepository touristRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {

        var userDB = userRepository.findByUsername(request.getUsername());
        if (userDB.isPresent()) {
            throw new AuthenticationException("Tên đăng nhập đã tồn tại");
        }



        var user = User.builder()
        // .firstname(request.getFirstname())
        // .lastname(request.getLastname())
        // .email(request.getEmail())
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(RoleEnum.USER)
        .build();
        userRepository.save(user);

        Tourist tourist = Tourist.builder()
                .fullname(request.getFullname())
                .email(request.getEmail())
                //.phone(request.getPhone())
                .user(user)
                .build();
        var touristDB = touristRepository.save(tourist);

        
        var jwtToken = jwtService.generateToken(user);
        Date tokenExpirationDate = jwtService.extractExpiration(jwtToken);
        
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .touristId(touristDB.getId())
            .username(request.getUsername())
            .fullname(request.getFullname())
            .tokenExpirationDate(SecurityConstant.JWT_EXPIRATION)
            .build();
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // var user = userRepository.findByUsername(request.getUsername())
        //     .orElseThrow(() -> new AuthenticationException("Vui lòng kiểm tra lại tài khoản và mật khẩu"));
       
        var user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new AuthenticationException("Vui lòng kiểm tra lại tài khoản và mật khẩu"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Vui lòng kiểm tra lại tài khoản và mật khẩu");
        }
        

        //User login bằng username và password
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                //request.getEmail(),
                request.getUsername(),
                request.getPassword()
            )
        );
        
        var jwtToken = jwtService.generateToken(user);
        Date tokenExpirationDate = jwtService.extractExpiration(jwtToken);

        var touristDB = touristRepository.findTouristByUserName(user.getUsername());

        return AuthenticationResponse.builder()
        .token(jwtToken)
        .touristId(touristDB.getId())
        .fullname(touristDB.getFullname())
        .username(user.getUsername())
        .email(touristDB.getEmail())
        .tokenExpirationDate(SecurityConstant.JWT_EXPIRATION)
        .build();
        
        
    }
}
