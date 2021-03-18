package com.T05.krowdtrialz;

import android.util.Log;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
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
 * Test Trial classes
 */
public class TrialTest {
    public User mockOwner () {
        User user = new User("Joe Bob","jbeast","jb@gmail.com","123");
        return user;
    }

    public User mockExperimenter (){
        User user = new User("Random Name","RN","rn@gmail.com","999");
        return user;
    }

    public BinomialExperiment mockBinomialExperiment (){
        BinomialExperiment experiment = new BinomialExperiment(mockOwner(),"Test Binomial experiment", "head", "tail");
        return experiment;
    }

    public IntegerExperiment mockIntegerExperiment (){
        IntegerExperiment experiment = new IntegerExperiment(mockOwner(),"Test Integer experiment", "m");
        return experiment;
    }

    public BinomialTrial mockBinomialTrial1(){
        BinomialTrial bTrial = new BinomialTrial(mockOwner(), 788,256);
        return bTrial;
    }

    public IntegerTrial mockIntegerTrial1(){
        IntegerTrial iTrial = new IntegerTrial(mockOwner(), 0,0);
        return iTrial;
    }

    public IntegerTrial mockIntegerTrial2(){
        IntegerTrial iTrial = new IntegerTrial(mockOwner(), 100,187);
        return iTrial;
    }

    @Test
    public void testGetTotalIntTrial(){
        IntegerExperiment experiment = mockIntegerExperiment();
        IntegerTrial iTrial1 = mockIntegerTrial1();
        iTrial1.setValue(255);
        IntegerTrial iTrial2 = mockIntegerTrial2();
        iTrial2.setValue(2);
        experiment.addTrial(iTrial1);
        experiment.addTrial(iTrial2);

        assertEquals(experiment.getTotal(), 257);
    }

    @Test
    public void testBinomialTrial(){
        BinomialExperiment experiment = mockBinomialExperiment();
        BinomialTrial bTrial = mockBinomialTrial1();
        bTrial.setPassCount(100);
        bTrial.setFailCount(200);
        experiment.addTrial(bTrial);
        assertEquals(experiment.getSuccessCount(), 100);
        assertEquals(experiment.getFailureCount(), 200);

        assertEquals(experiment.getSuccessRate(), 0.33,0.01);

    }

}
