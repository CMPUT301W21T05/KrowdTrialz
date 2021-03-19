package com.T05.krowdtrialz.ui.trial;

import android.os.Bundle;
import android.widget.EditText;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;

public class AddMeasurementTrialActivity extends TrialActivity {

    private Database db;
    private User user;
    private int longitude;
    private int latitude;
    private EditText valueEditText;
    private MeasurementTrial measurementTrial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurement_trial);

        valueEditText  = findViewById(R.id.measure_editText);
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

        measurementTrial = new MeasurementTrial(user, longitude, latitude);

        // set the pass and the fail counts
        float passText = Integer.parseInt(valueEditText.getText().toString());
        measurementTrial.setMeasurementValue(passText);

        return measurementTrial;
    } // end createTrial
} // end AddMeasurementTrialActivity