package com.T05.krowdtrialz.ui.search;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.ui.experimentDetails.ExperimentDetailsActivity;
import com.T05.krowdtrialz.util.Database;
import com.T05.krowdtrialz.util.ExperimentList;

import java.util.ArrayList;
import java.util.Arrays;

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
        db = Database.getInstance();

        searchExperimentsQuery = findViewById(R.id.search_experiment_query);

        searchResultsList = findViewById(R.id.search_results_list);

        experimentAdapter = new ExperimentList(this, new ArrayList<>(), db.getDeviceUser());

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

    /**
     * Pass query to database and add results to experiment ListView
     *
     * @param searchString
     *  String with search query
     */
    public void searchExperiments(String searchString) {
        experimentAdapter.clear();

        Log.e(TAG, "Query: " + searchString);
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(searchString.split("[^A-Za-z1-9]")));

        db.getExperimentsByTags(tags, new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
                Log.d(TAG, "Got search results" + experiments.toString());
                experimentAdapter.clear();
                experimentAdapter.addAll(experiments);

                /*
                 * pass the clicked experiment to the Experiment details page
                 */
                searchResultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "clicked position" + position);

                        Intent i = new Intent(SearchActivity.this, ExperimentDetailsActivity.class);

                        Experiment experiment = experimentAdapter.getItem(position);

                        String experimentID = experiment.getId();

                        i.putExtra("experiment", experimentID);
                        startActivity(i);



                    }
                });
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "Error Searching Database");
            }
        });
    }
}