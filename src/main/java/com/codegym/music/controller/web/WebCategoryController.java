package com.codegym.music.controller.web;

import com.codegym.music.exception.SongNotFoundException;
import com.codegym.music.model.Category;
import com.codegym.music.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("category")
public class WebCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("web/category/index");
        modelAndView.addObject("categories", categoryService.findAll());
        return modelAndView;
    }

    @GetMapping("details")
    public ModelAndView show(@RequestParam("id") Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            throw new SongNotFoundException(id);
        }
        ModelAndView modelAndView = new ModelAndView("web/category/showDetails");
        modelAndView.addObject("category", category.get());
        return modelAndView;
    }

}
