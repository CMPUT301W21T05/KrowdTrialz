package com.T05.krowdtrialz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Database db = new Database();
    private String localID;

    private String measureExperimentID = "123MES";
    private String binomialExperimentID = "123BIN";
    private String countExperimentID = "123COU";
    private String integerExperimentID = "123INT";
    private String uid = "JB123";

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
//        smokeTestGetExperimentsByOwner();
//        smokeTestGetExperimentsBySubscriber();
//        db.addExperiment(mockBinomialExperiment());

        smokeTestGetExperimentsBySearch();

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
        Log.d("saveID","Saved"+localID);

    }// end saveID

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

        if (localID == null) {
            Log.d("loadID","Created new id");
            Random rand = new Random();
            localID = String.valueOf(rand.nextInt(10000));
            db.verifyID(localID,new Database.GenerateIDCallback() {
                @Override
                public void onSuccess(String id) {
                    saveID();
                }

                @Override
                public void onFailure() {
                    loadID();
                }
            });
        }

    }// end loadID
    public User mockUser () {
        User user = new User("Joe Bob","jbeast","jb@gmail.com",uid);

        return user;
    }

    public MeasurementExperiment mockMeasurementExperiment (){
        MeasurementExperiment experiment = new MeasurementExperiment(mockUser(),"Test measurement experiment", "cm");
        return experiment;
    }
    public BinomialExperiment mockBinomialExperiment (){
        BinomialExperiment experiment = new BinomialExperiment(mockUser(),"Test binomial experiment", "Heads","Tails");
        return experiment;
    }
    public CountExperiment mockCountExperiment (){
        CountExperiment experiment = new CountExperiment(mockUser(),"Test count experiment","Cars Seen");
        return experiment;
    }
    public IntegerExperiment mockIntegerExperiment (){
        IntegerExperiment experiment = new IntegerExperiment(mockUser(),"Test Integer experiment", "Eggs dropped");
        return experiment;
    }


    void smokeTestGetExperimentsByOwner(){

        db.addExperiment(mockBinomialExperiment());

        db.getExperimentsByOwner(mockUser(), new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
//                Log.d("test",String.valueOf(experiments.size()));
                //Log.d("test",experiments.get(0).getDescription());
            }

            @Override
            public void onFailure() {
                Log.e("Failure","Failed");
            }
        });
    }

    void smokeTestGetExperimentsBySubscriber(){

        BinomialExperiment experiment = mockBinomialExperiment();

        experiment.setId("vpVzsJNKANMbN18gIQRB");

        db.addSubscription(mockUser(), experiment);

        db.getExperimentsBySubscriber(mockUser(), new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
                Log.d("test",String.valueOf(experiments.size()));
                //Log.d("test",experiments.get(0).getDescription());
            }

            @Override
            public void onFailure() {
                Log.e("Failure","Failed");
            }
        });
    }

    void smokeTestGetExperimentsBySearch(){

//        db.addExperiment(mockBinomialExperiment());

        db.getExperimentsBySearch("Test binomial experiment", new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
                Log.d("test",String.valueOf(experiments.size()));
//                Log.d("test",experiments.get(0).getDescription());
            }

            @Override
            public void onFailure() {
                Log.e("Failure","Failed");
            }
        });
    }


}// end MainActivity