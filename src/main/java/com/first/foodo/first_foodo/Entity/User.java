package com.first.foodo.first_foodo.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "foodo_users")
public class User {



    @Id
    private String id;

    @Column(nullable = false )
    private String name;

    @Column(unique = true,nullable = false)
    private String email;

    private String password;

    private String address;

    private String phoneNumber;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> roleEntities = new ArrayList<>();



    private boolean enabled=true;


    private boolean available = true; // applicable for delivery boy

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Restaurant> restaurants= new ArrayList<>();
}
