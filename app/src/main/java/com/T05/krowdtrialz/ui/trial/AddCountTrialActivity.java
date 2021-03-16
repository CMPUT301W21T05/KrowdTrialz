package com.T05.krowdtrialz.ui.trial;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.trial.CountTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;

public class AddCountTrialActivity extends TrialActivity {

    private Database db;
    private User user;
    private Location location;
    private CountTrial countTrial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_count_trial);

        // Change text from "Submit" to "Increment"
        getSubmitButton().setText(R.string.trial_increment);
    }

    @Override
    protected Trial createTrial() {
        // get user, location and create a trial
        db = Database.getInstance();
        user = db.getDeviceUser();
        location = new Location(LocationManager.GPS_PROVIDER); // This is temporary until Geolocation is implemented
        countTrial = new CountTrial(user, location);

        return countTrial;
    } // end createTrial
}// end AddCountTrialActivity