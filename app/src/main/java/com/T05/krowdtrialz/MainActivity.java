package com.T05.krowdtrialz;

import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.Trial;
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
import java.util.UUID;

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

//        IntegerExperiment experiment = mockIntegerExperiment();
//        experiment.setRegion("North America");
//        db.addExperiment(experiment);

        ArrayList<String> tags = new ArrayList<>();
        tags.add("asdfjaskjdfhaskjdhfakjsdhfkjashfkajshdfkjashfkjsadhfjkshadfjkhsadkljf");
        tags.add("billy");
        tags.add("a");
        tags.add("b");
        tags.add("c");
        tags.add("asdfjaskjdfhaskjdhfakjsdhfkjashf1kajshdfkjashfkjsadhfjkshadfjkhsadkljf");
        tags.add("biasdfl345345ly");
        tags.add("boasdft435b");
        tags.add("asdfjaskjdfhaskjdhfakjsdhfkjashfk2ajshdfkjashfkjsadhfjkshadfjkhsadkljf");
        tags.add("bi3l2ly");
        tags.add("bo3b");
        tags.add("asdfjaskjdfhaskjdhfakjsdhfkjashfkaj3shdfkjashfkjsadhfjkshadfjkhsadkljf");
        tags.add("bi32lly");
        tags.add("bo2b");
        tags.add("asdfjaskjdfhaskjdhfakjsdhfkjashfkajiu5667345yhyey435shdfkjashfkjsadhfjkshadfjkhsadkljf");
        tags.add("bil123ly");
        tags.add("b543ob");
        tags.add("asdf7jaskjdfhaskjdhfakjsdhfkjashfkajshdfkjashfkjsadhfjkshadfjkhsadkljf");
        tags.add("bi987lly");
        tags.add("bob");
        db.getExperimentsByTags(tags, new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
                Log.d("test Tags","Made it to onSuccess");
                for(Experiment experiment : experiments){
                    Log.d("test Tags",experiment.getId());
                }

            }

            @Override
            public void onFailure() {
                Log.e("Failure","Failure Tags");
            }
        });

    }

    /**
     * This saves unique id after loadID generates a unique id
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
     * This checks if the device already has an id saved if not it generates unique id and saves it to device
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    public void loadID() {

        SharedPreferences sharedP = getSharedPreferences("shared_P", MODE_PRIVATE);
        Gson gson = new Gson();
        String convertedID = sharedP.getString("ID", null);// gets saved arrayList (in json form) null if there is none saved
        Type type = new TypeToken<String>() {}.getType();
        localID = gson.fromJson(convertedID, type);


        if (localID == null) {
            UUID uuid = UUID.randomUUID();
            localID = uuid.toString();
            db.verifyID(localID.toString(),new Database.GenerateIDCallback() {
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
        User user = new User("Billy James","jamesb","billyjames@gmail.com", "bb0ee07d-0669-45e7-8d25-f3ca3f280b24");
        return user;
    }

    public IntegerExperiment mockIntegerExperiment (){
        IntegerExperiment experiment = new IntegerExperiment(mockUser(),"Test Integer experiment", "Eggs dropped");
        return experiment;
    }

}// end MainActivity