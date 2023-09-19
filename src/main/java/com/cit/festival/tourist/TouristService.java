package com.cit.festival.tourist;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TouristService {
    
    @Autowired
    private TouristRepository touristRepository;

    public Tourist findByUserName(String username) {
        return touristRepository.findTouristByUserName(username);
    }

    public List<Tourist> findAll() {
        return touristRepository.findAll();
    }
}
