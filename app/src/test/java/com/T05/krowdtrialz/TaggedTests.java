package com.T05.krowdtrialz;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.interfaces.Tagged;
import com.T05.krowdtrialz.model.user.User;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the Tagged interface.
 * Since Tagged is implemented for each experiment subclass, there is a test for each subclass.
 *
 * @author Ryan Shukla
 */
public class TaggedTests {
    private static final String id = "1234";
    private static final String name = "Name";
    private static final String username = "Username";
    private static final String email = "testemail@test.com";
    private static final User owner = new User(name, username, email, id);

    private static final String description = "This is the description.";
    private static final String[] descriptionTags = {"This", "is", "the", "description"};
    private static final String region = "Mars";

    private static final String passUnit = "Heads";
    private static final String failUnit = "Tails";
    private static BinomialExperiment mockBinomialExperiment() {
        BinomialExperiment experiment = new BinomialExperiment(owner, description, passUnit, failUnit);
        experiment.setRegion(region);
        return experiment;
    }

    private static final String unit = "Cars";
    private static CountExperiment mockCountExperiment() {
        CountExperiment experiment = new CountExperiment(owner, description, unit);
        experiment.setRegion(region);
        return experiment;
    }

    private static IntegerExperiment mockIntegerExperiment() {
        IntegerExperiment experiment = new IntegerExperiment(owner, description, unit);
        experiment.setRegion(region);
        return experiment;
    }

    private static MeasurementExperiment mockMeasurementExperiment() {
        MeasurementExperiment experiment = new MeasurementExperiment(owner, description, unit);
        experiment.setRegion(region);
        return experiment;
    }

    private void assertContainsTag(String expectedTag, List<String> tags) {
        assertTrue(tags.contains(expectedTag.toLowerCase()));
    }

    /**
     * Asserts that the list of tags contains fields of the base Experiment class.
     * @param tags The list of tags returned by Tagged.getTags()
     */
    private void assertContainsBaseExperimentInfo(List<String> tags) {
        assertContainsTag(name, tags);
        assertContainsTag(username, tags);
        assertContainsTag(email, tags);
        for (String tag : descriptionTags) {
            assertContainsTag(tag, tags);
        }
        assertContainsTag(region, tags);
    }

    @Test
    public void testBinomial() {
        Tagged tagged = mockBinomialExperiment();
        List<String> tags = tagged.getTags();

        assertContainsBaseExperimentInfo(tags);
        assertContainsTag(passUnit, tags);
        assertContainsTag(failUnit, tags);
    }

    @Test
    public void testCount() {
        Tagged tagged = mockCountExperiment();
        List<String> tags = tagged.getTags();

        assertContainsBaseExperimentInfo(tags);
        assertContainsTag(unit, tags);
    }

    @Test
    public void testInteger() {
        Tagged tagged = mockIntegerExperiment();
        List<String> tags = tagged.getTags();

        assertContainsBaseExperimentInfo(tags);
        assertContainsTag(unit, tags);
    }

    @Test
    public void testMeasurement() {
        Tagged tagged = mockMeasurementExperiment();
        List<String> tags = tagged.getTags();

        assertContainsBaseExperimentInfo(tags);
        assertContainsTag(unit, tags);
    }
}
