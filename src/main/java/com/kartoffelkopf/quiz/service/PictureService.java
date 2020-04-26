package com.kartoffelkopf.quiz.service;

import com.kartoffelkopf.quiz.model.Picture;

import java.util.Optional;

public interface PictureService {
    Iterable<Picture> findAll();
    Optional<Picture> findById(long id);
    Picture save(Picture picture);
    void delete(Picture picture);
}
