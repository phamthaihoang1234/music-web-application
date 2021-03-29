package com.codegym.music.service;


import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IService<E> {

    Iterable<E> findAll();

    Page<E> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Optional<E> findById(Long id);

    E save(E entity);

    void deleteById(Long id);

    long count();
}
