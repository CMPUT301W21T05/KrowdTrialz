package com.T05.krowdtrialz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.T05.krowdtrialz.ui.search.SearchActivity;
import com.T05.krowdtrialz.util.Database;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;



public class MainActivity extends AppCompatActivity {

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_subscribed, R.id.navigation_publish, R.id.navigation_owner)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        
        final FloatingActionButton openSearchView = (FloatingActionButton) findViewById(R.id.search_action_button);
        openSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SUBSCRIBED FRAGMENT", "Clicked Search");
                Intent intent = new Intent(navView.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        // Initialize the Database instance.
        SharedPreferences sharedPreferences = getSharedPreferences("shared_P", MODE_PRIVATE);
        Database.initializeInstance(sharedPreferences);
        db = Database.getInstance();
        // Generate a new user with unique ID or fetch information for an existing user.
        db.initializeDeviceUser();
    }

}// end MainActivity