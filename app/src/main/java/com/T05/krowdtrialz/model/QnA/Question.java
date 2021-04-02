package com.T05.krowdtrialz.model.QnA;

import com.T05.krowdtrialz.model.user.User;

import java.util.ArrayList;
/**
 *  This class represents a question
 */
public class Question {
    private String question;
    private ArrayList<Answer> answers;
    private User askedBy;

    public Question() { }

    public Question(String question,  User askedBy) {
        this.question = question;
        this.answers = new ArrayList<Answer>();
        this.askedBy = askedBy;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        if (this.answers == null){
            this.answers = new ArrayList<Answer>();
        }
        this.answers.add(answer);
    }

    public User getAskedBy() {
        return askedBy;
    }

    public void setAskedBy(User askedBy) {
        this.askedBy = askedBy;
    }
}
