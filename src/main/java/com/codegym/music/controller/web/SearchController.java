package com.codegym.music.controller.web;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("search")
public class SearchController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SongService songService;

    @Autowired
    private SingerService singerService;

    @Autowired
    private AlbumService albumService;

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

    @GetMapping
    public ModelAndView index(@RequestParam("q") Optional<String> query, Pageable pageable) {
        Page<Song> songs; // Tạo đối tượng lưu Page songs;
        if (query.isPresent()) {
            Optional<Album> album = albumService.findByNameContains(query.get());
            Optional<Singer> singer = singerService.findByNameContains(query.get());

            if (album.isPresent() && singer.isPresent()) {
                // Kiểm tra xem nếu Parameter search được truyền vào thì gọi service có 2 tham số
                songs = songService.findAllByNameContainsOrAlbumsContainsSingerNameContains(query.get(), album.get(), query.get(), pageable);
            } else if (album.isPresent()) {
                songs = songService.findAllByNameContainsOrAlbumsContains(query.get(), album.get(), pageable);
            } else if (singer.isPresent()) {
                songs = songService.findAllByNameContainsOrSingerNameContains(query.get(), query.get(), pageable);
            } else {
                songs = songService.findAllByNameContains(query.get(), pageable);
            }
            System.out.println(1);
        } else {
            // Nếu không có search thì gọi service có 1 tham số
            songs = songService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("web/search/index");
        modelAndView.addObject("songs", songs);
        modelAndView.addObject("title", query);
        return modelAndView;
    }

    @ModelAttribute("bxh")
    public Iterable<Song> bxhVn(){
        Optional<Album> album = albumService.findById(1L);
        Pageable pageable = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC, "views"));
        return songService.findAllByAlbums(album.get(),pageable);
    }

}
