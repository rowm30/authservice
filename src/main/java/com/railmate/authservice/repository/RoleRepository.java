package com.railmate.authservice.repository;

import com.railmate.authservice.model.Role;
import com.railmate.authservice.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
