package com.T05.krowdtrialz.ui.contributors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.ui.subscribed.SubscribedFragment;
import com.T05.krowdtrialz.util.Database;

import java.util.ArrayList;

public class ContributorsActivity extends AppCompatActivity {

    ListView contributorsList;
    ArrayAdapter<User> contributorsArrayAdapter;
    ArrayList<User> contributorsDataList;

    private Experiment currentExperiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributors);

        contributorsList = findViewById(R.id.contributors_listView);

        Intent intent = getIntent();
        String expID = intent.getStringExtra(MainActivity.EXTRA_EXPERIMENT_ID);
        Database.getInstance().getExperimentByID(expID, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment experiment) {
                currentExperiment = experiment;
            }

            @Override
            public void onFailure() {
                currentExperiment = null;
            }
        });
        contributorsDataList = new ArrayList<User>();
        if(currentExperiment != null){
            contributorsDataList.addAll(currentExperiment.getIgnoredUsers());
        }

        contributorsArrayAdapter = new ContributorList(this, contributorsDataList, currentExperiment);
        contributorsList.setAdapter(contributorsArrayAdapter);
    }
}