package com.codegym.music.service;

import com.codegym.music.model.Album;
import com.codegym.music.model.Singer;
import com.codegym.music.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface SongService {
    Page<Song> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Page<Song> findAllByNameContains(String name, Integer pageNo, Integer pageSize, String sortBy);

    Page<Song> findAll(Pageable pageable);

    Iterable<Song> findAll();

    Optional<Song> findById(Long id);

    Song save(Song song);

    void deleteById(Long id);

    long count();

    Iterable<Song> findAllBySingerId(Long id);

    Iterable<Song> findAllBy5BySingerId(Long singer_id,Long id);

    Iterable<Song> findAllByAlbums(Album album);

    Page<Song> findAllByAlbums(Album album,Pageable pageable);

    Page<Song> findAllByNameContainsOrAlbumsContainsSingerNameContains(String name, Album album, String singer, Pageable pageable);

    Page<Song> findAllByNameContainsOrAlbumsContains(String name, Album album, Pageable pageable);

    Page<Song> findAllByNameContainsOrSingerNameContains(String name, String singer, Pageable pageable);

    Page<Song> findAllByNameContains(String name, Pageable pageable);

    Optional<Song> findByNameContains(String name);
    Iterable<Song> findAllByStatusTrue();

}

