package com.T05.krowdtrialz.ui.publish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class PublishActivity extends AppCompatActivity {

    private PublishViewModel publishViewModel;
    private final String TAG = "PublishActivity";

    private Class experimentClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        publishViewModel = new ViewModelProvider(this).get(PublishViewModel.class);

        clearExperimentSettings();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.experiment_type_radio_group);
        Button publishExperimentButton = (Button) findViewById(R.id.publish_experiment_button);
        Button cancelPublishButton = (Button) findViewById(R.id.cancel_publish_button);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d(TAG, "Radio changed: " + ((Integer) checkedId).toString());
            switch (checkedId) {
                case R.id.count_experiment_radio:
                    Log.d(TAG, "Count Experiment Selected");
                    countExperimentSelected();
                    break;
                case R.id.integer_experiment_radio:
                    Log.d(TAG, "Integer Exeperiment Selected");
                    integerExperimentSelected();
                    break;
                case R.id.binomial_experiment_radio:
                    Log.d(TAG, "Binomial Experiment Selected");
                    binomialExperimentSelected();
                    break;
                case R.id.measurement_experiment_radio:
                    Log.d(TAG, "Measurement Experiment Selected");
                    measurementExperimentSelected();
                    break;
                default:
                    Log.e(TAG, "Unknown Selection");
                    clearExperimentSettings();
            }
        });


        publishExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Publish Experiment Selected");
                publishExperiment();
                finish();
            }
        });

        cancelPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * Gets user input data and publishes an experiment to the database.
     *
     * @author Ryan Shukla
     */
    public void publishExperiment() {
        //View view = getView();

        Database db = Database.getInstance();
        User owner = db.getDeviceUser();

        EditText descriptionEditText = findViewById(R.id.experiment_description_input);
        EditText regionEditText = findViewById(R.id.experiment_region_input);

        String region = regionEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        // Get fields that are specific to the type of experiment
        Experiment experiment;
        if (experimentClass == MeasurementExperiment.class) {
            EditText variableNameEditText = findViewById(R.id.experiment_variable_name_input);
            String unit = variableNameEditText.getText().toString();
            experiment = new MeasurementExperiment(owner, description, unit);
        } else if (experimentClass == CountExperiment.class) {
            EditText variableNameEditText = findViewById(R.id.experiment_variable_name_input);
            String unit = variableNameEditText.getText().toString();
            experiment = new CountExperiment(owner, description, unit);
        } else if (experimentClass == BinomialExperiment.class) {
            EditText passUnitEditText = findViewById(R.id.binomial_pass_criteria_input);
            String passUnit = passUnitEditText.getText().toString();
            EditText failUnitEditText = findViewById(R.id.binomial_fail_criteria_input);
            String failUnit = failUnitEditText.getText().toString();
            experiment = new BinomialExperiment(owner, description, passUnit, failUnit);
        } else if (experimentClass == IntegerExperiment.class) {
            EditText variableNameEditText = findViewById(R.id.experiment_variable_name_input);
            String unit = variableNameEditText.getText().toString();
            experiment = new IntegerExperiment(owner, description, unit);
        } else if (experimentClass == null) {
            Log.e(TAG, "experimentClass null in publishExperiment");
            return;
        } else {
            Log.e(TAG, "experimentClass not recognized in publishExperiment. experimentClass: " + experimentClass.getCanonicalName());
            return;
        }

        SwitchMaterial locationRequiredSwitch  = findViewById(R.id.geo_location_toggle);
        boolean locationRequired = locationRequiredSwitch.isChecked();
        experiment.setLocationRequired(locationRequired);

        experiment.setRegion(region);
        EditText minTrialsEditText = findViewById(R.id.minimum_trials_input);
        String minTrialsString = minTrialsEditText.getText().toString();
        int minTrials = 0;
        if (minTrialsString.length() != 0) {
            try {
                minTrials = Integer.parseInt(minTrialsString);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Failed to parse minimum trials as integer. "  + e.getMessage());
            }
        }
        experiment.setMinTrials(minTrials);

        publishViewModel.sendExperimentToDatabase(experiment);
    }

    /**
     * Update the view to get binomial experiment settings
     *
     *  publish experiment view to operate on
     */
    private void binomialExperimentSelected() {
        experimentClass = BinomialExperiment.class;

        // adjust input box visibility
        findViewById(R.id.binomial_pass_criteria_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.binomial_pass_criteria_input).setVisibility(View.VISIBLE);
        findViewById(R.id.binomial_fail_criteria_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.binomial_fail_criteria_input).setVisibility(View.VISIBLE);
        findViewById(R.id.experiment_variable_name_layout).setVisibility(View.GONE);
        findViewById(R.id.experiment_variable_name_input).setVisibility(View.GONE);
        findViewById(R.id.publish_experiment_button).setVisibility(View.VISIBLE);
    }

    /**
     * Adjust view to get count experiment settings
     *
     *  publish experiment view to operate on
     */
    private void countExperimentSelected() {
        experimentClass = CountExperiment.class;

        // Adjust visibility and change hint text
        findViewById(R.id.binomial_pass_criteria_layout).setVisibility(View.GONE);
        findViewById(R.id.binomial_pass_criteria_input).setVisibility(View.GONE);
        findViewById(R.id.binomial_fail_criteria_layout).setVisibility(View.GONE);
        findViewById(R.id.binomial_fail_criteria_input).setVisibility(View.GONE);
        findViewById(R.id.publish_experiment_button).setVisibility(View.VISIBLE);
        com.google.android.material.textfield.TextInputLayout variableNameLayout = findViewById(R.id.experiment_variable_name_layout);
        variableNameLayout.setVisibility(View.VISIBLE);
        com.google.android.material.textfield.TextInputEditText variableNameInput = findViewById(R.id.experiment_variable_name_input);
        variableNameInput.setVisibility(View.VISIBLE);

        variableNameLayout.setHint("Name of variable to count");
    }

    /**
     * Adjust view to get integer experiment settings
     *
     *  publish experiment view to operate on
     */
    private void integerExperimentSelected() {
        experimentClass = IntegerExperiment.class;

        // Adjust visibility and change hint text
        findViewById(R.id.binomial_pass_criteria_layout).setVisibility(View.GONE);
        findViewById(R.id.binomial_pass_criteria_input).setVisibility(View.GONE);
        findViewById(R.id.binomial_fail_criteria_layout).setVisibility(View.GONE);
        findViewById(R.id.binomial_fail_criteria_input).setVisibility(View.GONE);
        findViewById(R.id.publish_experiment_button).setVisibility(View.VISIBLE);
        com.google.android.material.textfield.TextInputLayout variableNameLayout = findViewById(R.id.experiment_variable_name_layout);
        variableNameLayout.setVisibility(View.VISIBLE);
        com.google.android.material.textfield.TextInputEditText variableNameInput = findViewById(R.id.experiment_variable_name_input);
        variableNameInput.setVisibility(View.VISIBLE);

        variableNameLayout.setHint("Name of integer result");
    }

    /**
     * Adjust view to get measurement experiment settings
     *
     *  publish experiment view to operate on
     */
    private void measurementExperimentSelected() {
        experimentClass = MeasurementExperiment.class;

        // Adjust visibility and change hint text
        findViewById(R.id.binomial_pass_criteria_layout).setVisibility(View.GONE);
        findViewById(R.id.binomial_pass_criteria_input).setVisibility(View.GONE);
        findViewById(R.id.binomial_fail_criteria_layout).setVisibility(View.GONE);
        findViewById(R.id.binomial_fail_criteria_input).setVisibility(View.GONE);
        findViewById(R.id.publish_experiment_button).setVisibility(View.VISIBLE);
        com.google.android.material.textfield.TextInputLayout variableNameLayout = findViewById(R.id.experiment_variable_name_layout);
        variableNameLayout.setVisibility(View.VISIBLE);
        com.google.android.material.textfield.TextInputEditText variableNameInput = findViewById(R.id.experiment_variable_name_input);
        variableNameInput.setVisibility(View.VISIBLE);

        variableNameLayout.setHint("Name of measurement");
    }

    /**
     * Clear experiment-specific UI elements
     *
     *  publish experiment view to operate on
     */
    private void clearExperimentSettings() {
        experimentClass = null;

        findViewById(R.id.binomial_pass_criteria_layout).setVisibility(View.GONE);
        findViewById(R.id.binomial_pass_criteria_input).setVisibility(View.GONE);
        findViewById(R.id.binomial_fail_criteria_layout).setVisibility(View.GONE);
        findViewById(R.id.binomial_fail_criteria_input).setVisibility(View.GONE);
        findViewById(R.id.experiment_variable_name_layout).setVisibility(View.GONE);
        findViewById(R.id.experiment_variable_name_input).setVisibility(View.GONE);
        findViewById(R.id.publish_experiment_button).setVisibility(View.GONE);
    }
}