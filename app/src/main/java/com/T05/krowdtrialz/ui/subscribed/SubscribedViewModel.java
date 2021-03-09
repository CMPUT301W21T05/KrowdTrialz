package com.T05.krowdtrialz.ui.subscribed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.util.Database;

import java.util.ArrayList;

public class SubscribedViewModel extends ViewModel {

    private Database db;
    private MutableLiveData<ArrayList<Experiment>> experiments;

    public SubscribedViewModel() {
        db = new Database();
        experiments = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Experiment>> getExperimentList(){
        // TODO: implement this
        //db.getExperimentsBySubscriber();
        return experiments;
    }

}