package com.T05.krowdtrialz.ui.trial;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.trial.Trial;

/**
 * Base class for common functionality across trial activities.
 */
public abstract class TrialActivity extends AppCompatActivity {
    private Button submitButton;

    /**
     * This is overidden so that this super class can get UI elements such as submitButton after the
     * activity's layout has been loaded.
     * @param layoutResID The layout for this activity.
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // This must be done after the layout is loaded
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

    protected Button getSubmitButton() {
        return submitButton;
    }
}
