package com.first.foodo.first_foodo.Service.impl;

import com.first.foodo.first_foodo.Dto.FileUpload;
import com.first.foodo.first_foodo.Dto.RestaurantDto;
import com.first.foodo.first_foodo.Entity.Restaurant;
import com.first.foodo.first_foodo.Exception.ImageErrorException;
import com.first.foodo.first_foodo.Exception.ResourceNotFoundException;
import com.first.foodo.first_foodo.Repository.RestaurantRepo;
import com.first.foodo.first_foodo.Service.ImageService;
import com.first.foodo.first_foodo.Service.RestaurantService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepo restaurantRepo;
    private ModelMapper modelMapper;
    private ImageService imageService;

    public RestaurantServiceImpl(RestaurantRepo restaurantRepo, ModelMapper modelMapper, ImageService imageService) {
        this.restaurantRepo = restaurantRepo;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
    }
    @Value("${restaurant.file.path}")
    private String folderPath;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp"
    );
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".webp"
    );

    @Override
    public RestaurantDto add(RestaurantDto restaurantDto) {

        restaurantDto.setId(UUID.randomUUID().toString());
        Restaurant restaurant = modelMapper.map(restaurantDto, Restaurant.class);
        restaurantRepo.save(restaurant);

        return modelMapper.map(restaurant, RestaurantDto.class);

    }

    @Override
    public RestaurantDto update(RestaurantDto restaurantDto, String id) {
        Restaurant restaurant = restaurantRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Restaurant Not FOund!!!"));


        restaurant.setName (restaurantDto.getName());
        restaurant.setAddress(restaurantDto.getAddress());
        restaurant.setDescription (restaurantDto.getDescription());
        restaurant.setCloseTime(restaurantDto.getCloseTime());
        restaurant.setOpenTime(restaurantDto.getOpenTime());
        restaurant.setOpen(restaurantDto.getOpen());
        Restaurant savedEntity = restaurantRepo.save(restaurant);

        return modelMapper.map(savedEntity, RestaurantDto.class);

    }

    @Override
    public void delete(String delete) {
        Restaurant restaurant = restaurantRepo.findById(delete).orElseThrow(() -> new ResourceNotFoundException("Restaurant not Found!!!"));

        restaurantRepo.delete(restaurant);
    }

    @Override
    public RestaurantDto get(String id) {
        Restaurant restaurant = restaurantRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Restaurant not Found!!!"));
        return modelMapper.map(restaurant,RestaurantDto.class);
    }

    @Override
    public Page<RestaurantDto> getAll(Pageable pageable) {
        return restaurantRepo.findAll(pageable).
                map((restaurant)->modelMapper.
                        map(restaurant,RestaurantDto.class));
    }



    @Override
    public List<RestaurantDto> searchByName(String keyword) {
        return restaurantRepo.findByNameContainingIgnoreCase(keyword).
                stream().map((temp)->modelMapper.map(temp,RestaurantDto.class)).
                collect(Collectors.toList());
    }

    @Override
    public Page<RestaurantDto> getOpenRestaurants(Pageable pageable) {
        Page<Restaurant> byOpen = restaurantRepo.findByOpen(true, pageable);
        return byOpen.map((restaurant)->modelMapper.map(restaurant,RestaurantDto.class));
    }

    @Override
    public RestaurantDto uploadBanner(MultipartFile file, String resturantId) throws IOException {

        Restaurant restaurant = restaurantRepo.findById(resturantId).orElseThrow(() -> new ResourceNotFoundException("Your given Restaurant ID is not there in the message"));

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new ImageErrorException("Only JPG, PNG, and WEBP images are allowed");
        }

        String fileName= file.getOriginalFilename();
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            throw new ImageErrorException("Uploaded file has no valid extension");
        }
        String fileExtension =fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new ImageErrorException("Only .jpg, .jpeg, .png, and .webp files are allowed");
        }
        String fullName=new Date().getTime() + fileExtension;

        FileUpload upload = imageService.upload(file, folderPath + fullName);

        restaurant.setBanner(upload.fileName());
        Restaurant save = restaurantRepo.save(restaurant);
        return modelMapper.map(save,RestaurantDto.class);
    }
}
