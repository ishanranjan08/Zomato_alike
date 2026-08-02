package com.first.foodo.first_foodo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class RoleEntity {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany(mappedBy ="roleEntities")
    private List<User> users =new ArrayList<>();
}
