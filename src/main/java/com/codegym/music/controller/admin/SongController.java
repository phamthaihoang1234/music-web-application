package com.codegym.music.controller.admin;

import com.codegym.music.model.Album;
import com.codegym.music.model.Singer;
import com.codegym.music.model.Song;
import com.codegym.music.service.AlbumService;
import com.codegym.music.service.SingerService;
import com.codegym.music.service.SongService;
import com.codegym.music.storage.StorageException;
import com.codegym.music.storage.StorageService;
import com.codegym.music.validator.CustomFileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("admin/songs")
public class SongController {

    @Autowired
    SongService songService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private SingerService singerService;

    @Autowired
    private AlbumService albumService;
    @Autowired
    private CustomFileValidator customFileValidator;

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("singers")
    public Iterable<Singer> singers() {
        return singerService.findAll();
    }

    @ModelAttribute("albums")
    public Iterable<Album> albums() {
        return albumService.findAll();
    }

    @GetMapping("create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("admin/songs/create");
        modelAndView.addObject("song", new Song());
        modelAndView.addObject("title", messageSource.getMessage("title.songs.add", null, Locale.getDefault()));

        return modelAndView;
    }

    @PostMapping("save")
    public String save(@Valid @ModelAttribute("song") Song song, BindingResult result, RedirectAttributes redirect) {
        MultipartFile multipartFile = song.getImageData();
        String fileName = multipartFile.getOriginalFilename();
        MultipartFile mp3File = song.getMp3Data();
        String mp3Name = mp3File.getOriginalFilename();
        customFileValidator.validate(song, result);
        if (result.hasErrors()) {
            return "admin/songs/create";
        }
        try {
            storageService.store(multipartFile);
            song.setImage(fileName);
            storageService.store(mp3File);
            song.setUrl(mp3Name);
        } catch (StorageException e) {
            song.setImage("150.png");
            song.setUrl("aaa");
        }
        song.setViews(0);
        song.setLikeCount(0);
        song.setLikeCount(0);
        song.setCreateAt(LocalDate.now());
        songService.save(song);
        redirect.addFlashAttribute("message", "<div class=\"alert alert-success\">" + messageSource.getMessage("alert.created", new Object[]{song.getName()}, Locale.getDefault()) + "</div>");
        redirect.addFlashAttribute("globalMessage", "Successfully created a new song: " + song.getId());
        return "redirect:/admin/songs/create";
    }

    @GetMapping
    public ModelAndView index(@RequestParam("s") Optional<String> s,
                              @RequestParam(defaultValue = "0") Integer pageNo,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "id") String sortBy, @RequestParam("SearchName") Optional<String> search, Pageable pageable) {
        Page<Song> songs;

        if (s.isPresent() && search.isPresent()) {
            songs = songService.findAllByNameContains(s.get(), pageNo, pageSize, sortBy);
            Optional<Song> song = songService.findByNameContains(search.get());
            if (song.isPresent()){
                songs = songService.findAllByNameContains(search.get(),pageable);
            }
        } else {
            songs = songService.findAll(pageNo, pageSize, sortBy);
        }
        ModelAndView modelAndView = new ModelAndView("admin/songs/list");
        modelAndView.addObject("songs", songs);
        modelAndView.addObject("txtSearch", s);
        modelAndView.addObject("title", messageSource.getMessage("title.songs.list", null, Locale.getDefault()));

        return modelAndView;
    }

    @GetMapping("{id}")
    public ModelAndView show(@PathVariable Long id, Model model) {
        ModelAndView modelAndView = new ModelAndView("admin/songs/view");
        Song song = songService.findById(id).get();
        modelAndView.addObject("song",song);
        modelAndView.addObject("title", messageSource.getMessage("title.songs.view", null, Locale.getDefault()));

        return modelAndView;

    }

    @GetMapping("edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<Song> song = songService.findById(id);
        if (song.isPresent()) {
            model.addAttribute("song", song.get());
            model.addAttribute("title", messageSource.getMessage("title.songs.edit", new Object[]{song.get().getName()}, Locale.getDefault()));
            return "admin/songs/edit";
        } else {
            redirect.addFlashAttribute("message", "Song not found!");
            return "redirect:/admin/songs";
        }
    }

    @PostMapping("edit")
    public String updateBlog(@Validated @ModelAttribute("song") Song song, BindingResult result) {
        MultipartFile multipartFile = song.getImageData();
        String fileName = multipartFile.getOriginalFilename();
        MultipartFile mp3File = song.getMp3Data();
        String mp3Name = mp3File.getOriginalFilename();
        Optional<Song> oldSong = songService.findById(song.getId());

        if (song.getViews() == null) {
            song.setViews(oldSong.get().getViews());
        }
        if (!song.getImageData().isEmpty() && !song.getMp3Data().isEmpty()) {

            customFileValidator.validate(song, result);
        }
        if (result.hasErrors()) {
            return "admin/songs/edit";
        }
        try {
            storageService.store(multipartFile);
            song.setImage(fileName);
            storageService.store(mp3File);
            song.setUrl(mp3Name);
        } catch (StorageException e) {
//            song.setImage("150.png");
//            song.setUrl("aaa");
            if (song.getImageData().isEmpty()) {
                song.setImage(oldSong.get().getImage());
            }
            if (song.getMp3Data().isEmpty()) {
                song.setUrl(oldSong.get().getUrl());
            }
        }
        songService.save(song);
        ModelAndView modelAndView = new ModelAndView("admin/songs/edit");
        modelAndView.addObject("song", song);
        modelAndView.addObject("message", "Song updated sucessfully");
        return "redirect:/admin/songs";
    }

    @PostMapping("delete")
    public String deleteById(@RequestParam("id") Long id, RedirectAttributes redirect) {
        songService.deleteById(id);
        redirect.addFlashAttribute("globalMessage", "Successfully deleted a song");
        return "redirect:/admin/songs";
    }
}
