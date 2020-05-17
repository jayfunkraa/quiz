package com.kartoffelkopf.quiz.service;

import com.itextpdf.text.DocumentException;
import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.model.Round;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Optional;

public interface QuizService {
    Iterable<Quiz> findAll();
    Optional<Quiz> findById(long id);
    Quiz save(Quiz quiz);
    void delete(Quiz quiz);
    Quiz addRound(long quizId, Round round);
    Quiz removeRound(Quiz quiz, Round round);
    ResponseEntity<byte[]> generatePdf(Quiz quiz) throws IOException, DocumentException;
}
