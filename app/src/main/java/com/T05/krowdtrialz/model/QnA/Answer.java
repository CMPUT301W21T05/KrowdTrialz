package com.T05.krowdtrialz.model.QnA;

import com.T05.krowdtrialz.model.user.User;

import java.util.Objects;

/**
 *  This class represents an answer to a question
 */
public class Answer {
    private String answer;
    private User responder;

    public Answer() { }

    public Answer(String answer, User responder) {
        this.answer = answer;
        this.responder = responder;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public User getResponder() {
        return responder;
    }

    public void setResponder(User responder) {
        this.responder = responder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer1 = (Answer) o;
        return Objects.equals(answer, answer1.answer) &&
                Objects.equals(responder, answer1.responder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer, responder);
    }
}
