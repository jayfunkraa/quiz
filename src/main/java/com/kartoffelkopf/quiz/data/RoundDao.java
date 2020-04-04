package com.kartoffelkopf.quiz.data;

import com.kartoffelkopf.quiz.model.Round;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundDao extends CrudRepository<Round, Long> {
}
