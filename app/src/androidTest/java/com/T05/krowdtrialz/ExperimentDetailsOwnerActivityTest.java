package com.T05.krowdtrialz;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.T05.krowdtrialz.ui.ExperimentDetailsOwnerActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

public class ExperimentDetailsOwnerActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<ExperimentDetailsOwnerActivity> rule =
            new ActivityTestRule<>(ExperimentDetailsOwnerActivity.class, true, true);

    @Before
    public void setUp() {
        Solo.Config config = new Solo.Config();
        config.screenshotSavePath = "content://com.android.providers.media.documents/document/images_root";
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), config,rule.getActivity());
    }

    @Test
    public void testHistogram() {
        solo.waitForText("View Contributors");
        solo.takeScreenshot("HISTOGRAMSCREENSHOT");
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
