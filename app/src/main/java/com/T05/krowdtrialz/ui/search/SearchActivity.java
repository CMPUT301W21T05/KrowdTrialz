package com.T05.krowdtrialz.ui.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.util.Database;
import com.T05.krowdtrialz.util.ExperimentList;

import java.util.ArrayList;

public class SearchActivity extends Activity {

    private Database db;
    private ArrayAdapter<Experiment> experimentAdapter;
    private final String TAG = "SearchActivity";

    public SearchActivity() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        db = new Database();

        Button searchExperimentsButton = findViewById(R.id.search_experiments_button);

        ListView searchResultsList = findViewById(R.id.search_results_list);

        EditText searchEditText = findViewById(R.id.search_editText);

        experimentAdapter = new ExperimentList(this, new ArrayList<>());

        searchResultsList.setAdapter(experimentAdapter);

        searchExperimentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchEditText.getText().toString();

                experimentAdapter.clear();

                if (searchString.isEmpty()) {
                    Log.e(TAG, "No Query");
                    return;
                }

                Log.e(TAG, searchString);
                db.getExperimentsByDescription(searchString, new Database.QueryExperimentsCallback() {
                    @Override
                    public void onSuccess(ArrayList<Experiment> experiments) {
                        Log.d(TAG, "Got search results" + experiments.toString());
                        experimentAdapter.addAll(experiments);
                        experimentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "Error Searching Database");
                    }
                });
            }
        });
    }
}