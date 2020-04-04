package com.kartoffelkopf.quiz.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(length = 4000)
    private String description;

    private String quizmaster;
    private int questionTimeLimit;

    @OneToMany
    private List<Round> rounds;

    public Quiz() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuizmaster() {
        return quizmaster;
    }

    public void setQuizmaster(String quizmaster) {
        this.quizmaster = quizmaster;
    }

    public int getQuestionTimeLimit() {
        return questionTimeLimit;
    }

    public void setQuestionTimeLimit(int questionTimeLimit) {
        this.questionTimeLimit = questionTimeLimit;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }
}
