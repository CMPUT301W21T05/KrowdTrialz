package com.T05.krowdtrialz.ui.trial;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.EditText;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;

public class AddIntegerTrialActivity extends TrialActivity {

    private Database db;
    private User user;
    private Location location;
    private EditText valueEditText;
    private IntegerTrial integerTrial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_integer_trial);

        valueEditText  = findViewById(R.id.integer_editText);
    }

    @Override
    protected Trial createTrial() {
        // get user, location and create a trial
        db = Database.getInstance();
        user = db.getDeviceUser();
        location = new Location(LocationManager.GPS_PROVIDER); // This is temporary until Geolocation is implemented
        integerTrial = new IntegerTrial(user, location);

        // set the pass and the fail counts
        int passText = Integer.parseInt(valueEditText.getText().toString());
        integerTrial.setValue(passText);

        return integerTrial;
    } // end createTrial
} // end AddIntegerTrialActivity