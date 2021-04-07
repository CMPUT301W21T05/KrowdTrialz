package com.T05.krowdtrialz.ui.trial;

import android.content.Context;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.util.Database;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;
import java.util.Locale;

/**
 * Base class for common functionality across trial activities.
 */
public abstract class TrialActivity extends AppCompatActivity implements LocationListener {
    private final String TAG = "Trial Activity";

    private Button submitButton;
    private Button qrGenerateButton;
    private Button barcodeButton;
    private Database db;

    private EditText passText;
    private EditText failText;
    private EditText valueText;

    private Experiment experiment = null;
    private LocationManager locationManager;
    private ListenerRegistration expRegistration;

    private boolean generateQRClicked = false;
    private boolean saveBarcodeClicked = false;
    private boolean submitClicked = false;

    private String qrString;
    private Intent intent;

    /**
     * This is overidden so that this super class can get UI elements such as submitButton after the
     * activity's layout has been loaded.
     * @param layoutResID The layout for this activity.
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        intent = getIntent();
        String experimentID = intent.getStringExtra(MainActivity.EXTRA_EXPERIMENT_ID);

        com.google.android.material.switchmaterial.SwitchMaterial locationRequired = findViewById(R.id.use_location_switch);

        db = Database.getInstance();

        expRegistration = db.getExperimentByID(experimentID, new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment experiment) {
                TrialActivity.this.experiment  = experiment;
                locationRequired.setChecked(experiment.isLocationRequired());
                locationRequired.setEnabled(!experiment.isLocationRequired());

                if (locationRequired.isChecked()){
                    //Runtime permissions
                    if (ContextCompat.checkSelfPermission(TrialActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(TrialActivity.this,new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },100);
                    }
                }

            }

            @Override
            public void onFailure() {
                Log.e(TAG, "Could not get experiment");
            }
        });

        // This must be done after the layout is loaded
        submitButton = findViewById(R.id.submit_trial_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitClicked = true;

                String type = experiment.getType();
                if(type == BinomialExperiment.type){
                    passText = findViewById(R.id.binomial1_editText);
                    failText = findViewById(R.id.binomial2_editText);
                }else if(type == MeasurementExperiment.type){
                    valueText = findViewById(R.id.measure_editText);
                } else if(type == IntegerExperiment.type){
                    valueText = findViewById(R.id.integer_editText);
                }


                if (type != "Count"){
                    if(valueText == null){
                        if (passText.getText().toString().equals("")|| failText.getText().toString().equals("")){
                            return;
                        }
                    }else{
                        if (valueText.getText().toString().equals("")){
                            return;
                        }
                    }
                }
                if (experiment == null) {
                    Log.e(TAG, "Could not add trial.");
                    return;
                }

                if (locationRequired.isChecked()){
                    // get location
                    getLocation();

                }
                else{
                    Trial trial = createTrial();
                    db.addTrial(trial, experiment);
                  
                    // Toast to confirm adding trial
                    Context context = getApplicationContext();
                    CharSequence text = "Added Trial";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                  
                    finish();
                }
            }
        });

        qrGenerateButton = findViewById(R.id.generate_qr_button);

        qrGenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQRClicked = true;

                intent = new Intent(v.getContext(), GenerateTrialQRActivity.class);
                String type = experiment.getType();
                // format will be ExperimentID/Type/Pass/Fail/Value/Longitude/Latitude
                qrString = "";

                if(type == BinomialExperiment.type){
                    passText = findViewById(R.id.binomial1_editText);
                    failText = findViewById(R.id.binomial2_editText);
                    qrString = String.format("%s/Binomial/%s/%s/None", experimentID, passText.getText().toString(),failText.getText().toString());
                } else if(type == CountExperiment.type){
                    qrString = String.format("%s/Count/None/None/None", experimentID);
                }else if(type == MeasurementExperiment.type){
                    valueText = findViewById(R.id.measure_editText);
                    qrString = String.format("%s/Measurement/None/None/%s", experimentID, valueText.getText().toString());
                } else if(type == IntegerExperiment.type){
                    valueText = findViewById(R.id.integer_editText);
                    qrString = String.format("%s/Integer/None/None/%s", experimentID, valueText.getText().toString());
                }

                if (type != "Count"){
                    if(valueText == null){
                        if (passText.getText().toString().equals("")|| failText.getText().toString().equals("")){
                            return;
                        }
                    }else{
                        if (valueText.getText().toString().equals("")){
                            return;
                        }
                    }
                }

                if (locationRequired.isChecked()){
                    getLocation();
                }else{
                    qrString = qrString+"/None/None";
                    if(intent != null){
                        Log.d(TAG, "Starting Generate" + type + "Trial activity.");
                        intent.putExtra("Data",qrString);
                        startActivity(intent);
                    } else{
                        Log.e(TAG,"Intent is null: Could not get Trial type.");
                    }
                }
            }
        });

        barcodeButton = findViewById(R.id.assign_barcode_button);

        barcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBarcodeClicked = true;

                intent = new Intent(v.getContext(), SaveBarCodeActivity.class);
                String type = experiment.getType();
                // format will be ExperimentID/Type/Pass/Fail/Value/Longitude/Latitude
                qrString = "";

                if(type == BinomialExperiment.type){
                    passText = findViewById(R.id.binomial1_editText);
                    failText = findViewById(R.id.binomial2_editText);
                    qrString = String.format("%s/Binomial/%s/%s/None", experimentID, passText.getText().toString(),failText.getText().toString());
                } else if(type == CountExperiment.type){
                    qrString = String.format("%s/Count/None/None/None", experimentID);
                }else if(type == MeasurementExperiment.type){
                    valueText = findViewById(R.id.measure_editText);
                    qrString = String.format("%s/Measurement/None/None/%s", experimentID, valueText.getText().toString());
                } else if(type == IntegerExperiment.type){
                    valueText = findViewById(R.id.integer_editText);
                    qrString = String.format("%s/Integer/None/None/%s", experimentID, valueText.getText().toString());
                }

                if (type != "Count"){
                    if(valueText == null){
                        if (passText.getText().toString().equals("")|| failText.getText().toString().equals("")){
                            return;
                        }
                    }else{
                        if (valueText.getText().toString().equals("")){
                            return;
                        }
                    }
                }

                if (locationRequired.isChecked()){
                    getLocation();
                }else{
                    qrString = qrString+"/None/None";

                    if(intent != null){
                        Log.d(TAG, "Starting Generate" + type + "Trial activity.");
                        intent.putExtra("Data",qrString);
                        startActivity(intent);
                    } else{
                        Log.e(TAG,"Intent is null: Could not get Trial type.");
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop listening to changes in the Database.
        expRegistration.remove();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,TrialActivity.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Trial trial = createTrial();
            trial.setLatitude(location.getLatitude());
            trial.setLongitude(location.getLongitude());

            if (submitClicked) {
                db.addTrial(trial, experiment);

                // Toast to confirm adding trial
                Context context = getApplicationContext();
                CharSequence text = "Added Trial";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                submitClicked = false;
                finish();

            } else if (generateQRClicked){
                qrString = qrString+String.format("/%s/%s", location.getLongitude(), location.getLatitude());

                generateQRClicked = false;

                if(intent != null){
                    intent.putExtra("Data",qrString);
                    startActivity(intent);
                } else{
                    Log.e(TAG,"Intent is null");
                }
            } else if (saveBarcodeClicked){
                qrString = qrString+String.format("/%s/%s", location.getLongitude(), location.getLatitude());
                saveBarcodeClicked = false;

                if(intent != null){
                    intent.putExtra("Data",qrString);
                    startActivity(intent);
                } else{
                    Log.e(TAG,"Intent is null");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
