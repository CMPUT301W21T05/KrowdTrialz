package com.T05.krowdtrialz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.ui.search.SearchActivity;
import com.T05.krowdtrialz.util.Database;

/**
 * This is the splash screen for the app.
 * This activity initializes the Database instance and will not start MainActivity until that
 * initialization has finished.
 *
 * @author Ryan Shukla
 */
public class SplashActivity extends AppCompatActivity {
    private final String TAG = "SPLASH ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize the Database instance.
        SharedPreferences sharedPreferences = getSharedPreferences("shared_P", MODE_PRIVATE);
        Database.initializeInstance(sharedPreferences,
                new Database.InitializeDatabaseCallback() {
                    @Override
                    public void onSuccess() {
                        startMainActivity();
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "Failed to initialize Database instance");
                    }
                });
    }

    private void startMainActivity() {
        Log.d(TAG, "Starting MainActivity");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}