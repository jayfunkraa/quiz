package com.kartoffelkopf.quiz.controller;

import com.kartoffelkopf.quiz.model.Question;
import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.model.Round;
import com.kartoffelkopf.quiz.model.User;
import com.kartoffelkopf.quiz.service.QuestionService;
import com.kartoffelkopf.quiz.service.QuizService;
import com.kartoffelkopf.quiz.service.RoundService;
import com.kartoffelkopf.quiz.service.RoundTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private RoundService roundService;

    @Autowired
    private RoundTypeService roundTypeService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping("/quizzes")
    public String quizzes(Model model) {
        model.addAttribute("quizzes", quizService.findAll());
        return "quiz/quizzes";
    }

    @RequestMapping("/quiz/add")
    public String addForm(Model model) {
        model.addAttribute("quiz", new Quiz());
        return "quiz/add";
    }

    @RequestMapping(value = "/quiz/add", method = RequestMethod.POST)
    public String addPost(Quiz quiz, Principal principal) {
        User user = (User)((UsernamePasswordAuthenticationToken)principal).getPrincipal();
        quiz.setUser(user);
        quizService.save(quiz);
        return "redirect:/quizzes";
    }

    @RequestMapping("/quiz/edit/{id}")
    public String editForm(@PathVariable long id, Model model) {
        model.addAttribute("quiz", quizService.findById(id).get());
        return "quiz/edit";
    }

    @RequestMapping("/quiz/{id}/add-round")
    public String addRoundForm(@PathVariable long id, Model model) {
        model.addAttribute("quiz", quizService.findById(id).get());
        model.addAttribute("round", new Round());
        model.addAttribute("roundTypes", roundTypeService.findAll());
        return "/quiz/add-round";
    }

    @RequestMapping(value = "/quiz/{id}/add-round", method = RequestMethod.POST)
    public String addRound(@PathVariable long id, Round round) {
        roundService.save(round);
        quizService.addRound(id, round);
        return "redirect:/quiz/edit/" + Long.toString(id);
    }

    @RequestMapping("/quiz/edit-details/{id}")
    public String editDetailsForm(@PathVariable long id, Model model) {
        Quiz quiz = quizService.findById(id).get();
        model.addAttribute("quiz", quiz);

        List<Long> roundIds = new ArrayList<>();
        for (Round r : quiz.getRounds()) {
            roundIds.add(r.getId());
        }
        model.addAttribute("roundIds", roundIds.toString().replaceAll("[\\[\\]\\s]+", ""));
        return "quiz/edit-details";
    }

    @RequestMapping(value = "/quiz/save", method = RequestMethod.POST)
    public String editDetails(@RequestParam String roundIds, Quiz quiz) {
        if (!roundIds.isEmpty()) {
            String[] roundIdArray = roundIds.split(",");
            List<Round> rounds = new ArrayList<>();
            for (String id : roundIdArray) {
                Round round = roundService.findById(Long.parseLong(id)).get();
                rounds.add(round);
            }
            quiz.setRounds(rounds);
        }
        quizService.save(quiz);
        return "redirect:/quiz/edit/" + Long.toString(quiz.getId());
    }

    @RequestMapping(value = "/quiz/delete", method = RequestMethod.POST)
    public String delete(@RequestParam long delQuizId) {
        Quiz quiz = quizService.findById(delQuizId).get();

        List<Round> rounds = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        for (Round r : quiz.getRounds()) {
            questions.addAll(r.getQuestions());
            rounds.add(r);
        }

        for (Question q : questions) {
            roundService.removeQuestion(questionService.getRound(q), q);
            questionService.delete(q);
        }
        for (Round r : rounds) {
            quizService.removeRound(quiz, r);
            roundService.delete(r);
        }

        quizService.delete(quiz);
        return "redirect:/quizzes";
    }

    @RequestMapping("/my-quizzes")
    public String userquizzes(Principal principal, Model model) {
        User user = (User)((UsernamePasswordAuthenticationToken)principal).getPrincipal();
        List<Quiz> quizzes = new ArrayList<>();
        for (Quiz q : quizService.findAll()) {
            if (q.getUser().getId() == user.getId()) {
                quizzes.add(q);
            }
        }
        model.addAttribute("quizzes", quizzes);
        return "quiz/my-quizzes";
    }
}
