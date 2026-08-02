package com.first.foodo.first_foodo.Repository;

import com.first.foodo.first_foodo.Entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepo extends JpaRepository<Restaurant,String> {
    List<Restaurant> findByAddress(String address);

    List<Restaurant> findByNameContainingIgnoreCase(String name);
    Page<Restaurant> findByOpen(Boolean flag, Pageable pageable);
    Page<Restaurant> findAll(Pageable pageable);

}
