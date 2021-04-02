package com.T05.krowdtrialz.model.QnA;

import com.T05.krowdtrialz.model.user.User;

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
}
