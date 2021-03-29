package com.codegym.music.controller.user;

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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("user/songs")
public class UserSongController {

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
        ModelAndView modelAndView = new ModelAndView("/web/user/UserCreate");
        modelAndView.addObject("song", new Song());
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
            return "/web/user/UserCreate";
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
        song.setStatus(false);
        song.setCreateAt(LocalDate.now());
        songService.save(song);
        redirect.addFlashAttribute("globalMessage", "Successfully created a new song: " + song.getId());
        return "redirect:/web/user/create";
    }

    @GetMapping
    public ModelAndView index() {
            Iterable<Song> songs = songService.findAllByStatusTrue();
        ModelAndView modelAndView = new ModelAndView("/web/user/UserListSong");
        modelAndView.addObject("songs", songs);
        return modelAndView;
    }
}
