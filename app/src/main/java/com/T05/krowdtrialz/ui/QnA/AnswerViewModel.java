package com.T05.krowdtrialz.ui.QnA;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.T05.krowdtrialz.model.QnA.Answer;
import com.T05.krowdtrialz.model.QnA.Question;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
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
        answerList.setValue(new ArrayList<>());
    }

    /**
     * Get list of ansers to the current question
     * @param expId
     *   Experiment of interest
     * @param position
     *   Answer's position in list
     * @return
     *   Live updating list of answers
     */
    public LiveData<ArrayList<Answer>> getAnswerList(String expId, int position){
        db = Database.getInstance();
        expRegistration = db.getExperimentByID(expId, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment experiment) {
                Log.d(TAG, "Got experiment query result: " + experiment.toString());

                // Get up to date user info for each answer
                for (Answer answer: experiment.getQuestions().get(position).getAnswers()) {
                    db.getUserByIdNotLive(answer.getResponder().getId(), new Database.GetUserCallback() {
                        @Override
                        public void onSuccess(User user) {
                            answer.setResponder(user);
                            ArrayList<Answer> tmpList = answerList.getValue();
                            if (tmpList.contains(answer)) {
                                // avoid duplicate answers
                                tmpList.remove(answer);
                            }
                            tmpList.add(answer);
                            answerList.setValue(tmpList);
                        }

                        @Override
                        public void onFailure() {
                            Log.e(TAG, "failed to get responder with id: " + answer.getResponder().getId());
                        }
                    });
                }
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