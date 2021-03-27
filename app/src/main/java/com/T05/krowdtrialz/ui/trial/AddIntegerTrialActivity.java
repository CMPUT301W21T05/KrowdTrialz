package com.T05.krowdtrialz.ui.trial;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class AddIntegerTrialActivity extends TrialActivity {

    private Database db;
    private User user;
    private EditText valueEditText;
    private IntegerTrial integerTrial;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_integer_trial);

        valueEditText = findViewById(R.id.integer_editText);
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
        integerTrial = new IntegerTrial(user);

        // set the pass and the fail counts
        int passText = Integer.parseInt(valueEditText.getText().toString());
        integerTrial.setValue(passText);

        return integerTrial;
    } // end createTrial




} // end AddIntegerTrialActivity