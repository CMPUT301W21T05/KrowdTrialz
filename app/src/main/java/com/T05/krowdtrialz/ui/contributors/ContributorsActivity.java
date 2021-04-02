package com.T05.krowdtrialz.ui.contributors;

import androidx.appcompat.app.AppCompatActivity;

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

        db = Database.getInstance();

        setContentView(R.layout.activity_contributors);

        contributorsList = findViewById(R.id.contributors_listView);

        Intent intent = getIntent();
        String expID = intent.getStringExtra(MainActivity.EXTRA_EXPERIMENT_ID);
        db.getExperimentByIDNotLive(expID, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment experiment) {
                currentExperiment = experiment;
                contributorsDataList = new ArrayList<User>();

                contributorsArrayAdapter = new ContributorList(ContributorsActivity.this, contributorsDataList, currentExperiment, db);

                contributorsList.setAdapter(contributorsArrayAdapter);

                for (User u: currentExperiment.getContributors()) {

                    db.getUserByIdNotLive(u.getId(), new Database.GetUserCallback() {
                        @Override
                        public void onSuccess(User user) {
                            contributorsDataList.add(user);
                            contributorsArrayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure() {
                            Log.e(TAG, "COULD NOT GET USERS");
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
                currentExperiment = null;
            }
        });
    }
}