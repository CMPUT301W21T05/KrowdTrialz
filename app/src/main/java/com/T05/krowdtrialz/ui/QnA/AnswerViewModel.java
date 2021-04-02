package com.T05.krowdtrialz.ui.QnA;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.T05.krowdtrialz.model.QnA.Answer;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.util.Database;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class AnswerViewModel extends ViewModel {

    private Database db;
    private MutableLiveData<ArrayList<Answer>> answerList;
    private static final String TAG = "AnswerViewModel";

    private ListenerRegistration expRegistration;

    public AnswerViewModel() {
        db = Database.getInstance();
        answerList = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Answer>> getAnswerList(String expId, int position){
        db = Database.getInstance();
        expRegistration = db.getExperimentByID(expId, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment experiment) {
                ArrayList<Answer> newList = new ArrayList<>();
                newList.addAll(experiment.getQuestions().get(position).getAnswers());
                answerList.setValue(newList);
                Log.d(TAG, "Got query result: " + experiment.toString());
            }

            @Override
            public void onFailure() {

            }
        });
        return answerList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Stop listening to changes in the Database
        expRegistration.remove();
    }
}