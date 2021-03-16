package com.T05.krowdtrialz;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ExperimentDetailsOwnerActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<ExperimentDetailsOwnerActivity> rule =
            new ActivityTestRule<>(ExperimentDetailsOwnerActivity.class, true, true);

    @Before
    public void setUp() {

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void testGraphs() throws InterruptedException {
        solo.waitForText("View Contributors");
        solo.wait(100000);
    }



    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
