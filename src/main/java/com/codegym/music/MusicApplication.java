package com.codegym.music;

import com.codegym.music.model.Role;
import com.codegym.music.model.User;
import com.codegym.music.service.RoleService;
import com.codegym.music.service.UserService;
import com.codegym.music.storage.StorageProperties;
import com.codegym.music.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MusicApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MusicApplication.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(MusicApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
//            storageService.deleteAll();
            storageService.init();
        };
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleService.findByName("ROLE_ADMIN") == null) {
            roleService.save(new Role("ROLE_ADMIN"));
            log.info("Create ROLE_ADMIN");
        }
        log.info("-------------------------------");
        if (roleService.findByName("ROLE_MEMBER") == null) {
            roleService.save(new Role("ROLE_MEMBER"));
            log.info("Create ROLE_MEMBER");
        }
        log.info("-------------------------------");
        if (!userService.findByEmail("admin@gmail.com").isPresent()) {
            User admin = new User();
            admin.setName("Tuan Anh");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            HashSet<Role> roles = new HashSet<>();
            roles.add(roleService.findByName("ROLE_ADMIN"));
            roles.add(roleService.findByName("ROLE_MEMBER"));
            admin.setRoles(roles);
            userService.save(admin);
            log.info("Create admin account 'admin' password: 'admin'");
        }
        log.info("-------------------------------");
        if (!userService.findByEmail("user@gmail.com").isPresent()) {
            User user = new User();
            user.setName("Khoai Tay");
            user.setEmail("user@gmail.com");
            user.setPassword(passwordEncoder.encode("user"));
            HashSet<Role> roles = new HashSet<>();
            roles.add(roleService.findByName("ROLE_MEMBER"));
            user.setRoles(roles);
            userService.save(user);
            log.info("Create user account 'user' password: 'user'");
        }
        log.info("-------------------------------");
    }
}
