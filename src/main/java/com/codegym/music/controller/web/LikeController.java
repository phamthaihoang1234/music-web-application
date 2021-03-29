package com.codegym.music.controller.web;


import com.codegym.music.model.Song;
import com.codegym.music.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("like")
public class LikeController {

    @Autowired
    SongService songService;

    @GetMapping
    public Integer like(@RequestParam("id") Long id) {
       // xuất hiện errol vì khai báo tên thuộc tính trùng từ khóa
        Optional<Song> song = songService.findById(id);

        if (song.isPresent()) {
            song.get().setLikeCount(song.get().getLikeCount() + 1);
            songService.save(song.get());
        } else {
            return 0;
        }
        return song.get().getLikeCount();
    }
}
