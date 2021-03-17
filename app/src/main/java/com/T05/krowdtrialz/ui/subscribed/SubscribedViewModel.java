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
    private MutableLiveData<ArrayList<Experiment>> experimentsList;
    private static final String TAG = "SubscribedViewModel";

    public SubscribedViewModel() {
        db = Database.getInstance();
        experimentsList = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Experiment>> getExperimentList(){
        db = Database.getInstance();
        db.getExperimentsBySubscriber(db.getDeviceUser(), new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
                ArrayList<Experiment> newList = new ArrayList<>();
                newList.addAll(experiments);
                experimentsList.setValue(newList);
                Log.d(TAG, "Got query results: " + experiments.toString());
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "Error Querying database.");
            }
        });
        return experimentsList;
    }

}