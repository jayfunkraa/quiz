package com.kartoffelkopf.quiz.data;

import com.kartoffelkopf.quiz.model.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionDao extends CrudRepository<Question, Long> {
}
