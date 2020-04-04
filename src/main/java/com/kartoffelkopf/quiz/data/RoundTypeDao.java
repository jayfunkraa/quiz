package com.kartoffelkopf.quiz.data;

import com.kartoffelkopf.quiz.model.RoundType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundTypeDao extends CrudRepository<RoundType, Long> {
}
