package com.T05.krowdtrialz;

import android.util.Log;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.CountTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;

import org.junit.Test;

import java.time.LocalDateTime;
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

    public CountExperiment mockCountExperiment (){
        CountExperiment experiment = new CountExperiment(mockOwner(),"Test Count experiment", "uNiTs");
        return experiment;
    }

    public MeasurementExperiment mockMeasurementExperiment (){
        MeasurementExperiment experiment = new MeasurementExperiment(mockOwner(),"Test measurement experiment", "cm");
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

    public CountTrial mockCountTrial1(){
        CountTrial cTrial = new CountTrial(mockOwner(), 100.2,-187.99, "September 30 2020");
        return cTrial;
    }
    public CountTrial mockCountTrial2(){
        CountTrial cTrial = new CountTrial(mockExperimenter(), 72.2,-10.99);
        return cTrial;
    }

    public MeasurementTrial mockMeasurementTrial1(){
        MeasurementTrial mTrial = new MeasurementTrial(mockOwner(), -192.642186,68.426148);
        return mTrial;
    }

    // test Integer trials sum all trial results
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

    // tests Binomial trials, getSuccessCount, getFailureCount and getSuccessRate()
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

    // test binomial trial all success case for getSuccessCount, getFailureCount and getSuccessRate()
    @Test
    public void testBinomialTrialAllSuccess(){
        BinomialExperiment experiment = mockBinomialExperiment();
        BinomialTrial bTrial = mockBinomialTrial1();
        bTrial.setPassCount(999);
        bTrial.setFailCount(0);
        experiment.addTrial(bTrial);
        assertEquals(experiment.getSuccessCount(), 999);
        assertEquals(experiment.getFailureCount(), 0);

        assertEquals(experiment.getSuccessRate(), 1,0.01);
    }

    // test binomial trial all failure case for getSuccessCount, getFailureCount and getSuccessRate()
    @Test
    public void testBinomialTrialAllFailure(){
        BinomialExperiment experiment = mockBinomialExperiment();
        BinomialTrial bTrial = mockBinomialTrial1();
        bTrial.setPassCount(0);
        bTrial.setFailCount(472);
        experiment.addTrial(bTrial);
        assertEquals(experiment.getSuccessCount(), 0);
        assertEquals(experiment.getFailureCount(), 472);

        assertEquals(experiment.getSuccessRate(), 0,0.01);
    }

    // test binomial trial for no trial case for getSuccessCount, getFailureCount and getSuccessRate()
    @Test
    public void testBinomialTrialNothing(){
        BinomialExperiment experiment = mockBinomialExperiment();
        BinomialTrial bTrial = mockBinomialTrial1();
        bTrial.setPassCount(0);
        bTrial.setFailCount(0);
        experiment.addTrial(bTrial);
        assertEquals(experiment.getSuccessCount(), 0);
        assertEquals(experiment.getFailureCount(), 0);

        assertEquals(experiment.getSuccessRate(), Double.NaN,0.01);
    }

    // test trial date functions
    @Test
    public void testDateFunctions(){
        CountTrial cTrial1 = mockCountTrial1();
        CountTrial cTrial2 = mockCountTrial2();
        assertEquals(cTrial1.getDateCreated(), "September 30 2020");

        LocalDateTime dateTime = LocalDateTime.now();
        assertEquals(cTrial2.getDayCreated(),dateTime.getDayOfMonth());
        assertEquals(cTrial2.getMonthCreated(),dateTime.getMonthValue());
        assertEquals(cTrial2.getYearCreated(),dateTime.getYear());
    }

    //test longitude and latitude functions
    @Test
    public void testLocation(){
        MeasurementTrial mTrial1 = mockMeasurementTrial1();
        assertEquals(mTrial1.getLatitude(),68.426148,0.00001);
        assertEquals(mTrial1.getLongitude(), -192.642186,0.00001);

        mTrial1.setLatitude(152.99);
        mTrial1.setLongitude(-99.990);
        assertEquals(mTrial1.getLatitude(),152.99,0.00001);
        assertEquals(mTrial1.getLongitude(), -99.990,0.00001);
    }

    //test getting the experimenter
    @Test
    public void testGetExperimenter(){
        User user = mockExperimenter();
        MeasurementTrial mTrial1 = new MeasurementTrial(user, -100.2,68.4);
        assertEquals(mTrial1.getExperimenter(), user);
    }

    // test getters and setters for integerTrial
    @Test
    public void testIntegerTrial(){
        IntegerTrial iTrial1 = mockIntegerTrial1();
        iTrial1.setValue(-99);
        assertEquals(iTrial1.getValue(),-99);

        iTrial1.setValue(0);
        assertEquals(iTrial1.getValue(),0);
    }

    // test getters and setters for MeasurementTrial
    @Test
    public void testMeasurementTrial(){
        MeasurementTrial mTrial1 = mockMeasurementTrial1();
        mTrial1.setMeasurementValue(-99.01f);
        assertEquals(mTrial1.getMeasurementValue(),-99.01f, 0.00001);

        mTrial1.setMeasurementValue(-99.01f);
        assertEquals(mTrial1.getMeasurementValue(),-99.01f, 0.00001);
    }

}
