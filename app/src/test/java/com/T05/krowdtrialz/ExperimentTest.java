package com.T05.krowdtrialz;

import android.util.Log;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
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
        IntegerTrial iTrial = new IntegerTrial(mockOwner(), 100,187);
        return iTrial;
    }

    // test adding trial into statistics experiments
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