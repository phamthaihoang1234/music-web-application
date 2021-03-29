package com.codegym.music.exception;

public class SongNotFoundException extends RuntimeException {

    public SongNotFoundException(Long id) {
        super("Could not find song " + id);
    }
}
