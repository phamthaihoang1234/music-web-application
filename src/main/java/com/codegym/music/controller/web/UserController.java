package com.codegym.music.controller.web;

import com.codegym.music.model.Song;
import com.codegym.music.model.User;
import com.codegym.music.repository.SongRepository;
import com.codegym.music.service.UserService;
import com.codegym.music.validator.RegisterValidator;
import com.codegym.music.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequestMapping("user")
class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RegisterValidator registerValidator;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private SongRepository songRepository;

    @ModelAttribute("songs")
    public Iterable<Song> sings() {
        return songRepository.findAll();
    }

    private String getPrincipal() {
        String username = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    @GetMapping("login")
    String login() {
        return "web/user/login";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @GetMapping("signup")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("web/user/signup");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("signup")
    public String register(@Validated @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirect) {
        registerValidator.validate(user, result);
        if (result.hasErrors()) {
            return "web/user/signup";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        redirect.addFlashAttribute("globalMessage", "Register successfully.");
        return "redirect:/user/login";
    }

    @GetMapping("profile")
    public String profile(Model model) {
        Optional<User> user = userService.findByEmail(this.getPrincipal());
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "web/user/profile";
        }
        return "redirect:/user/login";
    }

    @PostMapping("profile")
    public String profile(@Validated @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirect) {
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            return "web/user/profile";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        redirect.addFlashAttribute("globalMessage", "Updated profile successfully.");
        return "redirect:/user/profile";
    }

}
