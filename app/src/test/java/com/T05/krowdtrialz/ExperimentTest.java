package com.T05.krowdtrialz;

import android.util.Log;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.CountTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test Experiment classes
 */
public class ExperimentTest {

    public User mockOwner () {
        User user = new User("Joe Bob","jbeast","jb@gmail.com","123");
        return user;
    }

    public User mockExperimenter (){
        User user = new User("Random Name","RN","rn@gmail.com","999");
        return user;
    }

    public MeasurementExperiment mockMeasurementExperiment (){
        MeasurementExperiment experiment = new MeasurementExperiment(mockOwner(),"Test measurement experiment", "cm");
        return experiment;
    }

    public MeasurementTrial mockMeasurementTrial1(){
        MeasurementTrial mTrial = new MeasurementTrial(mockOwner(), 0,0);
        return mTrial;
    }

    public IntegerExperiment mockIntegerExperiment (){
        IntegerExperiment experiment = new IntegerExperiment(mockOwner(),"Test Integer experiment", "m");
        return experiment;
    }

    public IntegerTrial mockIntegerTrial(){
        IntegerTrial iTrial = new IntegerTrial(mockOwner(), 0,0);
        return iTrial;
    }

    public IntegerTrial mockIntegerTrial1(){
        IntegerTrial iTrial = new IntegerTrial(mockExperimenter(), 100,187);
        return iTrial;
    }

    public CountExperiment mockCountExperiment (){
        CountExperiment experiment = new CountExperiment(mockOwner(),"Test Count experiment", "uNiTs");
        return experiment;
    }

    public CountTrial mockCountTrial1(){
        CountTrial cTrial = new CountTrial(mockOwner(), 100.2,-187.99, "September 30 2020");
        return cTrial;
    }
    public CountTrial mockCountTrial2(){
        CountTrial cTrial = new CountTrial(mockExperimenter(), 72.2,-10.99, "January 10 2020");
        return cTrial;
    }

    // test adding trial into statistics experiments and getting a specific trial
    @Test
    public void testAddTrial(){
        MeasurementExperiment experiment = mockMeasurementExperiment();
        MeasurementTrial mTrial = mockMeasurementTrial1();
        experiment.addTrial(mTrial);
        ArrayList<?> measurementTrial = experiment.getTrials();
        assertEquals(measurementTrial.get(0), mTrial);

        IntegerExperiment intExperiment = mockIntegerExperiment();
        IntegerTrial iTrial = mockIntegerTrial1();
        intExperiment.addTrial(iTrial);
        ArrayList<?> IntegerTrial = intExperiment.getTrials();
        assertEquals(IntegerTrial.get(0), iTrial);
    }

    // test methods for getting experiment type, unit and setting unit
    @Test
    public void testUnit(){
        CountExperiment experiment = mockCountExperiment();
        CountTrial cTrial = mockCountTrial1();
        experiment.addTrial(cTrial);

        assertEquals("Count", experiment.getType());
        assertEquals("uNiTs", experiment.getUnit());

        experiment.setUnit("Unit");
        assertEquals("Unit", experiment.getUnit());
    }

    // test method for getting count of a trial
    @Test
    public void testGetCount(){
        CountExperiment experiment = mockCountExperiment();
        CountTrial cTrial1 = mockCountTrial1();
        CountTrial cTrial2 = mockCountTrial2();
        experiment.addTrial(cTrial1);
        experiment.addTrial(cTrial2);

        assertEquals(2, experiment.getCount());

    }


    // test method to find if user is owner of experiment
    @Test
    public void testIsOwner(){
        User testExperimenter = mockExperimenter();
        MeasurementExperiment experiment = new MeasurementExperiment(testExperimenter,"Test measurement experiment", "cm");
        assertFalse(experiment.isOwner(mockOwner()));
        assertTrue(experiment.isOwner(testExperimenter));

    }

    // test ignoring a specific user
    @Test
    public void testIgnoreUser(){
        MeasurementExperiment experiment = mockMeasurementExperiment();
        User testExperimenter = mockExperimenter();
        assertFalse(experiment.isIgnored(testExperimenter));

        // testing ignoring 1 user
        experiment.ignoreUser(testExperimenter);
        assertTrue(experiment.isIgnored(testExperimenter));
    }

    // test ignoring multiple users given a list of users
    @Test
    public void testIgnoreMultipleUsers(){
        ArrayList<User> userAList = new ArrayList<>();
        User testExperimenter = mockExperimenter();
        User testOwner = mockOwner();
        MeasurementExperiment experiment = mockMeasurementExperiment();

        assertFalse(experiment.isIgnored(testExperimenter));
        assertFalse(experiment.isIgnored(testOwner));

        userAList.add(testExperimenter);
        userAList.add(testOwner);

        experiment.ignoreMultipleUsers(userAList);

        assertTrue(experiment.isIgnored(testExperimenter));
        assertTrue(experiment.isIgnored(testOwner));
    }

    // test unremoving an ignored user
    @Test
    public void testRemoveIgnoredUser(){
        MeasurementExperiment experiment = mockMeasurementExperiment();
        User testExperimenter = mockExperimenter();

        experiment.ignoreUser(testExperimenter);
        assertTrue(experiment.isIgnored(testExperimenter));

        experiment.removeIgnoredUser(testExperimenter);
        assertFalse(experiment.isIgnored(testExperimenter));
    }

    // test publishing state
    @Test
    public void testPublishing(){
        MeasurementExperiment experiment = mockMeasurementExperiment();
        assertTrue(experiment.isPublished());

        experiment.unpublish();
        assertFalse(experiment.isPublished());
    }

    // test setting and getting ID
    @Test
    public void testID(){
        IntegerExperiment intExperiment = mockIntegerExperiment();
        intExperiment.setId("abc123");
        assertEquals(intExperiment.getId(), "abc123");
    }

    // test setting and getting Description
    @Test
    public void testDescription(){
        IntegerExperiment intExperiment = mockIntegerExperiment();
        intExperiment.setDescription("test     Description");
        assertEquals(intExperiment.getDescription(), "test     Description");
    }

    // test setting and getting Region
    @Test
    public void testRegion(){
        IntegerExperiment intExperiment = mockIntegerExperiment();
        intExperiment.setRegion("test     Region");
        assertEquals(intExperiment.getRegion(), "test     Region");
    }

    // test location required or not
    @Test
    public void testLocationRequired(){
        IntegerExperiment intExperiment = mockIntegerExperiment();
        intExperiment.setLocationRequired(true);
        assertTrue(intExperiment.isLocationRequired());

        intExperiment.setLocationRequired(false);
        assertFalse(intExperiment.isLocationRequired());
    }

    // test getting and setting minimum number of trials
    @Test
    public void testMinTrials(){
        IntegerExperiment intExperiment = mockIntegerExperiment();
        intExperiment.setMinTrials(0);
        assertEquals(intExperiment.getMinTrials(), 0);

        intExperiment.setMinTrials(-99);
        assertEquals(intExperiment.getMinTrials(), -99);

        intExperiment.setMinTrials(256);
        assertEquals(intExperiment.getMinTrials(), 256);
    }

    // test getting contributors and their ID's
    @Test
    public void testContributors(){
        Set<User> users = new HashSet<>();
        User testExperimenter = mockExperimenter();
        User testOwner = mockOwner();
        users.add(testExperimenter);
        users.add(testOwner);

        IntegerExperiment experiment = mockIntegerExperiment();
        experiment.addTrial(mockIntegerTrial());
        experiment.addTrial(mockIntegerTrial1());

        Set<User> usersExp = new HashSet<>();
        usersExp = experiment.getContributors();
        assertEquals(users,usersExp);

        Set<String> userID = new HashSet<>();
        userID.add("123");
        userID.add("999");
        assertEquals(userID, experiment.getContributorsIDs());
    }

    // test get valid trials of non-ignored contributors
    @Test
    public void testGetValidTrials(){
        User testExperimenter = mockExperimenter();
        User testOwner = mockOwner();
        IntegerExperiment experiment = mockIntegerExperiment();

        assertFalse(experiment.isIgnored(testExperimenter));

        IntegerTrial iTrial1 = mockIntegerTrial1();
        IntegerTrial iTrial = mockIntegerTrial();
        experiment.addTrial(iTrial1);
        experiment.addTrial(iTrial);

        experiment.ignoreUser(testExperimenter);
        assertTrue(experiment.isIgnored(testExperimenter));

        ArrayList<?> IntegerTrial = experiment.getValidTrials();
        assertEquals(IntegerTrial.get(0), iTrial);
    }

    // test setActive, isActive, setInactive, isInactive
    @Test
    public void testActive(){

        IntegerExperiment intExperiment = mockIntegerExperiment();
        intExperiment.setActive();
        assertTrue(intExperiment.isActive());

        intExperiment.setInactive();
        assertTrue(intExperiment.isInactive());

    }

    // test experiments getting tags
    @Test
    public void testGetTags() {
        MeasurementExperiment experiment = mockMeasurementExperiment();

        experiment.setMinTrials(12);
        experiment.setRegion("NOrth America");

        ArrayList<String> tags = (ArrayList<String>) experiment.getTags();
        List<String> expectedTags = new ArrayList<String>();

        expectedTags.addAll(Arrays.asList("joe", "12", "jbeast", "measurement",
                "test", "experiment", "bob", "jb@gmail.com", "cm", "north", "america"));

        Collections.sort(expectedTags,String.CASE_INSENSITIVE_ORDER);
        Collections.sort(tags, String.CASE_INSENSITIVE_ORDER);

        assertEquals(expectedTags, tags);
    }
}