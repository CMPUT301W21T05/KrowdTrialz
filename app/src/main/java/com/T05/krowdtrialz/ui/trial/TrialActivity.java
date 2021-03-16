package com.T05.krowdtrialz.ui.trial;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.trial.Trial;

/**
 * Base class for common functionality across trial activities.
 */
public abstract class TrialActivity extends AppCompatActivity {
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        submitButton = findViewById(R.id.submit_trial_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trial trial = createTrial();
                // TODO call Database.addTrial
            }
        });
    }

    /**
     * Fetch user inputs and construct a trial instance to send to the database.
     * This must be implemented in subclasses.
     * @return Trial instance to send to database.
     */
    protected abstract Trial createTrial();
}
