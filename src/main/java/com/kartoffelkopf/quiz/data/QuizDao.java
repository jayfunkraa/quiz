package com.kartoffelkopf.quiz.data;

import com.kartoffelkopf.quiz.model.Quiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizDao extends CrudRepository<Quiz, Long> {
}
