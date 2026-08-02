package com.first.foodo.first_foodo.Service;

import com.first.foodo.first_foodo.Dto.RestaurantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface RestaurantService {

    RestaurantDto add(RestaurantDto restaurantDto);

    RestaurantDto update (RestaurantDto restaurantDto, String id);
    void delete (String delete);
    //get single
    RestaurantDto get(String id);
    // get all
    Page<RestaurantDto> getAll(Pageable pageable);
    List<RestaurantDto> searchByName(String keyword);

    Page<RestaurantDto> getOpenRestaurants(Pageable pageable);

    RestaurantDto uploadBanner(MultipartFile file, String resturantId) throws IOException;
}
