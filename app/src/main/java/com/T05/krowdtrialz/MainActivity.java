package com.T05.krowdtrialz;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.util.Database;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.user.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.apache.commons.math3.analysis.function.Exp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Database db = new Database();
    private String localID;

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













        /************** This is all rough tests for Database class ************/

//        String id = null;
//        id = db.generateID();

//        db.addExperiment(new MeasurementExperiment(new User("Test","Test1","test@gmail.com","com.google.android.gms.tasks.zzu@8525085"),
//                "Measurement experiment","cm"));
//        db.addExperiment(new BinomialExperiment(new User("Test","Test1","test@gmail.com","com.google.android.gms.tasks.zzu@8525085"),
//                "Binomial experiment","Heads","Tails"));

//        Log.d("output", id);

//        User user = new User("Test","Test1","test@gmail.com","com.google.android.gms.tasks.zzu@8525085");
//        Experiment experiment = new BinomialExperiment(new User("Test","Test1","test@gmail.com","com.google.android.gms.tasks.zzu@8525085"),"Binomial1 experiment","Heads","Tails");
//        experiment.setId("123ABC");
//        db.addTrial(new BinomialTrial(user , new Location(LocationManager.GPS_PROVIDER)), experiment);
//        db.addTrial(new BinomialTrial(user , new Location(LocationManager.GPS_PROVIDER)), experiment);

//        ArrayList<Experiment> list = db.getExperimentsByOwner(user);
//
//        for (int i = 0; i < list.size(); i++) {
//            Log.d("getExpriments", list.get(i).getId());
//        }

//        loadID();

//        MeasurementExperiment measurementExperiment = new MeasurementExperiment(new
//                User("Test","Test1","test@gmail.com","com.google.android.gms.tasks.zzu@8525085"), "Measurement experiment","cm");
//        measurementExperiment.setId("6QclPlos8jhBC1yTz0CV");
//
//        db.addSubscription(new User("Test","Test1","test@gmail.com","com.google.android.gms.tasks.zzu@8525085") , measurementExperiment);

//        MeasurementExperiment measurementExperiment = new MeasurementExperiment(new
//                User("Test2","Test2","test2@gmail.com","com.google.android.gms.tasks.zzu@8525085"), "Measurement experiment2","inch");
//        measurementExperiment.setId("6QclPlos8jhBC1yTz0CV");
//
//        db.updateExperiment(measurementExperiment);



    }

    /**
     * This may be part of a solution to generate unique id
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    public void saveID() {

        // allows variable to be saved
        SharedPreferences sharedP = getSharedPreferences("shared_P", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedP.edit();
        Gson gson = new Gson();
        String convertedID = gson.toJson(localID); // converts arrayList to json
        editor.putString("ID",convertedID); // saves converted arrayList
        editor.apply();

    }

    /**
     * This may be part of a solution to generate unique id
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    public void loadID() {

        SharedPreferences sharedP = getSharedPreferences("shared_P", MODE_PRIVATE);
        Gson gson = new Gson();
        String convertedID = sharedP.getString("ID", null);// gets saved arrayList (in json form) null if there is none saved
        Type type = new TypeToken<String>() {}.getType();
        localID = gson.fromJson(convertedID, type);

        Log.d("loadID", "Made it to loadID");

        localID = db.generateID();
        Log.d("generateID NEgative:",localID);
        if (localID == null) {
            localID = db.generateID();
            saveID();
        }

    }

}