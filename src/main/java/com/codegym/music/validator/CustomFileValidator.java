package com.codegym.music.validator;

import com.codegym.music.model.Album;
import com.codegym.music.model.Blog;
import com.codegym.music.model.Singer;
import com.codegym.music.model.Song;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Component
public class CustomFileValidator implements Validator {

    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpg", "image/jpeg", "image/gif","audio/mpeg");
    public static final long TEN_MB_IN_BYTES = 10485760; // 10 MB

    @Override
    public boolean supports(Class<?> clazz) {
        return Blog.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MultipartFile file = null;
        if (target instanceof Blog) {
            Blog blog = (Blog) target;
            file = blog.getImageData();
        } else if (target instanceof Album) {
            Album album = (Album) target;
            file = album.getImageData();
        } else if (target instanceof Song) {
            Song song = (Song) target;
            file = song.getImageData();
        } else if (target instanceof Singer) {
            Singer singer = (Singer) target;
            file = singer.getImageData();
        }
        if (file.isEmpty()) {
            errors.rejectValue("imageData", "label.upload.file.required");
        } else if (!contentTypes.contains(file.getContentType())) {
            errors.rejectValue("imageData", "label.upload.invalid.file.type");
        } else if (file.getSize() > TEN_MB_IN_BYTES) {
            errors.rejectValue("imageData", "label.upload.exceeded.file.size");
        }
    }
}
