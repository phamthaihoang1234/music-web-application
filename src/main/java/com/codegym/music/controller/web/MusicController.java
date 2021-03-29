package com.codegym.music.controller.web;

import com.codegym.music.exception.SongNotFoundException;
import com.codegym.music.model.Album;
import com.codegym.music.model.Category;
import com.codegym.music.model.Singer;
import com.codegym.music.model.Song;
import com.codegym.music.service.AlbumService;
import com.codegym.music.service.CategoryService;
import com.codegym.music.service.SingerService;
import com.codegym.music.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("music")
public class MusicController {

    @Autowired
    private SongService songService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private SingerService singerService;

    @ModelAttribute("categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @ModelAttribute("albums")
    public Iterable<Album> albums() {
        return albumService.findAll();
    }

    @ModelAttribute("singers")
    public Iterable<Singer> sings() {
        return singerService.findAll();
    }


    @GetMapping("{id}")
    public String SongInfo(@PathVariable("id") Long id, Model model) {
        Optional<Song> song = songService.findById(id);
        if (!song.isPresent()) throw new SongNotFoundException(id);

        // Lấy ra 5 bài hát cùng thể loại
        Iterable<Song> concerning_song = songService.findAllBy5BySingerId(song.get().getSinger().getId(), id);

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "views"));

        // Lấy ra 5 bài hát nhiều view nhất trả vào list BXH
        Page<Song> songs = songService.findAll(pageable);

        // Cập nhật view > tăng view lên 1
        song.get().setViews(song.get().getViews() + 1);
        songService.save(song.get());

        model.addAttribute("song", song.get());
        model.addAttribute("concerning_songs", concerning_song);
        model.addAttribute("bxh", songs);
        return "web/music/musicInfo";
    }
}
