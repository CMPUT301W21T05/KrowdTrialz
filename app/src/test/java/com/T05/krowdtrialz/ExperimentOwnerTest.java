package com.T05.krowdtrialz;

import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.user.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExperimentOwnerTest {

    public User mockUser () {
        User user = new User("Joe Bob","jbeast","jb@gmail.com","123");
        return user;
    }

    public MeasurementExperiment mockMeasurementExperiment (){
        MeasurementExperiment experiment = new MeasurementExperiment(mockUser(),"Test measurement experiment", "cm");
        return experiment;
    }

    @Test
    public void testIsOwner(){
        MeasurementExperiment experiment = mockMeasurementExperiment();

        User user = experiment.getOwner();
        User user2 = new User("John", "johna", "ja@gmail.com", "856c7d10-364d-40ea-ad2d-3aedd6993c5b");

        assertEquals(false, experiment.isOwner(user2));
        assertEquals(true, experiment.isOwner(user));

    }
}
