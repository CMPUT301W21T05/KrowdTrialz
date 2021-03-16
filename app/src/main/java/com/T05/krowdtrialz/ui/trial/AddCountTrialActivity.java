package com.T05.krowdtrialz.ui.trial;

import android.os.Bundle;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.trial.Trial;

public class AddCountTrialActivity extends TrialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_count_trial);

        // Change text from "Submit" to "Increment"
        submitButton.setText(R.string.trial_increment);
    }

    @Override
    protected Trial createTrial() {
        // TODO
        return null;
    }
}