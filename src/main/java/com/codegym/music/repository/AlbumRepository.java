package com.codegym.music.repository;

import com.codegym.music.model.Album;
import com.codegym.music.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AlbumRepository extends PagingAndSortingRepository<Album, Long> {

    Page<Album> findAllByNameContains(String name, Pageable pageable);

    Page<Album> findAll(Pageable pageable);

    Iterable<Album> findAll();

    Optional<Album> findByNameContains(String name);


}
