package com.kartoffelkopf.quiz.service;

import com.kartoffelkopf.quiz.data.QuizDao;
import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.model.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizDao quizDao;

    @Override
    public Iterable<Quiz> findAll() {
        return quizDao.findAll();
    }

    @Override
    public Optional<Quiz> findById(long id) {
        return quizDao.findById(id);
    }

    @Override
    public Quiz save(Quiz quiz) {
        return quizDao.save(quiz);
    }

    @Override
    public void delete(Quiz quiz) {
        quizDao.delete(quiz);
    }

    @Override
    public Quiz addRound(long quizId, Round round) {
        Quiz quiz = quizDao.findById(quizId).get();
        quiz.getRounds().add(round);
        return quizDao.save(quiz);
    }

    @Override
    public Quiz removeRound(Quiz quiz, Round round) {
        List<Round> rounds = quiz.getRounds();
        rounds.remove(round);
        quiz.setRounds(rounds);
        quizDao.save(quiz);
        return quiz;
    }


}
