package com.T05.krowdtrialz.ui.contributors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.ui.subscribed.SubscribedFragment;
import com.T05.krowdtrialz.util.Database;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class ContributorsActivity extends AppCompatActivity {

    private static final String TAG = "CONTRIBUTORS ACTIVITY";

    ListView contributorsList;
    ArrayAdapter<User> contributorsArrayAdapter;
    ArrayList<User> contributorsDataList;

    private Experiment currentExperiment;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributors);
        setTitle("Contributors");

        db = Database.getInstance();

        contributorsList = findViewById(R.id.contributors_listView);
        contributorsDataList = new ArrayList<User>();

        Intent intent = getIntent();
        String expID = intent.getStringExtra(MainActivity.EXTRA_EXPERIMENT_ID);

        db.getExperimentByIDNotLive(expID, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment experiment) {
                currentExperiment = experiment;

                contributorsArrayAdapter = new ContributorList(ContributorsActivity.this, contributorsDataList, currentExperiment);

                contributorsList.setAdapter(contributorsArrayAdapter);
                updateContributorsList(experiment);
            }

            @Override
            public void onFailure() {
                currentExperiment = null;
            }
        });

    }

    /**
     * This fetches a list of contributors for an experiment from the database.
     *
     * @param exp
     */
    private void updateContributorsList(Experiment exp){
        contributorsArrayAdapter.clear();
        ArrayList<String> userIDs = new ArrayList<String>();
        userIDs.addAll(exp.getContributorsIDs());
        Log.d(TAG, userIDs.toString());
        for(String id : userIDs){
            Log.d(TAG, id);
            db.getUserByIdNotLive(id, new Database.GetUserCallback() {
                @Override
                public void onSuccess(User user) {
                    contributorsArrayAdapter.add(user);
                    contributorsArrayAdapter.notifyDataSetChanged();
                    Log.d(TAG,"got a contributor");
                }

                @Override
                public void onFailure() {
                    Log.e(TAG,"Could not get a contributor");
                }
            });
        }
    }
}