package com.first.foodo.first_foodo.Controllers;

import com.first.foodo.first_foodo.Dto.JwtResponse;
import com.first.foodo.first_foodo.Dto.LoginRequest;
import com.first.foodo.first_foodo.Dto.RefreshToken;
import com.first.foodo.first_foodo.Dto.UserDto;
import com.first.foodo.first_foodo.Repository.UserRepo;
import com.first.foodo.first_foodo.Security.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth") // sab auth related endpoints yahan honge
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private UserRepo userRepo;
    private ModelMapper modelMapper;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService, UserRepo userRepo, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    // POST endpoint for login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 1) Authenticate user
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),  // record ka direct accessor
                loginRequest.password()
        );
        authenticationManager.authenticate(authenticationToken);

        //getting token(Acces TOken)
        String jwtToken = jwtService.generateToken(loginRequest.email(),true);
        String refreshToken=jwtService.generateToken(loginRequest.email(),false);

        //getting userdetail
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.email());

        UserDto userDto =modelMapper.map( userRepo.findByEmail(userDetails.getUsername()).get(),UserDto.class);

        JwtResponse build = JwtResponse.builder().
                accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(userDto).build();

        return ResponseEntity.ok(build);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @RequestBody RefreshToken refreshToken
    ){
        if(jwtService.validateToken(refreshToken.getRefreshToken()) && jwtService.isRefreshToken(refreshToken.getRefreshToken())){

            String userNameRefreshToken = jwtService.getUserName(refreshToken.getRefreshToken());

            UserDto userDto = modelMapper.map(userRepo.findByEmail(userNameRefreshToken).get(), UserDto.class);

            String accessToken = jwtService.generateToken(userNameRefreshToken, true);
            String newrefreshToken = jwtService.generateToken(userNameRefreshToken, false);

            JwtResponse jwtResponse = JwtResponse.builder().
                    user(userDto).refreshToken(newrefreshToken).accessToken(accessToken).build();


            return ResponseEntity.ok(jwtResponse);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid RefreshToken!!!");
        }
    }


}
