package com.first.foodo.first_foodo.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantDto {

    private String id;
    private String description;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "KK-mm-ss")
    private LocalTime openTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "KK-mm-ss")
    private LocalTime closeTime;
    private Boolean open=true;
    private String banner;
}
