package com.kartoffelkopf.quiz.controller;

import com.itextpdf.text.DocumentException;
import com.kartoffelkopf.quiz.model.Picture;
import com.kartoffelkopf.quiz.model.Question;
import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.model.Round;
import com.kartoffelkopf.quiz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RoundController {

    @Autowired
    private RoundService roundService;

    @Autowired
    private RoundTypeService roundTypeService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private QuizService quizService;

    @RequestMapping("/round/{id}/add-question")
    public String addQuestionForm(@PathVariable long id, Model model) {
        Round round = roundService.findById(id).get();
        model.addAttribute("quiz", roundService.getQuiz(round));
        model.addAttribute("round", round);
        Question question = new Question();
        question.setQuestionNumber(roundService.getNextQuestionNumber(round));
        model.addAttribute("question", question);
        return "round/add-question";
    }

    @RequestMapping(value = "/round/{id}/add-question", method = RequestMethod.POST)
    public String addQuestion(@PathVariable long id,
                              Question question,
                              @RequestParam(required = false) MultipartFile file) {
        Quiz quiz = roundService.getQuiz(roundService.findById(id).get());

        if (file != null && !file.isEmpty()) {
            try {
                Picture picture = new Picture(file.getOriginalFilename(), file.getContentType(), file.getBytes());
                pictureService.save(picture);
                question.setPicture(picture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        questionService.save(question);
        roundService.addQuestion(id, question);
        return "redirect:/quiz/edit/" + Long.toString(quiz.getId());
    }

    @RequestMapping("/round/edit/{id}")
    public String editForm(@PathVariable long id, Model model) {
        Round round = roundService.findById(id).get();
        model.addAttribute("quiz", roundService.getQuiz(round));
        model.addAttribute("round", round);
        model.addAttribute("roundTypes", roundTypeService.findAll());

        List<Long> questionIds = new ArrayList<>();
        for (Question q : round.getQuestions()) {
            questionIds.add(q.getId());
        }
        model.addAttribute("questionIds", questionIds.toString().replaceAll("[\\[\\]\\s]+", ""));
        return "round/edit";
    }

    @RequestMapping(value = "/round/save", method = RequestMethod.POST)
    public String edit(@RequestParam long quizId,
                       @RequestParam String questionIds,
                       @RequestParam long roundTypeId,
                       Round round) {
        if (!questionIds.isEmpty()) {
            String[] questionIdArray = questionIds.split(",");
            List<Question> questions = new ArrayList<>();
            for (String id : questionIdArray) {
                Question question = questionService.findById(Long.parseLong(id)).get();
                questions.add(question);
            }
            round.setQuestions(questions);
        }
        round.setRoundType(roundTypeService.findById(roundTypeId).get());
        roundService.save(round);
        return "redirect:/quiz/edit/" + Long.toString(quizId);
    }

    @RequestMapping(value = "/round/delete", method = RequestMethod.POST)
    public String deleteForm(@RequestParam long delRoundId, @RequestParam long delQuizId) {
        Round round = roundService.findById(delRoundId).get();
        for (Question q : round.getQuestions()) {
            roundService.removeQuestion(round, q);
            questionService.delete(q);
        }
        quizService.removeRound(roundService.getQuiz(round), round);
        roundService.delete(round);
        return "redirect:/quiz/edit/" + Long.toString(delQuizId);
    }

    @RequestMapping("/round/download/{id}")
    public ResponseEntity<byte[]> getAnswerSheet(@PathVariable long id) {
        Round round = roundService.findById(id).get();
        ResponseEntity<byte[]> response = null;
        try {
            response = roundService.generatePdf(round);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return response;
    }
}
