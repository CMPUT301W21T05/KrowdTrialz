package com.T05.krowdtrialz.ui.trial;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;

public class AddBinomialTrialActivity extends TrialActivity {
    private Database db;
    private User user;
    private int longitude;
    private int latitude;
    private EditText passEditText;
    private EditText failEditText;
    private BinomialTrial binomialTrial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_binomial_trial);

        passEditText  = findViewById(R.id.binomial1_editText);
        failEditText  = findViewById(R.id.binomial2_editText);
    }

    @Override
    protected Trial createTrial() {

        // get user, location and create a trial
        db = Database.getInstance();
        user = db.getDeviceUser();
        longitude = 90; // This is temporary until Geolocation is implemented
        latitude = 90; // This is temporary until Geolocation is implemented

        binomialTrial = new BinomialTrial(user, longitude, latitude);

        // set the pass and the fail counts
        int passText = Integer.parseInt(passEditText.getText().toString());
        int failText = Integer.parseInt(failEditText.getText().toString());
        binomialTrial.setPassCount(passText);
        binomialTrial.setFailCount(failText);

        return binomialTrial;
    } // end createTrial
}// end AddBinomialTrialActivity