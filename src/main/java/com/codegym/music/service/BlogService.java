package com.codegym.music.service;

import com.codegym.music.model.Blog;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface BlogService {
    Page<Blog> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Page<Blog> findAllByTitleContains(String title, Integer pageNo, Integer pageSize, String sortBy);

    Page<Blog> findAllBlogsByCategoryId(Long id, Integer pageNo, Integer pageSize, String sortBy);

    Optional<Blog> findById(Long id);

    Blog save(Blog blog);

    void deleteById(Long id);
}
