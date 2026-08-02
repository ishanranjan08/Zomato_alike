package com.first.foodo.first_foodo.Controllers;


import com.first.foodo.first_foodo.Dto.RestaurantDto;
import com.first.foodo.first_foodo.Service.ImageService;
import com.first.foodo.first_foodo.Service.RestaurantService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ImageService imageService;
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    public RestaurantController(RestaurantService restaurantService, ImageService imageService) {
        this.restaurantService = restaurantService;
        this.imageService = imageService;
    }


    @PostMapping
    public ResponseEntity<RestaurantDto> add(@Valid @RequestBody RestaurantDto restaurantDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.add(restaurantDto));
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<String> delete(@PathVariable("restaurantId") String  id){
        restaurantService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Restaurant Deleted");
    }


    @GetMapping
    public ResponseEntity<Page<RestaurantDto>> restaurants(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RestaurantDto> restaurants = restaurantService.getAll(pageable);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);

    }


    @PutMapping
    public ResponseEntity<RestaurantDto>  update(@Valid @RequestBody RestaurantDto restaurantDto){
        String id = restaurantDto.getId();

        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.update(restaurantDto,id));
    }

    @PostMapping("/image/{restaurantId}")
    public ResponseEntity<?> image(@RequestParam("trade")MultipartFile multipartFile,
                                   @PathVariable("restaurantId") String id) throws IOException {


        logger.info("Received banner upload request: {} ({})", multipartFile.getOriginalFilename(), multipartFile.getContentType());

        restaurantService.uploadBanner(multipartFile,id);
        logger.info("Image uploaded successfully");

        return ResponseEntity.ok("Image is uploaded Succesfully");
    }
}
