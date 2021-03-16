package com.T05.krowdtrialz.ui.experimentDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExperimentStatistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExperimentStatistics extends Fragment {
    public static final String tabName = "Stats";

    private Experiment experiment = null;

    private TextView mean;
    private TextView stdev;
    private TextView median;

    public ExperimentStatistics() {
        // Required empty public constructor
    }

    /**
     * Inject experiment into the fragment
     * @param experiment
     */
    public void injectExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param experiment
     * @return A new instance of fragment ExperimentStatistics.
     */
    public static ExperimentStatistics newInstance(Experiment experiment) {
        ExperimentStatistics fragment = new ExperimentStatistics();
        fragment.injectExperiment(experiment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_experiment_statistics, container, false);

        populateTrialResults(root);

        return root;
    }

    /**
     * This method fills out Mean, Standard deviation, Median and maybe some trial results
     * @author
     *  Ricky Au
     */
    private void populateTrialResults(View view) {
        // fill out Mean
        mean = view.findViewById(R.id.mean);
        // TODO: get the mean of experiment

        // TODO: get the standard deviation of experiment
        // fill out Standard deviation
        stdev = view.findViewById(R.id.stddev);

        // TODO: get mean added to xml then
        median = view.findViewById(R.id.median);

    }
}