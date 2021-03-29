package com.codegym.music.controller.web;

import com.codegym.music.model.Album;
import com.codegym.music.model.Response;
import com.codegym.music.model.Song;
import com.codegym.music.repository.SongRepository;
import com.codegym.music.service.AlbumService;
import com.codegym.music.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("bxh")
public class BxhController {

    @Autowired
    private SongService songService;

    @Autowired
    private AlbumService albumService;

    @GetMapping
    public Response bxh(@RequestParam("album") Long id){
        Optional<Album> album = albumService.findById(id);
        Pageable pageable = PageRequest.of(0,5,Sort.by(Sort.Direction.DESC, "views"));
        Iterable<Song> songs = songService.findAllByAlbums(album.get(),pageable);
        return new Response(songs, "OK", HttpStatus.OK);
    }
}
