package com.kartoffelkopf.quiz.service;

import com.kartoffelkopf.quiz.data.PictureDao;
import com.kartoffelkopf.quiz.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    private PictureDao pictureDao;

    @Override
    public Iterable<Picture> findAll() {
        return pictureDao.findAll();
    }

    @Override
    public Optional<Picture> findById(long id) {
        return pictureDao.findById(id);
    }

    @Override
    public Picture save(Picture picture) {
        return pictureDao.save(picture);
    }

    @Override
    public void delete(Picture picture) {
        pictureDao.delete(picture);
    }
}
