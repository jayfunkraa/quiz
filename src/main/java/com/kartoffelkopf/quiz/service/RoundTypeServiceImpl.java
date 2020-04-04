package com.kartoffelkopf.quiz.service;

import com.kartoffelkopf.quiz.data.RoundTypeDao;
import com.kartoffelkopf.quiz.model.RoundType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoundTypeServiceImpl implements RoundTypeService {

    @Autowired
    private RoundTypeDao roundTypeDao;


    @Override
    public Iterable<RoundType> findAll() {
        return roundTypeDao.findAll();
    }

    @Override
    public Optional<RoundType> findById(long id) {
        return roundTypeDao.findById(id);
    }

    @Override
    public RoundType save(RoundType roundType) {
        return roundTypeDao.save(roundType);
    }

    @Override
    public void delete(RoundType roundType) {
        roundTypeDao.delete(roundType);
    }
}
