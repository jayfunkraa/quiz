package com.kartoffelkopf.quiz.service;

import com.kartoffelkopf.quiz.data.RoundDao;
import com.kartoffelkopf.quiz.model.Question;
import com.kartoffelkopf.quiz.data.QuestionDao;
import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.model.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private RoundDao roundDao;

    @Override
    public Iterable<Question> findAll() {
        return questionDao.findAll();
    }

    @Override
    public Optional<Question> findById(long id) {
        return questionDao.findById(id);
    }

    @Override
    public Question save(Question question) {
        return questionDao.save(question);
    }

    @Override
    public void delete(Question question) {
        questionDao.delete(question);
    }

    @Override
    public Round getRound(Question question) {
        for (Round r : roundDao.findAll()) {
            for (Question q : r.getQuestions()) {
                if (q.getId() == question.getId()) {
                    return r;
                }
            }
        }
        return null;
    }
}
