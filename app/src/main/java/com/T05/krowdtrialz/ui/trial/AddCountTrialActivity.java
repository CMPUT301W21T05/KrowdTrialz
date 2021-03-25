package com.T05.krowdtrialz.ui.trial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.CountTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;

public class AddCountTrialActivity extends TrialActivity {

    private Database db;
    private User user;
    private int longitude;
    private int latitude;
    private CountTrial countTrial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_count_trial);

        // Change text from "Submit" to "Increment"
        getSubmitButton().setText(R.string.trial_increment);


    }

    /**
     * Construct a trial from the current application state
     *
     * @return trial
     */
    @Override
    protected Trial createTrial() {
        // get user, location and create a trial
        db = Database.getInstance();
        user = db.getDeviceUser();
        longitude = 90; // This is temporary until Geolocation is implemented
        latitude = 90; // This is temporary until Geolocation is implemented

        countTrial = new CountTrial(user, longitude, latitude);

        return countTrial;
    } // end createTrial
}// end AddCountTrialActivity