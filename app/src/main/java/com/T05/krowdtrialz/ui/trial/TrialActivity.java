package com.T05.krowdtrialz.ui.trial;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.util.Database;

/**
 * Base class for common functionality across trial activities.
 */
public abstract class TrialActivity extends AppCompatActivity {
    private final String TAG = "Trial Activity";

    private Button submitButton;
    private Database db;

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
                if (experiment == null) {
                    Log.e(TAG, "Could not add trial.");
                    return;
                }
                Trial trial = createTrial();
                db.addTrial(trial, experiment);
                finish();
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
