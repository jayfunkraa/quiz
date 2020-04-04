package com.kartoffelkopf.quiz.service;

import com.itextpdf.text.DocumentException;
import com.kartoffelkopf.quiz.model.Question;
import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.model.Round;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RoundService {
    Iterable<Round> findAll();
    Optional<Round> findById(long id);
    Round save(Round round);
    void delete(Round round);
    Round addQuestion(long roundId, Question question);
    Quiz getQuiz(Round round);
    Round removeQuestion(Round round, Question question);
    int getNextQuestionNumber(Round round);
    ResponseEntity<byte[]> generatePdf(Round round) throws IOException, DocumentException;
}
