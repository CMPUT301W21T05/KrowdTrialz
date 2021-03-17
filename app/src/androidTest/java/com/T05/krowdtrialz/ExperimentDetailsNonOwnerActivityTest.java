package com.T05.krowdtrialz;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.CountTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.ui.ExperimentDetailsNonOwnerActivity;
import com.T05.krowdtrialz.ui.ExperimentDetailsOwnerActivity;
import com.T05.krowdtrialz.util.Database;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;

public class ExperimentDetailsNonOwnerActivityTest {
    private Solo solo;
    private Database db;

    @Rule
    public ActivityTestRule<ExperimentDetailsNonOwnerActivity> rule =
            new ActivityTestRule<>(ExperimentDetailsNonOwnerActivity.class, true, true);

    @Before
    public void setUp() {

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void addIgnoredUserCountExperiment(){
        //set up experiment
        User user = new User ("Joseph Joebo", "Joe@gmail.com", "joejoejoe123", "0727fc22-b075-4a42-8d81-1716bc0e9e12");
        CountExperiment experiment = new CountExperiment(user, "this is a good one", "Cars seen");

        //add trials
        User user1 = new User("Billybob", "Billybobbob123", "bobbobbob@gmail.com", "0785a394-a419-4acd-be56-e953f3de37c5");
        User user2 = new User("Jimbo", "jimbo123", "jimbo@gmail.com", "1abdab43-4be5-44bd-8375-4bdc571754de");

        CountTrial trial1 = new CountTrial(user1, new Location(LocationManager.GPS_PROVIDER));
        CountTrial trial2 = new CountTrial(user1, new Location(LocationManager.GPS_PROVIDER));
        CountTrial trial3 = new CountTrial(user2, new Location(LocationManager.GPS_PROVIDER));
        CountTrial trial4 = new CountTrial(user2, new Location(LocationManager.GPS_PROVIDER));

        experiment.addTrial(trial1);
        experiment.addTrial(trial2);
        experiment.addTrial(trial3);
        experiment.addTrial(trial4);

        //add ignored user
        experiment.ignoreUser(user1);

        db.addExperiment(experiment);
    }

    @Test
    public void addIgnoredUserIntegerExperiment(){
        //set up experiment
        User user = new User ("Joseph Joebo", "Joe@gmail.com", "joejoejoe123", "0727fc22-b075-4a42-8d81-1716bc0e9e12");
        IntegerExperiment experiment = new IntegerExperiment(user, "this is a good one", "Eggs dropped");

        //add trials
        User user1 = new User("Billybob", "Billybobbob123", "bobbobbob@gmail.com", "0785a394-a419-4acd-be56-e953f3de37c5");
        User user2 = new User("Jimbo", "jimbo123", "jimbo@gmail.com", "1abdab43-4be5-44bd-8375-4bdc571754de");

        IntegerTrial trial1 = new IntegerTrial(user1, new Location(LocationManager.GPS_PROVIDER));
        trial1.setValue(50);
        IntegerTrial trial2 = new IntegerTrial(user1, new Location(LocationManager.GPS_PROVIDER));
        trial2.setValue(100);
        IntegerTrial trial3 = new IntegerTrial(user2, new Location(LocationManager.GPS_PROVIDER));
        trial3.setValue(150);
        IntegerTrial trial4 = new IntegerTrial(user2, new Location(LocationManager.GPS_PROVIDER));
        trial4.setValue(200);

        experiment.addTrial(trial1);
        experiment.addTrial(trial2);
        experiment.addTrial(trial3);
        experiment.addTrial(trial4);

        //add ignored user
        experiment.ignoreUser(user1);

        db.addExperiment(experiment);
    }

    @Test
    public void addIgnoredUserBinomialExperiment(){
        //set up experiment
        User user = new User ("Joseph Joebo", "Joe@gmail.com", "joejoejoe123", "0727fc22-b075-4a42-8d81-1716bc0e9e12");
        BinomialExperiment experiment = new BinomialExperiment(user, "this is a good one", "Heads", "Tails");

        //add trials
        User user1 = new User("Billybob", "Billybobbob123", "bobbobbob@gmail.com", "0785a394-a419-4acd-be56-e953f3de37c5");
        User user2 = new User("Jimbo", "jimbo123", "jimbo@gmail.com", "1abdab43-4be5-44bd-8375-4bdc571754de");

        BinomialTrial trial1 = new BinomialTrial(user1, new Location(LocationManager.GPS_PROVIDER));
        trial1.setPassCount(50);
        trial1.setFailCount(40);
        BinomialTrial trial2 = new BinomialTrial(user1, new Location(LocationManager.GPS_PROVIDER));
        trial2.setPassCount(100);
        trial2.setFailCount(90);
        BinomialTrial trial3 = new BinomialTrial(user2, new Location(LocationManager.GPS_PROVIDER));
        trial3.setPassCount(150);
        trial3.setFailCount(140);
        BinomialTrial trial4 = new BinomialTrial(user2, new Location(LocationManager.GPS_PROVIDER));
        trial4.setPassCount(200);
        trial4.setFailCount(190);

        experiment.addTrial(trial1);
        experiment.addTrial(trial2);
        experiment.addTrial(trial3);
        experiment.addTrial(trial4);

        //add ignored user
        experiment.ignoreUser(user1);

        db.addExperiment(experiment);
    }

    @Test
    public void testGraphs() throws InterruptedException {
        solo.waitForText("View Contributors");
        solo.sleep(15000);
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
