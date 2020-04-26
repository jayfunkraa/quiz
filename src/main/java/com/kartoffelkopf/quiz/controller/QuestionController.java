package com.kartoffelkopf.quiz.controller;

import com.kartoffelkopf.quiz.model.Picture;
import com.kartoffelkopf.quiz.model.Question;
import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.model.Round;
import com.kartoffelkopf.quiz.service.PictureService;
import com.kartoffelkopf.quiz.service.QuestionService;
import com.kartoffelkopf.quiz.service.RoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private RoundService roundService;

    @RequestMapping("/question/{id}/edit")
    public String editQuestion(@PathVariable long id, Model model) {
        Question question = questionService.findById(id).get();
        Round round = questionService.getRound(question);
        model.addAttribute("question", question);
        model.addAttribute("round", round);
        model.addAttribute("quiz", roundService.getQuiz(round));
        return "question/edit";
    }

    @RequestMapping(value = "/question/save", method = RequestMethod.POST)
    public String saveQuestion(@RequestParam long quizId,
                               @RequestParam(required = false) MultipartFile file,
                               Question question) {
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
        return "redirect:/quiz/edit/" + Long.toString(quizId);
    }

    @RequestMapping(value = "/question/delete", method = RequestMethod.POST)
    public String deleteQuestion(@RequestParam long delQuestionId, @RequestParam long delQuizId) {
        Question question = questionService.findById(delQuestionId).get();
        Picture picture = question.getPicture();
        roundService.removeQuestion(questionService.getRound(question), question);
        questionService.delete(question);
        if (picture != null) {
            pictureService.delete(picture);
        }
        return "redirect:/quiz/edit/" + Long.toString(delQuizId);
    }

    //TODO: Possibly move this to its own controller
    @RequestMapping("/picture/get/{id}")
    public ResponseEntity<byte[]> getPicture(@PathVariable long id) {
        Picture picture = pictureService.findById(id).get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(picture.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(picture.getFilename()).build());
        return new ResponseEntity<>(picture.getBytes(), headers, HttpStatus.OK);
    }
}
