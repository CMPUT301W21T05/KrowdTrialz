package com.T05.krowdtrialz.ui.experimentDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.CountTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import org.apache.commons.math3.analysis.function.Exp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExperimentPlots#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExperimentPlots extends Fragment {

    public static final String tabName = "Plots", TAG = "EXPERIMENT PLOTS";

    private Experiment experiment = null;

    private BarChart barChart;
    private ScatterChart scatterChart;

    public ExperimentPlots() {
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
     * @param experiment Parameter 1.
     * @return A new instance of fragment ExperimentPlots.
     */
    public static ExperimentPlots newInstance(Experiment experiment) {
        ExperimentPlots fragment = new ExperimentPlots();
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
        View root = inflater.inflate(R.layout.fragment_experiment_plots, container, false);

        barChart = root.findViewById(R.id.experiment_histogram);
        scatterChart = root.findViewById(R.id.experiment_time_plot);

        populateHistogram();
        populateTimePlot();

        return root;
    }

    /**
     * This method creates a Histogram based on experiment
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    private void populateHistogram (){
        if (experiment == null || barChart == null) {
            Log.e(TAG, "Could not create histogram");
            return;
        }

        // Get array of data from experiment
        List<BarEntry> entries = new ArrayList<BarEntry>();
        if (this.experiment.getType() == BinomialExperiment.type) { // Binomial format: {successCount, failureCount}
            // make list of data
            ArrayList<Integer> dataPoints = new ArrayList<Integer>();
            BinomialExperiment binomialExperiment = (BinomialExperiment) experiment;
            dataPoints.add(binomialExperiment.getSuccessCount());
            dataPoints.add(binomialExperiment.getFailureCount());

            // make list of entries
            for (int i = 0; i < dataPoints.size(); i++) {
                // turn your data into Entry objects
                entries.add(new BarEntry(i, dataPoints.get(i)));
            }
        }else if (this.experiment.getType() == CountExperiment.type){ // Count format: {Count}
            // make list of data
            ArrayList<Integer> dataPoints = new ArrayList<Integer>();
            dataPoints.add(this.experiment.getTrials().size());

            // make list of entries
            for (int i = 0; i < dataPoints.size(); i++) {
                // turn your data into Entry objects
                entries.add(new BarEntry(i, dataPoints.get(i)));
            }
        }
        else if (this.experiment.getType() == IntegerExperiment.type){ // Integer format: list of data points
            // make list of data
            IntegerExperiment integerExperiment = (IntegerExperiment) experiment;
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
                    uniqueDataPoints.put(dataPoints.get(i), uniqueDataPoints.get(dataPoints.get(i)) + 1);
                }else{
                    uniqueDataPoints.put(dataPoints.get(i), 1);
                }
            }

            // make list of entries
            for (int i : uniqueDataPoints.keySet()) {
                // turn your data into Entry objects
                entries.add(new BarEntry(i ,uniqueDataPoints.get(i)));
            }

        }else if (this.experiment.getType() == MeasurementExperiment.type) {
            // make list of data
            MeasurementExperiment measurementExperiment = (MeasurementExperiment) experiment;

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
                    uniqueDataPoints.put(dataPoints.get(i), uniqueDataPoints.get(dataPoints.get(i)) + 1);
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
        BarDataSet barDataSet = new BarDataSet(entries, String.format("%s Trials", experiment.getType()));
        BarData barData = new BarData(barDataSet);

        // add data to chart
        barChart.setData(barData);
        barData.setBarWidth(0.9f); // set custom bar width
        barChart.setFitBars(true);
        barChart.invalidate(); // Refreshes chart
    }// end populateHistogram

    /**
     * This method creates a TimePlot based on experiment
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    private void populateTimePlot () {
        if (experiment == null || barChart == null) {
            Log.e(TAG, "Could not create time plot");
            return;
        }

        // Get array of data from experiment
        if (this.experiment.getType() == BinomialExperiment.type) { // Binomial format: {successCount, failureCount}

            List<Entry> passEntries = new ArrayList<Entry>();
            List<Entry> failEntries = new ArrayList<Entry>();

            // make list of data
            BinomialExperiment binomialExperiment = (BinomialExperiment) this.experiment;

            ArrayList<Trial> temp = binomialExperiment.getTrials();
            ArrayList<BinomialTrial> binomialTrials = new ArrayList<BinomialTrial>();
            for (Trial trial : temp){
                binomialTrials.add((BinomialTrial) trial);
            }

            Hashtable<String, Integer> datePasses = new Hashtable<String, Integer>();
            Hashtable<String, Integer> dateFails = new Hashtable<String, Integer>();
            for (BinomialTrial trial : binomialTrials){
                int passes = trial.getPassCount();
                int fails = trial.getFailCount();
                String dateOfTrial = encodeDate(trial.getDateCreated().getYear(), trial.getDateCreated().getMonthValue(), trial.getDateCreated().getDayOfMonth());

                if (datePasses.containsKey(dateOfTrial)){
                    datePasses.put(dateOfTrial, datePasses.get(dateOfTrial) + passes);
                }else{
                    datePasses.put(dateOfTrial,passes);
                }
                if (dateFails.containsKey(dateOfTrial)){
                    dateFails.put(dateOfTrial, dateFails.get(dateOfTrial) + fails);
                }else{
                    dateFails.put(dateOfTrial,fails);
                }

            }

            // make list of entries
            for (String key : datePasses.keySet()) {
                // turn your data into Entry objects
                passEntries.add(new Entry(decodeDate(key), datePasses.get(key)));
            }
            for (String key : dateFails.keySet()) {
                // turn your data into Entry objects
                failEntries.add(new Entry(decodeDate(key), dateFails.get(key)));
            }

            ScatterDataSet passDataSet = new ScatterDataSet(passEntries, binomialExperiment.getPassUnit());
            ScatterDataSet failDataSet = new ScatterDataSet(failEntries, binomialExperiment.getFailUnit());

            passDataSet.setColors(new int[] {R.color.purple_700, R.color.teal_700});

            List<IScatterDataSet> dataSets = new ArrayList<IScatterDataSet>();

            dataSets.add(passDataSet);
            dataSets.add(failDataSet);

            // create data set
            ScatterData scatterData = new ScatterData(dataSets);

            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return formatDate(value);
                }
            };
            XAxis xAxis = scatterChart.getXAxis();
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);

            // add data to chart
            scatterChart.setData(scatterData);
            scatterChart.invalidate(); // Refreshes chart

        }else if (this.experiment.getType() == CountExperiment.type){ // Count format: {Count}
            List<Entry> entries = new ArrayList<Entry>();

            // make list of data
            CountExperiment countExperiment = (CountExperiment) this.experiment;

            ArrayList<Trial> temp = countExperiment.getTrials();
            ArrayList<CountTrial> countTrials = new ArrayList<CountTrial>();
            for (Trial trial : temp){
                countTrials.add((CountTrial) trial);
            }

            Hashtable<String, Integer> dateCounts = new Hashtable<String, Integer>();
            for (CountTrial trial : countTrials){
                String dateOfTrial = encodeDate(trial.getDateCreated().getYear(), trial.getDateCreated().getMonthValue(), trial.getDateCreated().getDayOfMonth());

                if (dateCounts.containsKey(dateOfTrial)){
                    dateCounts.put(dateOfTrial, dateCounts.get(dateOfTrial) + 1);
                }else{
                    dateCounts.put(dateOfTrial, 1);
                }
            }

            // make list of entries
            for (String key : dateCounts.keySet()) {
                // turn your data into Entry objects
                entries.add(new Entry(decodeDate(key), dateCounts.get(key)));
            }

            ScatterDataSet countDataSet = new ScatterDataSet(entries, countExperiment.getUnit());

            countDataSet.setColors(new int[] {R.color.purple_700});

            List<IScatterDataSet> dataSets = new ArrayList<IScatterDataSet>();

            dataSets.add(countDataSet);


            // create data set
            ScatterData scatterData = new ScatterData(dataSets);

            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return formatDate(value);
                }
            };
            XAxis xAxis = scatterChart.getXAxis();
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);

            // add data to chart
            scatterChart.setData(scatterData);
            scatterChart.invalidate(); // Refreshes chart
        } else if (this.experiment.getType() == IntegerExperiment.type){ // Integer format: list of data points
            List<Entry> entries = new ArrayList<Entry>();

            // make list of data
            IntegerExperiment integerExperiment = (IntegerExperiment) this.experiment;

            ArrayList<Trial> temp = integerExperiment.getTrials();
            ArrayList<IntegerTrial> integerTrials = new ArrayList<IntegerTrial>();
            for (Trial trial : temp){
                integerTrials.add((IntegerTrial) trial);
            }

            Hashtable<String, ArrayList<Integer>> dateValues = new Hashtable<String, ArrayList<Integer>>();
            for (IntegerTrial trial : integerTrials){
                String dateOfTrial = encodeDate(trial.getDateCreated().getYear(), trial.getDateCreated().getMonthValue(), trial.getDateCreated().getDayOfMonth());
                ArrayList<Integer> values;

                if (dateValues.containsKey(dateOfTrial)){
                    values = dateValues.get(dateOfTrial);
                }else{
                    values = new ArrayList<Integer>();
                }
                values.add(trial.getValue());
                dateValues.put(dateOfTrial, values);
            }

            // make list of entries
            for (String key : dateValues.keySet()) {
                // turn your data into Entry objects
                for (int value : dateValues.get(key)){
                    entries.add(new Entry(decodeDate(key), value));
                }

            }

            ScatterDataSet integerDataSet = new ScatterDataSet(entries, integerExperiment.getUnit());

            integerDataSet.setColors(new int[] {R.color.purple_700});

            List<IScatterDataSet> dataSets = new ArrayList<IScatterDataSet>();

            dataSets.add(integerDataSet);


            // create data set
            ScatterData scatterData = new ScatterData(dataSets);

            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return formatDate(value);
                }
            };
            XAxis xAxis = scatterChart.getXAxis();
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);

            // add data to chart
            scatterChart.setData(scatterData);
            scatterChart.invalidate(); // Refreshes chart

        }else if (this.experiment.getType() == MeasurementExperiment.type) {
            List<Entry> entries = new ArrayList<Entry>();

            // make list of data
            MeasurementExperiment measurementExperiment = (MeasurementExperiment) this.experiment;

            ArrayList<Trial> temp = measurementExperiment.getTrials();
            ArrayList<MeasurementTrial> measurementTrials = new ArrayList<MeasurementTrial>();
            for (Trial trial : temp){
                measurementTrials.add((MeasurementTrial) trial);
            }

            Hashtable<String, ArrayList<Float>> dateValues = new Hashtable<String, ArrayList<Float>>();
            for (MeasurementTrial trial : measurementTrials){
                String dateOfTrial = encodeDate(trial.getDateCreated().getYear(), trial.getDateCreated().getMonthValue(), trial.getDateCreated().getDayOfMonth());
                ArrayList<Float> values;

                if (dateValues.containsKey(dateOfTrial)){
                    values = dateValues.get(dateOfTrial);
                }else{
                    values = new ArrayList<Float>();
                }
                values.add(trial.getMeasurementValue());
                dateValues.put(dateOfTrial, values);
            }

            // make list of entries
            for (String key : dateValues.keySet()) {
                // turn your data into Entry objects
                for (float value : dateValues.get(key)){
                    entries.add(new Entry(decodeDate(key), value));
                }

            }

            ScatterDataSet measurementDataSet = new ScatterDataSet(entries, measurementExperiment.getUnit());

            measurementDataSet.setColors(new int[] {R.color.purple_700});

            List<IScatterDataSet> dataSets = new ArrayList<IScatterDataSet>();

            dataSets.add(measurementDataSet);


            // create data set
            ScatterData scatterData = new ScatterData(dataSets);

            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return formatDate(value);
                }
            };
            XAxis xAxis = scatterChart.getXAxis();
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);

            // add data to chart
            scatterChart.setData(scatterData);
            scatterChart.invalidate(); // Refreshes chart
        }
    }// end populateTimePlot

    private String encodeDate (int year, int month, int day){
        String stringYear = String.valueOf(year);
        stringYear = stringYear.substring(2,4);
        String stringMonth;
        String stringDay;
        if (month < 10){
            stringMonth = "0"+String.valueOf(month);
        }else{
            stringMonth = String.valueOf(month);
        }
        if (day < 10){
            stringDay = "0"+String.valueOf(day);
        }else{
            stringDay = String.valueOf(day);
        }

        return stringYear + "/" + stringMonth + "/" + stringDay;

    }
    private float decodeDate (String date){
        return (float) Integer.parseInt(date.replaceAll("/",""));
    }
    private String formatDate (float date) {
        int dateInt = (int) date;
        String formattedDate = String.valueOf(dateInt);
        return "20" + formattedDate.substring(0,2) + "/" + formattedDate.substring(2,4) + "/" + formattedDate.substring(4,6);
    }
}