package com.T05.krowdtrialz.ui.subscribed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;


import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.ui.ExperimentDetailsNonOwnerActivity;
import com.T05.krowdtrialz.ui.ExperimentDetailsOwnerActivity;
import com.T05.krowdtrialz.util.Database;
import com.T05.krowdtrialz.util.ExperimentList;

import org.apache.commons.math3.analysis.function.Exp;

import java.util.ArrayList;



public class SubscribedFragment extends Fragment {

    public static final String EXTRA_EXPERIMENT_ID = "com.T05.krowdtrialz.ui.EXPERIMENT_ID";
    private SubscribedViewModel subscribedViewModel;

    ListView experimentsList;
    ArrayAdapter<Experiment> experimentArrayAdapter;
    ArrayList<Experiment> experimentsDataList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subscribedViewModel = new ViewModelProvider(this).get(SubscribedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscribed, container, false);


        experimentsList = root.findViewById(R.id.subscribed_exp_listView);

        experimentsDataList = new ArrayList<Experiment>();
        experimentArrayAdapter = new ExperimentList(root.getContext(), experimentsDataList);
        experimentsList.setAdapter(experimentArrayAdapter);

        subscribedViewModel.getExperimentList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Experiment>>() {
            @Override
            public void onChanged(ArrayList<Experiment> experiments) {
                experimentArrayAdapter.clear();
                experimentArrayAdapter.addAll(experiments);
            }
        });

        User deviceUser = Database.getInstance().getDeviceUser();

        experimentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Go to experiment details screen (Owner or non-Owner)
                Experiment currentExperiment = experimentArrayAdapter.getItem(position);
                Intent intent;

                if(currentExperiment.isOwner(deviceUser)){
                    // If user is owner
                    intent = new Intent(root.getContext(), ExperimentDetailsOwnerActivity.class);
                } else {
                    // If user is not owner
                    intent = new Intent(root.getContext(), ExperimentDetailsNonOwnerActivity.class);
                }

                intent.putExtra(EXTRA_EXPERIMENT_ID, currentExperiment.getId());
                startActivity(intent);
            }
        });

        return root;
    }
}