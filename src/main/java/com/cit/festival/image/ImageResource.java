package com.cit.festival.image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cit.festival.exception.NotFoundException;
import com.cit.festival.hotel.Hotel;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/image")
public class ImageResource {

    @Autowired
	private ImageService service;

    // @PostMapping("/hotel")
	// public ResponseEntity<?> uploadHotelImage(
	// 	//@RequestParam(value = "name", required = false) String name,
	// 	@RequestParam("image") MultipartFile file,
	// 	@RequestParam(value = "hotelId") Integer hotelId
	// 	) throws IOException {

	// 	//System.out.println("Hotel ID: " + hotelId);

	// 	String uploadImage = service.uploadHotelImage(file, hotelId);
	// 	return ResponseEntity.status(HttpStatus.OK)
	// 			.body(uploadImage);
	// }


	@PostMapping
	public ResponseEntity<ImageDTO> uploadImage(
		//@RequestParam(value = "name", required = false) String name,
		@RequestParam("image") MultipartFile file
		) throws IOException {

		ImageDTO uploadImage = service.uploadImage(file);
		if (uploadImage != null) {
			return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
		}
		throw new NotFoundException("Upload failed");
		// if (uploadImage != null) {
		// 	return ResponseEntity.ok("File ảnh upload thành công : " + file.getOriginalFilename());
		// }
		// return ResponseEntity.status(HttpStatus.OK)
		// 		.body(uploadImage);
	}

	// @PostMapping("/room")
	// public ResponseEntity<?> uploadRoomImage(
	// 	//@RequestParam(value = "name", required = false) String name,
	// 	@RequestParam("image") MultipartFile file,
	// 	@RequestParam(value = "roomId") Integer roomId
	// 	) throws IOException {

	// 	//System.out.println("Hotel ID: " + hotelId);

	// 	String uploadImage = service.uploadRoomImage(file, roomId);
	// 	return ResponseEntity.status(HttpStatus.OK)
	// 			.body(uploadImage);
	// }

	@GetMapping("/{fileName}")
	public ResponseEntity<Image> findImageByName(@PathVariable String fileName){

		Optional<Image> image = service.findByName(fileName);
		if (!image.isPresent()) {
			throw new NotFoundException("Không tìm thấy hình ảnh");
		}
		return ResponseEntity.status(HttpStatus.OK).body(image.get());

	}

	@GetMapping("/download/{fileName}")
	public ResponseEntity<?> downloadImage(@PathVariable String fileName){

		byte[] imageData=service.downloadImage(fileName);
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.valueOf("image/png"))
				.body(imageData);

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		service.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/all")
    public ResponseEntity<List<Image>> findAll() {

        List<Image> images = service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(images);
    }

	// @GetMapping("/hotel/{hotelId}")
    // public ResponseEntity<List<ImageDTO>> findByHotelId(
	// 	@PathVariable Integer hotelId
	// ) {

    //     List<ImageDTO> imagesDtos = service.findByHotelId(hotelId);
    //     return ResponseEntity.status(HttpStatus.OK).body(imagesDtos);
    // }

	// @GetMapping
	// public ResponseEntity<?> downloadAllImage(){
	// 	List<byte[]> images = new ArrayList<>();
	// 	images = service.downloadAllImage();
	// 	return ResponseEntity.status(HttpStatus.OK)
	// 			.contentType(MediaType.valueOf("image/jpg"))
	// 			.body(images);

	// }

}
