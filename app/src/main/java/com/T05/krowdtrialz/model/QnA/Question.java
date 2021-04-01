package com.T05.krowdtrialz.model.QnA;

import com.T05.krowdtrialz.model.user.User;

import java.util.ArrayList;
/**
 *  This class represents a question
 */
public class Question {
    private String question;
    private ArrayList<Answer> answer;
    private User askedBy;

    public Question() { }

    public Question(String question,  User askedBy) {
        this.question = question;
        this.answer = new ArrayList<Answer>();
        this.askedBy = askedBy;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Answer> getAnswers() {
        return answer;
    }

    public void addAnswer(Answer answer) {
        this.answer.add(answer);
    }

    public User getAskedBy() {
        return askedBy;
    }

    public void setAskedBy(User askedBy) {
        this.askedBy = askedBy;
    }
}
