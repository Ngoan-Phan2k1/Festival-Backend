package com.cit.festival.FestivalContent;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cit.festival.schedule.Schedule;
import com.cit.festival.schedule.ScheduleDTO;
import com.cit.festival.schedule.ScheduleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/festivalContent")
@CrossOrigin("*")
public class FestivalContentResource {

    private final FestivalContentService festivalContentService;

    public FestivalContentResource (
        FestivalContentService festivalContentService
    ) {
        this.festivalContentService = festivalContentService;
    }

    @PostMapping
    public ResponseEntity<FestivalContentDTO> add(
        @RequestBody @Valid FestivalContent festivalContent,
        @RequestParam(value = "imageName", required = false) String imageName
    ) {

        FestivalContentDTO festivalContentDTO = festivalContentService.add(festivalContent, imageName);
        return ResponseEntity.status(HttpStatus.OK).body(festivalContentDTO);
    }

    @GetMapping("festival/{festivalId}")
    public ResponseEntity<List<FestivalContentDTO>> findByFestivalId(
        @PathVariable Integer festivalId
    ) {
        List<FestivalContentDTO> festivalContentDTOs = festivalContentService.findByFestivalId(festivalId);
        return ResponseEntity.status(HttpStatus.OK).body(festivalContentDTOs);
    }

    @GetMapping("/content/{id}")
    public ResponseEntity<FestivalContentDTO> findById(@PathVariable Integer id) {
        FestivalContentDTO festivalContentDTO = festivalContentService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(festivalContentDTO);
    }

    @PutMapping("/{contentId}")
    public ResponseEntity<FestivalContentDTO> updateFestivalContent(
        @PathVariable Integer contentId,
        @RequestBody @Valid FestivalContent festivalContent,
        @RequestParam(value = "imageName", required = false) String imageName
    ) {

        FestivalContentDTO festivalContentDTO = festivalContentService.update(festivalContent, contentId, imageName);
        return ResponseEntity.status(HttpStatus.OK).body(festivalContentDTO);
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<List<FestivalContentDTO>> deleteFestivalContent(
        @PathVariable Integer contentId,
        @RequestParam(value = "festivalId", required = true) Integer festivalId
    ) {

        List<FestivalContentDTO> festivalContentDTOs = festivalContentService.deleteById(festivalId, contentId);
        return ResponseEntity.status(HttpStatus.OK).body(festivalContentDTOs);
    }
    
}
