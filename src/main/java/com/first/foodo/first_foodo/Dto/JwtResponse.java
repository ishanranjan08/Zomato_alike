package com.first.foodo.first_foodo.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtResponse {
    String accessToken;
    String refreshToken;
    private UserDto user;
}
