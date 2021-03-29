package com.codegym.music.repository;


import com.codegym.music.model.Singer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SingerRepository extends PagingAndSortingRepository<Singer, Long> {

    Page<Singer> findAllByNameContains(String name, Pageable pageable);

    Page<Singer> findAll(Pageable pageable);

    Iterable<Singer> findAll();

    Optional<Singer> findByNameContains(String name);

}
