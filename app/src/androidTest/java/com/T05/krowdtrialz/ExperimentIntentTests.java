package com.T05.krowdtrialz;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.ui.SplashActivity;
import com.T05.krowdtrialz.ui.publish.PublishFragment;
import com.T05.krowdtrialz.util.Database;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExperimentIntentTests {
    private Solo solo;
    private Database db = null;
    private String description = "Determine what rate of success a coin has";
    private String minTrials = "13";
    private String region = "Antarctica";
    private String passCriteria = "Heads";
    private String failCriteria = "Tails";
    private String searchTerm = "Determine";

    @Rule
    public ActivityTestRule<MainActivity> ruleMain =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @ClassRule
    public static ActivityTestRule<SplashActivity> ruleSplash =
            new ActivityTestRule<>(SplashActivity.class, true, true);

    @BeforeClass
    public static void beforeClass() {
        Solo solo = new Solo(InstrumentationRegistry.getInstrumentation(), ruleSplash.getActivity());
        Activity activity = ruleSplash.getActivity();
        solo.waitForActivity(SplashActivity.class);
    }

    @Before
    public void before() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), ruleMain.getActivity());
        db = Database.getInstance();
    }

    @Test
    /**
     * @alert Verification for US 01.01.01
     */
    public void testPublishExperiment(){
        //Click on publish tab
        solo.waitForView(R.id.search_action_button);
        solo.clickOnView(solo.getView(R.id.navigation_publish));
        solo.waitForView(R.id.geo_location_toggle);

        //Click on binomial
        solo.clickOnView(solo.getView(R.id.binomial_experiment_radio));

        //Click on add a description editText
        EditText descriptionBox = (EditText) solo.getView(R.id.experiment_description_input);
        solo.clickOnView(descriptionBox);
        solo.sleep(1000);

        //Type in a description
        solo.typeText(descriptionBox, description);

        //Click on geo location required toggle
        solo.clickOnView(solo.getView(R.id.geo_location_toggle));

        //Click on min trials editText
        EditText minTrialsBox = (EditText) solo.getView(R.id.minimum_trials_input);
        solo.clickOnView(minTrialsBox);
        solo.sleep(500);

        //Type in minTrials
        solo.typeText(minTrialsBox, minTrials);

        //Click on experiment region
        EditText regionBox = (EditText) solo.getView(R.id.experiment_region_input);
        solo.clickOnView(regionBox);
        solo.sleep(500);

        //Type in a region
        solo.typeText(regionBox, region);

        //Click on pass criteria
        EditText passCriteriaBox = (EditText) solo.getView(R.id.binomial_pass_criteria_input);
        solo.clickOnView(passCriteriaBox);
        solo.sleep(500);

        //Type in a pass criteria
        solo.typeText(passCriteriaBox, passCriteria);

        //Click on fail criteria
        EditText failCriteriaBox = (EditText) solo.getView(R.id.binomial_fail_criteria_input);
        solo.clickOnView(failCriteriaBox);
        solo.sleep(500);

        //Type in a fail criteria
        solo.typeText(failCriteriaBox, failCriteria);

        //Click on publish
        solo.clickOnView(solo.getView(R.id.publish_experiment_button));

        //Click on search icon
        solo.clickOnView(solo.getView(R.id.search_action_button));

        //Click on search bar
        SearchView searchBar = (SearchView) solo.getView(R.id.search_experiment_query);
        solo.clickOnView(searchBar);
        solo.sleep(500);

        //Type in a word from the description
        solo.typeText(0, searchTerm);

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Return to subscribed experiments page
        solo.goBack();
        solo.waitForView(R.id.publish_experiment_button);
        solo.clickOnView(solo.getView(R.id.navigation_subscribed));
        solo.waitForView(R.id.subscribed_exp_listView);
    }

    @Test
    /**
     * @alert Verification for US 01.04.01
     */
    public void testSubscribe(){
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

    @Test
    /**
     * @alert Verification for US 01.02.01 and US 01.03.01
     */
    public void testEndAndUnpublish(){
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

        //Click on more tab
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnText("More");

        //Click on end button
        solo.clickOnView(solo.getView(R.id.end_experiment_button));

        //Go back to subscribed page to refresh
        solo.goBack();
        solo.waitForView(solo.getView(R.id.search_experiment_query));
        solo.goBack();
        solo.waitForView(solo.getView(R.id.subscribed_exp_listView));

        //Click on search icon
        solo.waitForView(R.id.search_action_button);
        solo.clickOnView(solo.getView(R.id.search_action_button));

        //Click on search bar
        searchBar = (SearchView) solo.getView(R.id.search_experiment_query);
        solo.clickOnView(searchBar);
        solo.sleep(500);

        //Type in a word from the description
        solo.typeText(0, searchTerm);

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Make sure the active checkbox is unchecked
        MaterialCheckBox activeBox = (MaterialCheckBox) solo.getView(R.id.status_checkBox_experiment);
        assertFalse(activeBox.isChecked());

        //Click on experiment
        solo.clickOnText(description);

        //Make sure the add trials button is not enabled
        solo.waitForView(R.id.subscribe_button_experiment);
        Button addTrialsButton = (Button) solo.getView(R.id.add_trials_experiment);
        assertFalse(addTrialsButton.isEnabled());

        //Return to subscribed screen
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
