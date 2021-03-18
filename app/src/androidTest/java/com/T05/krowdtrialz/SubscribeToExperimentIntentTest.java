package com.T05.krowdtrialz;

import android.util.Log;
import android.widget.SearchView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.T05.krowdtrialz.ui.SplashActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SubscribeToExperimentIntentTest {
    private Solo solo;
    private String description = "Determine what rate of success a coin has";
    private String minTrials = "13";
    private String region = "Antarctica";
    private String passCriteria = "Heads";
    private String failCriteria = "Tails";
    private String searchTerm = "Determine";

    @Rule
    public ActivityTestRule<SplashActivity> rule =
            new ActivityTestRule<>(SplashActivity.class, true, true);

    @Before
    public void setUp() {
        Log.d("intenttesting", "made it to before");
        solo.sleep(10000);
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    /**
     * @alert Verification for US 01.04.01
     */
    public void testSubscribe(){
        Log.d("intenttesting", "made it to testSubscribe");
        //Click on search icon
        solo.waitForView(R.id.search_action_button);
        solo.clickOnView(solo.getView(R.id.search_action_button));

        //Click on search bar
        SearchView searchBar = (SearchView) solo.getView(R.id.search_experiment_query);
        solo.clickOnView(searchBar);
        solo.sleep(500);

        //Type in a word from the description
        solo.typeText(0, searchTerm);

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Click on experiment
        solo.clickOnText(description);

        //Click on subscribe
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnView(solo.getView(R.id.subscribe_button_experiment));

        //Go back to search screen
        solo.goBack();
        solo.waitForView(R.id.search_experiment_query);
        solo.goBack();
        solo.waitForView(R.id.navigation_publish);

        //Click on publish tab
        solo.clickOnView(solo.getView(R.id.navigation_publish));
        solo.waitForView(R.id.publish_experiment_button);

        //Click on subscribed tab
        solo.clickOnView(solo.getView(R.id.navigation_subscribed));
        solo.waitForView(R.id.subscribed_exp_listView);
        assertTrue(solo.waitForText(description));
    }

    @After
    public void tearDown() {
        Log.d("intenttesting", "made it to after");
        solo.finishOpenedActivities();
    }
}
