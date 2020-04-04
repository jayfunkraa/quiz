package com.kartoffelkopf.quiz.service;

import com.kartoffelkopf.quiz.model.Question;
import com.kartoffelkopf.quiz.model.Round;

import java.util.Optional;

public interface QuestionService {
    Iterable<Question> findAll();
    Optional<Question> findById(long id);
    Question save(Question question);
    void delete(Question question);
    Round getRound(Question question);
}
