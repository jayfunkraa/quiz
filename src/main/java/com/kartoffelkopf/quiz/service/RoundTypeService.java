package com.kartoffelkopf.quiz.service;

import com.kartoffelkopf.quiz.model.RoundType;

import java.util.Optional;

public interface RoundTypeService {
    Iterable<RoundType> findAll();
    Optional<RoundType> findById(long id);
    RoundType save(RoundType roundType);
    void delete(RoundType roundType);
}
