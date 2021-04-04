package com.T05.krowdtrialz.ui.QnA;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.T05.krowdtrialz.model.QnA.Question;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class QuestionViewModel extends ViewModel {

    private Database db;
    private MutableLiveData<ArrayList<Question>> questionList;
    private static final String TAG = "QuestionViewModel";

    private ListenerRegistration expRegistration;

    public QuestionViewModel() {
        db = Database.getInstance();
        questionList = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Question>> getQuestionList(String expId){
        db = Database.getInstance();
        expRegistration = db.getExperimentByID(expId, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment experiment) {
                questionList.setValue(new ArrayList<>());
                Log.d(TAG, "Got experiment query result: " + experiment.toString());

                // Get up to date information about each user
                for (Question question: experiment.getQuestions()) {
                    db.getUserByIdNotLive(question.getAskedBy().getId(), new Database.GetUserCallback() {
                        @Override
                        public void onSuccess(User user) {
                            question.setAskedBy(user);
                            ArrayList<Question> tmpList = questionList.getValue();
                            tmpList.add(question);
                            questionList.setValue(tmpList);
                        }

                        @Override
                        public void onFailure() {
                            Log.d(TAG, "Failed to get asked by user with id: " + question.getAskedBy().getId());
                        }
                    });
                }
            }

            @Override
            public void onFailure() {

            }
        });
        return questionList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Stop listening to changes in the Database
        expRegistration.remove();
    }
}