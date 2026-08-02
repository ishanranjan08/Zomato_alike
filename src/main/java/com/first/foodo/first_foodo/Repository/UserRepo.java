package com.first.foodo.first_foodo.Repository;

import com.first.foodo.first_foodo.Dto.UserDto;
import com.first.foodo.first_foodo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,String> {
    List<User> findAllByAddress(String address);

    @Query("SELECT DISTINCT u FROM User u JOIN u.restaurants r WHERE r.address = :address")
    List<User> findUsersByRestaurantAddress(String address);

   List<User> findByName(String name);


    Optional<User> findByEmail(String email);

}
