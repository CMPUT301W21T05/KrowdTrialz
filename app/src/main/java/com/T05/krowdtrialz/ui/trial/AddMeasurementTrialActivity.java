package com.T05.krowdtrialz.ui.trial;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.EditText;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;

public class AddMeasurementTrialActivity extends TrialActivity {

    private Database db;
    private User user;
    private Location location;
    private EditText valueEditText;
    private MeasurementTrial measurementTrial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurement_trial);

        valueEditText  = findViewById(R.id.measure_editText);
    }

    @Override
    protected Trial createTrial() {
        // get user, location and create a trial
        db = Database.getInstance();
        user = db.getDeviceUser();
        location = new Location(LocationManager.GPS_PROVIDER); // This is temporary until Geolocation is implemented
        measurementTrial = new MeasurementTrial(user, location);

        // set the pass and the fail counts
        float passText = Integer.parseInt(valueEditText.getText().toString());
        measurementTrial.setMeasurementValue(passText);

        return measurementTrial;
    } // end createTrial
} // end AddMeasurementTrialActivity