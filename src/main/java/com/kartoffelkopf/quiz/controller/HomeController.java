package com.kartoffelkopf.quiz.controller;

import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private QuizService quizService;

    @RequestMapping("/")
    public String home(Model model) {
        List<Quiz> top3Quizzes = new ArrayList<>();
        for (Quiz q : quizService.findAll()) {
            if (top3Quizzes.size() < 3) {
                top3Quizzes.add(q);
            }
        }
        model.addAttribute("quizzes", top3Quizzes);
        return "index";
    }
}
