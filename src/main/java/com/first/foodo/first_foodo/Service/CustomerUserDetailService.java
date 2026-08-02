package com.first.foodo.first_foodo.Service;

import com.first.foodo.first_foodo.Entity.User;
import com.first.foodo.first_foodo.Exception.ResourceNotFoundException;
import com.first.foodo.first_foodo.Repository.UserRepo;
import com.first.foodo.first_foodo.Security.CustomerUserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    private UserRepo userRepo;

    public CustomerUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        CustomerUserDetail customerUserDetail = new CustomerUserDetail(user);
        return customerUserDetail;
    }



}
