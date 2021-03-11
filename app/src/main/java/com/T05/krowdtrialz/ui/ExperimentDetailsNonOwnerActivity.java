package com.T05.krowdtrialz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.user.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ExperimentDetailsNonOwnerActivity extends AppCompatActivity {

    private Experiment experiment;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_details_non_owner);

        barChart = findViewById(R.id.histogram_owner);

        // Get experiment object
        Bundle extras = getIntent().getExtras();
        experiment = (Experiment) extras.get("experiment");

        // Get array of data from experiment
        List<BarEntry> entries = new ArrayList<BarEntry>();
        if (experiment.getType() == "Binomial") { // Binomial format: {successCount, failureCount}
            // make list of data
            ArrayList<Integer> dataPoints = new ArrayList<Integer>();
            BinomialExperiment binomialExperiment = (BinomialExperiment) extras.get("experiment");
            dataPoints.add(binomialExperiment.getSuccessCount());
            dataPoints.add(binomialExperiment.getFailureCount());

            // make list of entries
            for (int i = 0; i < dataPoints.size(); i++) {
                // turn your data into Entry objects
                entries.add(new BarEntry(i, dataPoints.get(i)));
            }
        }else if (experiment.getType() == "Count"){ // Count format: {Count}
            // make list of data
            ArrayList<Integer> dataPoints = new ArrayList<Integer>();
            dataPoints.add(experiment.getTrials().size());

            // make list of entries
            for (int i = 0; i < dataPoints.size(); i++) {
                // turn your data into Entry objects
                entries.add(new BarEntry(i, dataPoints.get(i)));
            }
        }
        else if (experiment.getType() == "Integer"){ // Integer format: list of data points
            // make list of data
            IntegerExperiment integerExperiment = (IntegerExperiment) extras.get("experiment");
            double[] temp = integerExperiment.getTrials().stream()
                    .mapToDouble(trial -> ((IntegerTrial) trial).getValue())
                    .toArray();

            ArrayList<Integer> dataPoints = new ArrayList<Integer>();
            for(int i=0; i<temp.length; i++) {
                dataPoints.add((int) temp[i]);
            }

            // make dictionary that stores the frequency of each unique data point
            Hashtable<Integer, Integer> uniqueDataPoints = new Hashtable<Integer, Integer>();
            for (int i = 0; i < dataPoints.size(); i++) {
                // Count the number of occurrences of each data point
                if (uniqueDataPoints.containsKey(dataPoints.get(i))) {
                    uniqueDataPoints.put(dataPoints.get(i), uniqueDataPoints.get(dataPoints.get(i)+1));
                }else{
                    uniqueDataPoints.put(dataPoints.get(i), 1);
                }
            }

            // make list of entries
            for (int i : uniqueDataPoints.keySet()) {
                // turn your data into Entry objects
                entries.add(new BarEntry(i, uniqueDataPoints.get(i)));
            }

        }else if (experiment.getType() == "Measurement") {
            // make list of data
            MeasurementExperiment measurementExperiment = (MeasurementExperiment) extras.get("experiment");
            double[] temp = measurementExperiment.getTrials().stream()
                    .mapToDouble(trial -> ((MeasurementTrial) trial).getMeasurementValue())
                    .toArray();

            ArrayList<Double> dataPoints = new ArrayList<Double>();
            for(int i=0; i<temp.length; i++) {
                dataPoints.add(temp[i]);
            }

            // make dictionary that stores the frequency of each unique data point
            Hashtable<Double, Integer> uniqueDataPoints = new Hashtable<Double, Integer>();
            for (int i = 0; i < dataPoints.size(); i++) {
                // Count the number of occurrences of each data point
                if (uniqueDataPoints.containsKey(dataPoints.get(i))) {
                    uniqueDataPoints.put(dataPoints.get(i), uniqueDataPoints.get(dataPoints.get(i)+1));
                }else{
                    uniqueDataPoints.put(dataPoints.get(i), 1);
                }
            }

            // make list of entries
            for (double i : uniqueDataPoints.keySet()) {
                // turn your data into Entry objects
                entries.add(new BarEntry((float) i, (float) uniqueDataPoints.get(i)));
            }
        }

        // create data set
        BarDataSet barDataSet = new BarDataSet(entries, "Binomial Trials");
        BarData barData = new BarData(barDataSet);

        // add data to chart
        barChart.setData(barData);
        barChart.invalidate(); // Refreshes chart

    }
}