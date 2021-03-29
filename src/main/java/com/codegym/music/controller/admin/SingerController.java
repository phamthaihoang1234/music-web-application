package com.codegym.music.controller.admin;


import com.codegym.music.model.Singer;
import com.codegym.music.service.SingerService;
import com.codegym.music.service.SongService;
import com.codegym.music.storage.StorageException;
import com.codegym.music.storage.StorageService;
import com.codegym.music.validator.SingerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("admin/singers")
public class SingerController {

    Logger logger = LoggerFactory.getLogger(SingerController.class);

    @Autowired
    private SongService songService;

    @Autowired
    private SingerService singerService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private StorageService storageService;

    @Autowired
    private SingerValidator singerValidator;

    @GetMapping("create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("admin/singer/create");
        modelAndView.addObject("singer", new Singer());
        return modelAndView;
    }

    @PostMapping("create")
    public String saveSinger(@Valid @ModelAttribute("singer") Singer singer, BindingResult result, RedirectAttributes redirect) {
//        singerValidator.validate(singer, result);
        if (result.hasErrors()) {
            return "admin/singer/create";
        }
        MultipartFile avatarFile = singer.getImageData();
        MultipartFile coverFile = singer.getCoverFile();
        try {
            storageService.store(avatarFile);
            storageService.store(coverFile);
            singer.setAvatarURL(avatarFile.getOriginalFilename());
            singer.setCoverURL(coverFile.getOriginalFilename());
        } catch (StorageException ex) {
            logger.error("ANHNBT EXCEPTION: " + ex.getMessage());
            singer.setAvatarURL("150.png");
            singer.setCoverURL("150.png");
        }
        singer.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        singerService.save(singer);
        redirect.addFlashAttribute("message", messageSource.getMessage("alert.created", new Object[]{singer.getName()}, Locale.getDefault()));
        return "redirect:/admin/singers";
    }

    @GetMapping
    public ModelAndView index(@RequestParam("s") Optional<String> s, Pageable pageable) {
        Page<Singer> singers;
        if (s.isPresent()) {
//            Sort sort = Sort.by("id").descending();
            singers = singerService.findAllByNameContains(s.get(), pageable);
        } else {
            singers = singerService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("admin/singer/list");
        modelAndView.addObject("singers", singers);
        modelAndView.addObject("title", messageSource.getMessage("title.singers.list", null, Locale.getDefault()));
        modelAndView.addObject("txtSearch", s);
        return modelAndView;
    }

    @GetMapping("{id}")
    public String show(@PathVariable Long id, Model model) {
        Singer singers = singerService.findById(id).get();
        model.addAttribute("singer", singers);
        model.addAttribute("songs", songService.findAllBySingerId(id));
        return "admin/singer/view";
    }

    @GetMapping("edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<Singer> singer = singerService.findById(id);
        if (singer.isPresent()) {
            model.addAttribute("singer", singer.get());
            return "admin/singer/edit";
        } else {
            redirect.addFlashAttribute("message", "Singer not found!");
            return "redirect:/admin/singers";
        }
    }

    @PostMapping("edit-singer")
    public String updateBlog(@Valid @ModelAttribute("singer") Singer singer, BindingResult result, RedirectAttributes redirect) {
        Optional<Singer> oldSingger = singerService.findById(singer.getId());
        if (oldSingger.get().getAvatarURL() == null) {
            singerValidator.validate(singer, result);
        }
        if (result.hasErrors()) {
            return "admin/singer/edit";
        }
        try {
            MultipartFile avatarFile = singer.getImageData();
            MultipartFile coverFile = singer.getCoverFile();

            storageService.store(avatarFile);
            storageService.store(coverFile);
            singer.setAvatarURL(avatarFile.getOriginalFilename());
            singer.setCoverURL(coverFile.getOriginalFilename());
        } catch (StorageException ex) {
            logger.error("ANHNBT EXCEPTION: " + ex.getMessage());
            singer.setAvatarURL("150.png");
            singer.setCoverURL("150.png");
            if (singer.getAvatarURL().isEmpty()) {
                singer.setAvatarURL(oldSingger.get().getAvatarURL());
            }
            if (singer.getCoverURL().isEmpty()) {
                singer.setCoverURL(oldSingger.get().getCoverURL());
            }
        }
        singerService.save(singer);
        redirect.addFlashAttribute("message", messageSource.getMessage("alert.updated", new Object[]{singer.getName()}, Locale.getDefault()));
        return "redirect:/admin/singers";
    }

    @GetMapping("delete-singer/{id}")
    public String showDeleteForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
        Optional<Singer> singer = singerService.findById(id);
        if (singer.isPresent()) {
            model.addAttribute("singer", singer.get());
            return "admin/singer/delete";
        } else {
            redirect.addFlashAttribute("message", "Singer not found");
            return "redirect:/admin/singers";
        }
    }

    @PostMapping("delete")
    public String deleteBlog(@ModelAttribute("singer") Singer singer, RedirectAttributes redirect) {
        singerService.deleteById(singer.getId());
        redirect.addFlashAttribute("message", "Deleted successfully.");
        return "redirect:/admin/singers";
    }
}
