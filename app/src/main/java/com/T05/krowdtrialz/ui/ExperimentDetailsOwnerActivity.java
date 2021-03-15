package com.T05.krowdtrialz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.T05.krowdtrialz.MainActivity;
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
import com.T05.krowdtrialz.util.Database;
import com.T05.krowdtrialz.ui.subscribed.SubscribedFragment;
import com.T05.krowdtrialz.ui.trial.AddBinomialTrialActivity;
import com.T05.krowdtrialz.ui.trial.AddCountTrialActivity;
import com.T05.krowdtrialz.ui.trial.AddIntegerTrialActivity;
import com.T05.krowdtrialz.ui.trial.AddMeasurementTrialActivity;
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

public class ExperimentDetailsOwnerActivity extends AppCompatActivity {

    private static final String TAG = "ExperimentDetailsO";
    private EditText description;
    private TextView region;
    private EditText minTrials;
    TextView mean;
    TextView stdev;
    TextView median;

    //for testing
    private String str;

    private Database db;
    private BarChart barChart;
    private ScatterChart scatterChart;
    private Experiment experiment;

    private Button addTrialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_details_owner);
        db = Database.getInstance();
        description = findViewById(R.id.description_owner_screen_editText);
        minTrials = findViewById(R.id.minimum_trial_owner_screen_exitText);

        Intent intent = getIntent();
        // TODO: Use this to get experiment object
        String experimentID = intent.getStringExtra(MainActivity.EXTRA_EXPERIMENT_ID);

        addTrialButton = findViewById(R.id.add_trials_owner_screen_button);

        addTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add Trials selected.");
                addTrial();
            }
        });

        db.getExperimentByID(experimentID, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment exp) {
                experiment = exp;
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "Error Searching Database for experiment");
            }
        });


        description.setText(experiment.getDescription());
        minTrials.setText(String.valueOf(experiment.getMinTrials()));

        /**
         * This method updates the Description when the owner edits the text
         * NOTE: user must click something else to update catch their changes
         * @author
         *  Ricky Au
         */
        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Log.d(TAG, "edited the description");
                    Toast.makeText(ExperimentDetailsOwnerActivity.this, "changed description to " + description.getText().toString(),Toast.LENGTH_SHORT).show();
                    //TODO: currently crashes if you retry to edit Description maybe just have confirm button that saves edit fields

                    //TODO: verify this works when we can pass experiment
                    experiment.setDescription(description.getText().toString());
                    db.updateExperiment(experiment);
                }
            }
        });

        // For entering minimum number of trials
        minTrials.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Log.d(TAG, "edited the min number of trials");
                    //TODO: currently crashes if you retry to edit Description maybe just have confirm button that saves edit fields
                    //TODO: verify this works when we can pass experiment
                    String numStr = minTrials.getText().toString();
                    int num = Integer.parseInt(numStr);
                    Toast.makeText(ExperimentDetailsOwnerActivity.this, "num trial is now " + num,Toast.LENGTH_SHORT).show();
                    experiment.setMinTrials(num);
                    db.updateExperiment(experiment);
                }
            }
        });

        populateRegionInfo();
        populateTrialResults();

    }// end onCreate


    /**
     * This method starts an add trial activity based on the experiment type.
     * @author Vasu Gupta
     */
    void addTrial() {
        Intent intent = null;
        String type = experiment.getType();

        if (type.equals("Binomial")) {
            intent = new Intent(this, AddBinomialTrialActivity.class);
        } else if (type.equals("Count")) {
            intent = new Intent(this, AddCountTrialActivity.class);
        } else if (type.equals("Measurement")) {
            intent = new Intent(this, AddMeasurementTrialActivity.class);
        } else if (type.equals("Integer")) {
            intent = new Intent(this, AddIntegerTrialActivity.class);
        }

        if (intent != null) {
            Log.d(TAG, "Starting Add" + type + "Trial activity.");
            intent.putExtra(MainActivity.EXTRA_EXPERIMENT_ID, experiment.getId());
            startActivity(intent);
        } else {
            Log.e(TAG, "Intent is null: Could not get Trial type.");
        }
    }

    /**
     * This method fills out Region, If none exist it will be blank
     * @author
     *  Ricky Au
     */
    private void populateRegionInfo() {

        // fill out region
        region = findViewById(R.id.region_detail_owner_screen_textView);
        region.setText(experiment.getRegion());

    }

    /**
     * This method ends the experiment when button pressed
     * @author
     *  Ricky Au
     */
    public void endExperiment(View view){
        Log.d(TAG, "end experiment");
        Toast.makeText(ExperimentDetailsOwnerActivity.this, "pressed endExperiment",Toast.LENGTH_SHORT).show();
        //TODO: need a database method for end experiment

    }

    /**
     * This method unpublishes the experiment when button is pressed
     * @author
     *  Ricky Au
     */
    public void unpublishExperiment(View view){
        Log.d(TAG, "unpublish experiment");
        Toast.makeText(ExperimentDetailsOwnerActivity.this, "pressed unpublishExperiment",Toast.LENGTH_SHORT).show();
        //TODO: need a database method for unpublishing experiment

    }

    /**
     * This method opens the Contributors page for the experiment when button is pressed
     * @author
     *  Ricky Au
     */
    public void viewContributors(View view){
        Log.d(TAG, "view Contributors");
        Toast.makeText(ExperimentDetailsOwnerActivity.this, "pressed viewContributors",Toast.LENGTH_SHORT).show();
        //TODO: connect to view contributors page

    }

    /**
     * This method subscribes the user to the experiment they are looking at
     * @author
     *  Ricky Au
     */
    public void subscribeToExperiment(View view){
        Log.d(TAG, "subscribe to experiment");
        Toast.makeText(ExperimentDetailsOwnerActivity.this, "pressed subscribe",Toast.LENGTH_SHORT).show();
        db.addSubscription(db.getDeviceUser(), experiment);
    }

    /**
     * This method subscribes the user to the experiment they are looking at
     * @author
     *  Ricky Au
     */
    public void addTrialToExperiment(View view){
        Log.d(TAG, "addTrial");
        Toast.makeText(ExperimentDetailsOwnerActivity.this, "pressed add Trial",Toast.LENGTH_SHORT).show();
        // TODO: pass a trial to add it to this experiment
//        switch activity to whatever type of trial this experiment is
    }

    /**
     * This method fills out Mean, Standard deviation, Mean and some trial results
     * @author
     *  Ricky Au
     */
    private void populateTrialResults() {
        // fill out Mean
        mean = findViewById(R.id.mean_detail_owner_screen_textView);
        // TODO: get the mean of experiment, Need method
//        mean.setText(__get_mean_of_experiment__);

        // TODO: get the standard deviation of experiment, Need method
        // fill out Standard deviation
        stdev = findViewById(R.id.st_dev_detail_owner_screen_textView);
//        stdev.setText(__get_stdev_of_experiment__);

        // TODO: get mean added to xml then, Need method
//        mean = findViewByID()
//        stdev.setText(__get_mean_of_experiment__);

        // TODO: maybe we make a button that pulls up a listview of all results
    }

    /**
     * This method allows user to view map of experiment
     * @author
     *  Ricky Au
     */
    public void viewMap(View view){
        Log.d(TAG, "view map");
        Toast.makeText(ExperimentDetailsOwnerActivity.this, "pressed view map",Toast.LENGTH_SHORT).show();
        // TODO: open map activity

    }

    /**
     * This method allows user to view map Questions and Answers for Experiment
     * @author
     *  Ricky Au
     */
    public void viewQnA(View view){
        Log.d(TAG, "view Q&A");
        Toast.makeText(ExperimentDetailsOwnerActivity.this, "pressed view Q&A",Toast.LENGTH_SHORT).show();
        // TODO: open Q&A activity

    }


    /**
     * This method creates a Histogram based on experiment
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    private void populateHistogram (){

        barChart = findViewById(R.id.histogram_owner);

        // Get experiment object
        Bundle extras = getIntent().getExtras();
        experiment = (Experiment) extras.get("experiment");

        // Get array of data from experiment
        List<BarEntry> entries = new ArrayList<BarEntry>();
        if (this.experiment.getType() == "Binomial") { // Binomial format: {successCount, failureCount}
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
        }else if (this.experiment.getType() == "Count"){ // Count format: {Count}
            // make list of data
            ArrayList<Integer> dataPoints = new ArrayList<Integer>();
            dataPoints.add(this.experiment.getTrials().size());

            // make list of entries
            for (int i = 0; i < dataPoints.size(); i++) {
                // turn your data into Entry objects
                entries.add(new BarEntry(i, dataPoints.get(i)));
            }
        }
        else if (this.experiment.getType() == "Integer"){ // Integer format: list of data points
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

        }else if (this.experiment.getType() == "Measurement") {
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
        scatterChart = findViewById(R.id.time_plot_owner);

        // Get experiment object
        Bundle extras = getIntent().getExtras();
        experiment = (Experiment) extras.get("experiment");

        // Get array of data from experiment
        if (this.experiment.getType() == "Binomial") { // Binomial format: {successCount, failureCount}

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

        }else if (this.experiment.getType() == "Count"){ // Count format: {Count}
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
        } else if (this.experiment.getType() == "Integer"){ // Integer format: list of data points
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

        }else if (this.experiment.getType() == "Measurement") {
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

    private String encodeDate (int year, int month,int day){
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
}// end ExperimentDetailsOwnerActivity

