package com.codegym.music.validator;

import com.codegym.music.model.Singer;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Component
public class SingerValidator implements Validator {

    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpg", "image/jpeg", "image/gif");
    public static final long TEN_MB_IN_BYTES = 10485760; // 10 MB

    @Override
    public boolean supports(Class<?> clazz) {
        return Singer.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Singer singer = (Singer) target;
        MultipartFile avatarFile = singer.getImageData();
        MultipartFile coverFile = singer.getCoverFile();

        if (avatarFile.isEmpty()) {
            errors.rejectValue("imageData", "label.upload.avatar.required");
        } else if (contentTypes.contains(avatarFile.getContentType())) {
            errors.rejectValue("imageData", "label.upload.invalid.file.type");
        } else if (avatarFile.getSize() > TEN_MB_IN_BYTES) {
            errors.rejectValue("imageData", "label.upload.exceeded.file.size");
        }

        if (coverFile.isEmpty()) {
            errors.rejectValue("coverFile", "label.upload.cover.required");
        } else if (contentTypes.contains(coverFile.getContentType())) {
            errors.rejectValue("coverFile", "label.upload.invalid.file.type");
        } else if (coverFile.getSize() > TEN_MB_IN_BYTES) {
            errors.rejectValue("coverFile", "label.upload.exceeded.file.size");
        }

    }
}
