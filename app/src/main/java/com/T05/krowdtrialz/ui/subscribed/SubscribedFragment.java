package com.T05.krowdtrialz.ui.subscribed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;


import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;
import com.T05.krowdtrialz.util.ExperimentList;

import java.util.ArrayList;



public class SubscribedFragment extends Fragment {

    private SubscribedViewModel subscribedViewModel;

    ListView experimentsList;
    ArrayAdapter<Experiment> experimentArrayAdapter;
    ArrayList<Experiment> experimentsDataList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subscribedViewModel = new ViewModelProvider(this).get(SubscribedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscribed, container, false);


        experimentsList = root.findViewById(R.id.subscribed_exp_listView);

        User deviceUser = Database.getInstance().getDeviceUser();

        experimentsDataList = new ArrayList<Experiment>();
        experimentArrayAdapter = new ExperimentList(root.getContext(), experimentsDataList, deviceUser);
        experimentsList.setAdapter(experimentArrayAdapter);

        subscribedViewModel.getExperimentList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Experiment>>() {
            @Override
            public void onChanged(ArrayList<Experiment> experiments) {
                experimentArrayAdapter.clear();
                experimentArrayAdapter.addAll(experiments);
            }
        });





        return root;
    }
}