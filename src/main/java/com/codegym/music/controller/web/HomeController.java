package com.codegym.music.controller.web;

import com.codegym.music.model.Album;
import com.codegym.music.model.Category;
import com.codegym.music.model.Song;
import com.codegym.music.model.Singer;
import com.codegym.music.service.AlbumService;
import com.codegym.music.service.CategoryService;
import com.codegym.music.service.SongService;
import com.codegym.music.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class HomeController {

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

    @GetMapping("/403")
    public String accessDenied() {
        return "errors/403";
    }

    @ModelAttribute("songs")
    public Iterable<Song> songs() {
        return songService.findAllByStatusTrue();
    }

    @GetMapping("/")
    public ModelAndView listSongs(@RequestParam("SearchName") Optional<String> search, @PageableDefault(size = 10) Pageable pageable) {
        Page<Song> songs; // Tạo đối tượng lưu Page songs;
        ModelAndView modelAndView = new ModelAndView("web/home");
        if (search.isPresent()) {
            Optional<Album> album = albumService.findByNameContains(search.get());
            Optional<Singer> singer = singerService.findByNameContains(search.get());

            if (album.isPresent() && singer.isPresent()) {
                // Kiểm tra xem nếu Parameter search được truyền vào thì gọi service có 2 tham số
                songs = songService.findAllByNameContainsOrAlbumsContainsSingerNameContains(search.get(), album.get(), search.get(), pageable);
            } else if (album.isPresent()) {
                songs = songService.findAllByNameContainsOrAlbumsContains(search.get(), album.get(), pageable);
            } else if (singer.isPresent()) {
                songs = songService.findAllByNameContainsOrSingerNameContains(search.get(), search.get(), pageable);
            } else {
                songs = songService.findAllByNameContains(search.get(), pageable);
            }
            System.out.println(1);
        } else {
            // Nếu không có search thì gọi service có 1 tham số
            songs = songService.findAll(pageable);
        }
        modelAndView.addObject("songs", songs);

        return modelAndView;
    }

    @ModelAttribute("bxh")
    public Iterable<Song> bxhVn() {
        Optional<Album> album = albumService.findById(1L);
        Iterable<Song> bxh = null;
        if (album.isPresent()) {
            Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "views"));
            bxh = songService.findAllByAlbums(album.get(), pageable);
        }
        return bxh;
    }
}
