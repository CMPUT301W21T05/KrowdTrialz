package com.T05.krowdtrialz.ui.trial;

import android.os.Bundle;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.trial.Trial;

public class AddMeasurementTrialActivity extends TrialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurement_trial);
    }

    @Override
    protected Trial createTrial() {
        // TODO
        return null;
    }
}