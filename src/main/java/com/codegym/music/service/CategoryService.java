package com.codegym.music.service;

import com.codegym.music.model.Blog;
import com.codegym.music.model.Category;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CategoryService {

    Page<Category> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Page<Category> findAllByNameContains(String name, Integer pageNo, Integer pageSize, String sortBy);

    Iterable<Category> findAll();

    Optional<Category> findById(Long id);

    Category save(Category category);

    void deleteById(Long id);

}
