package com.codegym.music.service;

import com.codegym.music.model.Song;
import com.codegym.music.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User save(User user);

    Optional<User> findByEmail(String email);

    Iterable<User> findAll();

    Optional<User> findById(Long id);

    boolean existByEmail(String email);

    Page<Song> findAll(Pageable pageable);
}
