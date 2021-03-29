package com.codegym.music.service;


import com.codegym.music.model.Singer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SingerService extends IService<Singer> {

    Page<Singer> findAllByNameContains(String name, Pageable pageable);

    Page<Singer> findAll(Pageable pageable);

    Optional<Singer> findByNameContains(String name);
}
