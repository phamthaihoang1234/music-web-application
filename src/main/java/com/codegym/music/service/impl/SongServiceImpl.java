package com.codegym.music.service.impl;

import com.codegym.music.model.Album;
import com.codegym.music.model.Category;
import com.codegym.music.model.Singer;
import com.codegym.music.model.Song;
import com.codegym.music.repository.SongRepository;
import com.codegym.music.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongServiceImpl implements SongService {

    @Autowired
    private SongRepository songRepository;


    @Override
    public Page<Song> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        return songRepository.findAll(paging);
    }

    @Override
    public Page<Song> findAllByNameContains(String name, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        return songRepository.findAllByNameContains(name, paging);
    }

    @Override
    public Page<Song> findAll(Pageable pageable) {
        return songRepository.findAll(pageable);
    }

    @Override
    public Iterable<Song> findAll() {
        return songRepository.findAll();
    }

    @Override
    public Optional<Song> findById(Long id) {
        return songRepository.findById(id);
    }

    @Override
    public Song save(Song song) {
        return songRepository.save(song);
    }

    @Override
    public void deleteById(Long id) {
        songRepository.deleteById(id);
    }

    @Override
    public long count() {
        return songRepository.count();
    }

    @Override
    public Iterable<Song> findAllBySingerId(Long id) {
        return songRepository.findAllBySingerId(id);
    }

    @Override
    public Iterable<Song> findAllBy5BySingerId(Long singer_id, Long id) {
        return songRepository.findFirst5BySingerIdAndIdNot(singer_id, id);
    }

    @Override
    public Iterable<Song> findAllByAlbums(Album album) {
        return songRepository.findAllByAlbums(album);
    }

    @Override
    public Page<Song> findAllByAlbums(Album album, Pageable pageable) {
        return songRepository.findAllByAlbums(album, pageable);
    }

    @Override
    public Page<Song> findAllByNameContainsOrAlbumsContainsSingerNameContains(String name, Album album, String singer, Pageable pageable) {
        return songRepository.findAllByNameContainsOrAlbumsContainsOrSingerContains(name, album, singer, pageable);
    }

    @Override
    public Iterable<Song> findAllByStatusTrue() {
        return songRepository.findAllByStatusTrue();
    }

    @Override
    public Page<Song> findAllByNameContainsOrAlbumsContains(String name, Album album, Pageable pageable) {
        return songRepository.findAllByNameContainsOrAlbumsContains(name, album, pageable);
    }

    @Override
    public Page<Song> findAllByNameContainsOrSingerNameContains(String name, String singer, Pageable pageable) {
        return songRepository.findAllByNameContainsOrSingerNameContains(name, singer, pageable);
    }

    @Override
    public Page<Song> findAllByNameContains(String name, Pageable pageable) {
        return songRepository.findAllByNameContains(name, pageable);
    }

    @Override
    public Optional<Song> findByNameContains(String name) {
        return songRepository.findByNameContains(name);
    }

}
