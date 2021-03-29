package com.codegym.music.service.impl;


import com.codegym.music.service.SingerService;
import com.codegym.music.model.Singer;
import com.codegym.music.repository.SingerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SingerServiceImpl implements SingerService {
    @Autowired
    SingerRepository singerRepository;

    @Override
    public Iterable<Singer> findAll() {
        return singerRepository.findAll();
    }

    @Override
    public Page<Singer> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        return singerRepository.findAll(paging);
    }

    @Override
    public Optional<Singer> findById(Long id) {
        return singerRepository.findById(id);
    }

    @Override
    public Singer save(Singer entity) {
        return singerRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        singerRepository.deleteById(id);
    }

    @Override
    public long count() {
        return singerRepository.count();
    }

    @Override
    public Page<Singer> findAllByNameContains(String name, Pageable pageable) {

        return singerRepository.findAllByNameContains(name, pageable);
    }

    @Override
    public Page<Singer> findAll(Pageable pageable) {
        return singerRepository.findAll(pageable);
    }

    @Override
    public Optional<Singer> findByNameContains(String name) {
        return singerRepository.findByNameContains(name);
    }
}
