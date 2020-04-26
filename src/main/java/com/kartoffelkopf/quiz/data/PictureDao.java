package com.kartoffelkopf.quiz.data;

import com.kartoffelkopf.quiz.model.Picture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureDao extends CrudRepository<Picture, Long> {
}
