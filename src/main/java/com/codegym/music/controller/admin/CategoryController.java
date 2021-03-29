package com.codegym.music.controller.admin;

import com.codegym.music.model.Category;
import com.codegym.music.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }


    @GetMapping
    public ModelAndView index(@RequestParam("s") Optional<String> s,
                              @RequestParam(defaultValue = "0") Integer pageNo,
                              @RequestParam(defaultValue = "5") Integer pageSize,
                              @RequestParam(defaultValue = "id") String sortBy) {
        Page<Category> categories;
        if (s.isPresent()) {
//            Sort sort = Sort.by("id").descending();
            categories = categoryService.findAllByNameContains(s.get(), pageNo, pageSize, sortBy);
        } else {
            categories = categoryService.findAll(pageNo, pageSize, sortBy);
        }
        ModelAndView modelAndView = new ModelAndView("admin/categories/index");
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("title", messageSource.getMessage("title.categories.index", null, Locale.getDefault()));
        modelAndView.addObject("txtSearch", s);
        return modelAndView;
    }

    @GetMapping("create")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("admin/categories/create");
        modelAndView.addObject("category", new Category());
        modelAndView.addObject("title", messageSource.getMessage("title.categories.add", null, Locale.getDefault()));
        return modelAndView;
    }

    @PostMapping("create")
    public String create(@Valid @ModelAttribute("category") Category category, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "admin/categories/create";
        }
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        categoryService.save(category);

        redirect.addFlashAttribute("message", "<div class=\"alert alert-success\">" + messageSource.getMessage("alert.created", new Object[] {category.getName()}, Locale.getDefault()) + "</div>");
        return "redirect:/admin/categories";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
        if (id == null) {
            redirect.addFlashAttribute("message", "<div class=\"alert alert-danger\">" + messageSource.getMessage("alert.null", null, Locale.getDefault()) + "</div>");
            return "redirect:/admin/categories";
        }

        Optional<Category> category = categoryService.findById(id);

        if (!category.isPresent()) {
            redirect.addFlashAttribute("message", "<div class=\"alert alert-danger\">" + messageSource.getMessage("alert.notfound", new Object[]{id}, Locale.getDefault()) + "</div>");
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", category.get());
        model.addAttribute("title", messageSource.getMessage("title.categories.edit", new Object[]{category.get().getName()}, Locale.getDefault()));
        return "admin/categories/edit";
    }

    private String notFound(RedirectAttributes redirect) {
        return "errors/404";
    }

    @PostMapping("edit")
    public String edit(@Valid @ModelAttribute("category") Category category, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "admin/categories/edit";
        }
        category.setUpdatedAt(LocalDateTime.now());
        categoryService.save(category);
        redirect.addFlashAttribute("message", "<div class=\"alert alert-success\">" + messageSource.getMessage("alert.updated", new Object[] {category.getName()}, Locale.getDefault()) + "</div>");
        return "redirect:/admin/categories";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("id") Long id, RedirectAttributes redirect) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            redirect.addFlashAttribute("message", "<div class=\"alert alert-success\">" + messageSource.getMessage("alert.deleted", new Object[] {category.get().getName()}, Locale.getDefault()) + "</div>");
            categoryService.deleteById(id);
        } else {
            redirect.addFlashAttribute("message", "<div class=\"alert alert-danger\">" + messageSource.getMessage("alert.notfound", new Object[]{id}, Locale.getDefault()) + "</div>");
        }
        return "redirect:/admin/categories";
    }
}
