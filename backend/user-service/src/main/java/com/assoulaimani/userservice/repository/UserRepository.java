package com.assoulaimani.userservice.repository;


import com.assoulaimani.userservice.entity.Role;
import com.assoulaimani.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    boolean existsByEmail(String email);

    List<User> findByNomContainingIgnoreCase(String nom);
}
