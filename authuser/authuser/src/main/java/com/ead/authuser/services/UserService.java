package com.ead.authuser.services;

import com.ead.authuser.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {


    List<User> listAllUsers();

    Optional<User> findUserById(UUID userId);

    void deleteUserById(UUID userId);

    void saveUser(User user);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    Page<User> listPageUsers(Specification<User> spec, Pageable pageable);
}
