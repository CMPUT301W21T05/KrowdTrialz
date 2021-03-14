package com.T05.krowdtrialz.ui.publish;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.ui.subscribed.SubscribedViewModel;
import com.T05.krowdtrialz.util.Database;

public class PublishViewModel extends ViewModel {

    private Database db;
    private static final String TAG = "SubscribedViewModel";

    public PublishViewModel() {
        db = Database.getInstance();
    }

    public void sendExperimentToDatabase(Experiment experiment) {
        Log.d(TAG, "Calling Database.addExperiment");
        db.addExperiment(experiment);
    }
}