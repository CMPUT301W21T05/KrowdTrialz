package com.T05.krowdtrialz;

import com.T05.krowdtrialz.model.QnA.Answer;
import com.T05.krowdtrialz.model.QnA.Question;
import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.CountTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.user.User;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class QnATest {
    public User mockOwner () {
        User user = new User("Joe Bob","jbeast","jb@gmail.com","123");
        return user;
    }

    public User mockExperimenter (){
        User user = new User("Random Name","RN","rn@gmail.com","999");
        return user;
    }

    public Answer mockAnswer1(){
        Answer answer = new Answer("answer", mockOwner());
        return answer;
    }

    public Answer mockAnswer2(){
        Answer answer = new Answer("another answer", mockExperimenter());
        return answer;
    }

    public Answer mockAnswer3(){
        Answer answer = new Answer("", mockExperimenter());
        return answer;
    }

    public Question mockQuestion1(){
        Question question = new Question("Asking a Question?", mockExperimenter());
        return question;
    }

    // test getting and setting answers
    @Test
    public void testAnswer(){
        Answer answer = mockAnswer1();
        assertEquals(answer.getAnswer(), "answer");

        answer.setAnswer("different answer");
        assertEquals(answer.getAnswer(), "different answer");
    }

    // test getting and setting Responders
    @Test
    public void testResponder(){
        User responder = mockOwner();
        Answer answer = new Answer("testing Responder", responder);
        assertEquals(answer.getResponder(), responder);

        User responder1 = mockExperimenter();
        answer.setResponder(responder1);
        assertEquals(answer.getResponder(), responder1);
    }

    // test getting and setting questions
    @Test
    public void testQuestion(){
        Question question = mockQuestion1();
        assertEquals(question.getQuestion(), "Asking a Question?");

        question.setQuestion("Can I change question?");
        assertEquals(question.getQuestion(), "Can I change question?");
    }

    // testing adding questions and getting array of answers
    @Test
    public void testGetAnswers(){
        Question question = mockQuestion1();
        Answer answer1 = mockAnswer1();
        Answer answer2 = mockAnswer2();
        Answer answer3 = mockAnswer3();
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        question.addAnswer(answer3);

        ArrayList<Answer> ansList = new ArrayList<>();
        ansList.add(answer1);
        ansList.add(answer2);
        ansList.add(answer3);

        assertEquals(question.getAnswers(), ansList);
    }

    // testing AskedBy setter and getter
    @Test
    public void testAskedBy(){
        User user = mockExperimenter();
        Question question = new Question("Asked by?", user );
        assertEquals(question.getAskedBy(), user );

        User otherUser = mockOwner();
        question.setAskedBy(otherUser);
        assertEquals(question.getAskedBy(), otherUser);
    }

}
