package com.T05.krowdtrialz.ui.subscribed;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.util.Database;

import java.util.ArrayList;

public class SubscribedViewModel extends ViewModel {

    private Database db;
    private MutableLiveData<ArrayList<Experiment>> experiments;
    private static final String TAG = "SubscribedViewModel";

    public SubscribedViewModel() {
        db = Database.getInstance();
        experiments = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Experiment>> getExperimentList(){
        db.getExperimentsBySubscriber(db.getDeviceUser(), new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
                experiments.clear();
                experiments.addAll(experiments);
                Log.d(TAG, "Got query results: " + experiments.toString());
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "Error Querying database.");
            }
        });
        return experiments;
    }

}