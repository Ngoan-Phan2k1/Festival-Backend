package com.cit.festival.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cit.festival.config.JwtService;
import com.cit.festival.role.RoleEnum;
import com.cit.festival.tourist.Tourist;
import com.cit.festival.tourist.TouristRepository;
import com.cit.festival.user.User;
import com.cit.festival.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TouristRepository touristRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AuthenticationResponse register(RegisterRequest request) {
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
                .phone(request.getPhone())
                .user(user)
                .build();
        touristRepository.save(tourist);
        
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //User login bằng username và password
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                //request.getEmail(),
                request.getUsername(),
                request.getPassword()
            )
        );
        var user = userRepository.findByUsername(request.getUsername())
            .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
