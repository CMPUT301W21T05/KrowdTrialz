package com.T05.krowdtrialz.ui.trial;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.util.Database;

/**
 * Base class for common functionality across trial activities.
 */
public abstract class TrialActivity extends AppCompatActivity {
    private final String TAG = "Trial Activity";

    private Button submitButton;
    private Button qrGenerateButton;
    private Database db;

    private EditText passText;
    private EditText failText;
    private EditText valueText;

    private Experiment experiment = null;

    /**
     * This is overidden so that this super class can get UI elements such as submitButton after the
     * activity's layout has been loaded.
     * @param layoutResID The layout for this activity.
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        Intent intent = getIntent();
        String experimentID = intent.getStringExtra(MainActivity.EXTRA_EXPERIMENT_ID);

        com.google.android.material.switchmaterial.SwitchMaterial locationRequired = findViewById(R.id.use_location_switch);

        db = Database.getInstance();

        db.getExperimentByID(experimentID, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment experiment) {
                TrialActivity.this.experiment  = experiment;
                locationRequired.setChecked(experiment.isLocationRequired());
                locationRequired.setEnabled(!experiment.isLocationRequired());
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "Could not get experiment");
            }
        });

        // This must be done after the layout is loaded
        submitButton = findViewById(R.id.submit_trial_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = experiment.getType();
                if(type == BinomialExperiment.type){
                    passText = findViewById(R.id.binomial1_editText);
                    failText = findViewById(R.id.binomial2_editText);
                }else if(type == MeasurementExperiment.type){
                    valueText = findViewById(R.id.measure_editText);
                } else if(type == IntegerExperiment.type){
                    valueText = findViewById(R.id.integer_editText);
                }


                if (type != "Count"){
                    if(valueText == null){
                        if (passText.getText().toString().equals("")|| failText.getText().toString().equals("")){
                            Log.d("test","returning");
                            return;
                        }
                    }else{
                        if (valueText.getText().toString().equals("")){
                            Log.d("test","returning1");
                            return;
                        }
                    }
                }
                if (experiment == null) {
                    Log.e(TAG, "Could not add trial.");
                    return;
                }
                Trial trial = createTrial();
                db.addTrial(trial, experiment);
                finish();
            }
        });

        qrGenerateButton = findViewById(R.id.generate_qr_button);

        qrGenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GenerateTrialQRActivity.class);
                String type = experiment.getType();
                // format will be ExperimentID/Type/Pass/Fail/Value/Longitude/Latitude
                String qrString = "";

                if(type == BinomialExperiment.type){
                    passText = findViewById(R.id.binomial1_editText);
                    failText = findViewById(R.id.binomial2_editText);
                    qrString = String.format("%s/Binomial/%s/%s/None", experimentID, passText.getText().toString(),failText.getText().toString());
                } else if(type == CountExperiment.type){
                    qrString = String.format("%s/Count/None/None/None", experimentID);
                }else if(type == MeasurementExperiment.type){
                    valueText = findViewById(R.id.measure_editText);
                    qrString = String.format("%s/Measurement/None/None/%s", experimentID, valueText.getText().toString());
                } else if(type == IntegerExperiment.type){
                    valueText = findViewById(R.id.integer_editText);
                    qrString = String.format("%s/Integer/None/None/%s", experimentID, valueText.getText().toString());
                }

                if (type != "Count"){
                    if(valueText == null){
                        if (passText.getText().toString().equals("")|| failText.getText().toString().equals("")){
                            Log.d("test","returning");
                            return;
                        }
                    }else{
                        if (valueText.getText().toString().equals("")){
                            Log.d("test","returning1");
                            return;
                        }
                    }
                }

                // TODO Actually get users location when submitting trial
                if (locationRequired.isChecked()){
                    qrString = qrString+String.format("/%s/%s", "90","90");
                }else{
                    qrString = qrString+"/None/None";
                }

                if(intent != null){
                    Log.d("test",qrString);
                    Log.d(TAG, "Starting Generate" + type + "Trial activity.");
                    intent.putExtra("Data",qrString);
                    startActivity(intent);
                } else{
                    Log.e(TAG,"Intent is null: Could not get Trial type.");
                }
            }
        });

    }

    /**
     * Fetch user inputs and construct a trial instance to send to the database.
     * This must be implemented in subclasses.
     * @return Trial instance to send to database.
     */
    protected abstract Trial createTrial();

    protected Button getSubmitButton() {
        return submitButton;
    }
}
