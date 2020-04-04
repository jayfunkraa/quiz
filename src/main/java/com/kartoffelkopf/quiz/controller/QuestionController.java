package com.kartoffelkopf.quiz.controller;

import com.kartoffelkopf.quiz.model.Question;
import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.model.Round;
import com.kartoffelkopf.quiz.service.QuestionService;
import com.kartoffelkopf.quiz.service.RoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

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
                               @RequestParam long questionId,
                               @RequestParam String questionText,
                               @RequestParam String answerText) {
        Question question = questionService.findById(questionId).get();
        question.setQuestionText(questionText);
        question.setAnswerText(answerText);
        questionService.save(question);
        return "redirect:/quiz/edit/" + Long.toString(quizId);
    }

    @RequestMapping(value = "/question/delete", method = RequestMethod.POST)
    public String deleteQuestion(@RequestParam long delQuestionId, @RequestParam long delQuizId) {
        Question question = questionService.findById(delQuestionId).get();
        roundService.removeQuestion(questionService.getRound(question), question);
        questionService.delete(question);
        return "redirect:/quiz/edit/" + Long.toString(delQuizId);
    }
}
