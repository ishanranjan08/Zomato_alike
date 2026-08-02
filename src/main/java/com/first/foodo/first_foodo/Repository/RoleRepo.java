package com.first.foodo.first_foodo.Repository;

import com.first.foodo.first_foodo.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<RoleEntity,Integer> {
    RoleEntity findByName(String name);

}
