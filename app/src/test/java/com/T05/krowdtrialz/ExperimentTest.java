package com.T05.krowdtrialz;

import android.util.Log;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;

import org.junit.Test;

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

    public MeasurementTrial mockMeasurementTrial2(){
        MeasurementTrial mTrial = new MeasurementTrial(mockExperimenter(), 12,55);
        return mTrial;
    }


    @Test
    public void testAddTrial(){
        MeasurementExperiment experiment = mockMeasurementExperiment();
        MeasurementTrial mTrial = mockMeasurementTrial1();
        experiment.addTrial(mTrial);

        ArrayList<?> trial = experiment.getTrials();

        assertEquals(trial.get(0), mTrial);

    }

    @Test
    public void testIgnoreUser(){
        MeasurementExperiment experiment = mockMeasurementExperiment();
        User testExperimenter = mockExperimenter();
        assertFalse(experiment.isIgnored(testExperimenter));

        // testing ignoring 1 user
        experiment.ignoreUser(testExperimenter);
        assertTrue(experiment.isIgnored(testExperimenter));
    }

    @Test
    public void testRemoveIgnoredUser(){
        MeasurementExperiment experiment = mockMeasurementExperiment();
        User testExperimenter = mockExperimenter();

        experiment.ignoreUser(testExperimenter);
        assertTrue(experiment.isIgnored(testExperimenter));

        experiment.removeIgnoredUser(testExperimenter);
        assertFalse(experiment.isIgnored(testExperimenter));
    }

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