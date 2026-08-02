package com.first.foodo.first_foodo.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Entity
@Table(name = "foodo_restaurant")
@ToString
@Getter
@Setter
public class Restaurant {




    @Id
    private String id;

    @Lob
    private String description;
    private String name;
    private String address;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean open=true;
    private String banner;

    @ManyToOne
    private User user;
}
