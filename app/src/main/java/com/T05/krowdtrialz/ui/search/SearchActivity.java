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
import android.widget.SearchView;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.util.Database;
import com.T05.krowdtrialz.util.ExperimentList;

import java.util.ArrayList;

public class SearchActivity extends Activity implements SearchView.OnQueryTextListener{

    private Database db;
    private ArrayAdapter<Experiment> experimentAdapter;
    private final String TAG = "SearchActivity";
    private SearchView searchExperimentsQuery;
    private ListView searchResultsList;

    public SearchActivity() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        db = new Database();

        searchExperimentsQuery = findViewById(R.id.search_experiment_query);

        searchResultsList = findViewById(R.id.search_results_list);

        experimentAdapter = new ExperimentList(this, new ArrayList<>());

        searchResultsList.setAdapter(experimentAdapter);

        searchExperimentsQuery.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchExperiments(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchExperiments(newText);
        return true;
    }

    public void searchExperiments(String searchString) {
        experimentAdapter.clear();

        Log.e(TAG, "Query: " + searchString);
        db.getExperimentsByDescription(searchString, new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
                Log.d(TAG, "Got search results" + experiments.toString());
                experimentAdapter.clear();
                experimentAdapter.addAll(experiments);
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "Error Searching Database");
            }
        });
    }
}