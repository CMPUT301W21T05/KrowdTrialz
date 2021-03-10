package com.T05.krowdtrialz;

import android.util.Log;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.user.User;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExperimentTest {

    public User mockUser () {
        User user = new User("Joe Bob","jbeast","jb@gmail.com", "123");
        return user;
    }

    public MeasurementExperiment mockMeasurementExperiment (){
        MeasurementExperiment experiment = new MeasurementExperiment(mockUser(),"Test measurement experiment", "cm");
        return experiment;
    }

    @Test
    public void tags_areCorrect() {
        MeasurementExperiment experiment = mockMeasurementExperiment();
        
        experiment.setMinTrials(12);
        experiment.setRegion("Here");

        Set<String> tags = experiment.getTags();
        Set<String> expectedTags = new HashSet<>();

        expectedTags.addAll(Arrays.asList("here", "joe", "12", "jbeast",
                "test", "experiment", "bob", "jb@gmail.com", "cm", "measurement"));

        assertEquals(expectedTags, tags);
    }
}