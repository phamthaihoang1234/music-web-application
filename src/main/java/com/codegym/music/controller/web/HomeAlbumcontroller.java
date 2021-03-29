package com.codegym.music.controller.web;

import com.codegym.music.model.Album;
import com.codegym.music.model.Song;
import com.codegym.music.service.AlbumService;
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

@Controller
@RequestMapping("album")
public class HomeAlbumcontroller {
    @Autowired
    AlbumService albumService;

    @Autowired
    SongService songService;

    @GetMapping("{id}")
    public String show(@PathVariable Long id , Model model){
        Album album = albumService.findById(id).get();
        model.addAttribute("albums",album);
        model.addAttribute("songs",songService.findAllByAlbums(album));
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "views"));
        // Lấy ra 5 bài hát nhiều view nhất trả vào list BXH
        Page<Song> songs = songService.findAll(pageable);



        model.addAttribute("bxh", songs);

        return "web/albums/listsongofalbum";
    }

    @ModelAttribute("bxh")
    public Iterable<Song> bxhVn(){
        Pageable pageable = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC, "views"));
        return songService.findAll(pageable);
    }

}
