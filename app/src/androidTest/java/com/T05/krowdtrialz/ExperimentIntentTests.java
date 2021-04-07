package com.T05.krowdtrialz;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.T05.krowdtrialz.ui.SplashActivity;
import com.T05.krowdtrialz.ui.search.SearchActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains tests for user stories related to actions on experiments
 * @author Furmaan Sekhon and Jacques Leong-Sit
 */
public class ExperimentIntentTests {
    private Solo solo;
    private String experimentType = "binomial";
    private String description = "Determine what success a coin has";
    private String minTrials = "0";
    private String region = "Antarctica";
    private String passCriteria = "Heads";
    private String failCriteria = "Tails";
    private String valueCriteria = "Coin's Flipped";
    private String searchTerm = "Determine what success a coin";

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
        Activity activity = ruleMain.getActivity();
        solo.waitForActivity(MainActivity.class);
    }

    @Test
    /**
     * @alert Verification for US 01.01.01
     */
    public void testPublishBinomialExperiment(){
        experimentType = "binomial";
        publish();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Go back to subscribed page
        solo.hideSoftKeyboard();
        solo.goBack();

        delete();
    }

    @Test
    /**
     * @alert Verification for US 01.01.01
     */
    public void testPublishIntegerExperiment(){
        experimentType = "integer";
        publish();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Go back to subscribed page
        solo.hideSoftKeyboard();
        solo.goBack();

        delete();
    }

    @Test
    /**
     * @alert Verification for US 01.01.01
     */
    public void testPublishCountExperiment(){
        experimentType = "count";
        publish();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Go back to subscribed page
        solo.hideSoftKeyboard();
        solo.goBack();

        delete();
    }

    @Test
    /**
     * @alert Verification for US 01.01.01
     */
    public void testPublishMeasurementExperiment(){
        experimentType = "measurement";
        publish();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Go back to subscribed page
        solo.hideSoftKeyboard();
        solo.goBack();

        delete();
    }

    @Test
    /**
     * @alert Verification for US 01.04.01
     */
    public void testSubscribe(){
        experimentType = "Measurement";
        publish();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Click on experiment
        solo.clickOnText(description);

        //Click on subscribe
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnView(solo.getView(R.id.subscribe_button_experiment));

        //Go back to search screen
        solo.goBack();
        solo.hideSoftKeyboard();
        solo.waitForView(R.id.search_experiment_query);
        solo.goBack();
        solo.waitForView(R.id.navigation_publish);

        //Click on publish tab
        solo.clickOnView(solo.getView(R.id.navigation_publish));
        solo.waitForView(R.id.new_publish_button);

        //Click on subscribed tab
        solo.clickOnView(solo.getView(R.id.navigation_subscribed));
        solo.waitForView(R.id.subscribed_exp_listView);
        assertTrue(solo.waitForText(description));

        delete();
    }

    @Test
    /**
     * @alert Verification for US 01.02.01 and US 01.03.01
     */
    public void testEndAndUnpublish(){
        experimentType = "binomial";
        publish();

        search();

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
        solo.hideSoftKeyboard();
        solo.goBack();
        solo.waitForView(solo.getView(R.id.search_action_button));

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Make sure the active checkbox is unchecked
        assertFalse(solo.isCheckBoxChecked("Active"));

        //Click on experiment
        solo.clickOnText(description);

        //Make sure the add trials button is not enabled
        solo.waitForView(R.id.subscribe_button_experiment);
        Button addTrialsButton = (Button) solo.getView(R.id.add_trials_experiment);
        solo.sleep(3000);
        assertFalse(addTrialsButton.isEnabled());

        //Go back to subscribed page
        solo.goBack();
        solo.hideSoftKeyboard();
        solo.goBack();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Click on experiment
        solo.clickOnText(description);

        //Click on more tab
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnText("More");

        //Click on unpublish button
        solo.clickOnView(solo.getView(R.id.unpublish_experiment_button));

        //Go back to subscribed page
        solo.sleep(1000);
        solo.goBack();

        //Make sure the experiment doesn't show up
        assertFalse(solo.waitForText(description));

        solo.hideSoftKeyboard();
        solo.goBack();

        //Click on owner tab
        solo.waitForView(R.id.search_action_button);
        solo.clickOnView(solo.getView(R.id.navigation_owner));

        //Make sure the experiment shows up in owners page
        assertTrue(solo.waitForText(description));

        // click on experiment
        solo.waitForView(R.id.search_action_button);
        solo.clickOnText(description);
        //Click on more tab
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnText("More");
        //Click on delete button
        solo.clickOnView(solo.getView(R.id.delete_experiment_button));

    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    private void search(){
        //Click on search icon
        solo.waitForView(R.id.search_action_button);
        solo.clickOnView(solo.getView(R.id.search_action_button));

        //Click on search bar
        SearchView searchBar = (SearchView) solo.getView(R.id.search_experiment_query);
        solo.clickOnView(searchBar);
        solo.sleep(500);

        //Type in a word from the description
        solo.typeText(0, searchTerm);
    }

    private void publish(){
        //Click on publish tab
        solo.waitForView(R.id.search_action_button);
        solo.clickOnView(solo.getView(R.id.navigation_publish));

        // Click on "Publish New Experiment" button
        solo.clickOnView(solo.getView(R.id.new_publish_button));
        solo.waitForView(R.id.geo_location_toggle);

        //Click on Experiment type
        if (experimentType.toLowerCase().equals("measurement")) {
            solo.clickOnView(solo.getView(R.id.measurement_experiment_radio));
        }else if (experimentType.toLowerCase().equals("count")) {
            solo.clickOnView(solo.getView(R.id.count_experiment_radio));
        }else if (experimentType.toLowerCase().equals("integer")) {
            solo.clickOnView(solo.getView(R.id.integer_experiment_radio));
        }else{
            solo.clickOnView(solo.getView(R.id.binomial_experiment_radio));
        }

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

        if (experimentType.toLowerCase().equals("binomial")) {
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
        }else{
            // Click on value criteria
            EditText valueCriteriaBox = (EditText) solo.getView(R.id.experiment_variable_name_input);
            solo.clickOnView(valueCriteriaBox);
            solo.sleep(500);

            //Type in value criteria
            solo.typeText(valueCriteriaBox, valueCriteria);
        }


        //Click on publish
        solo.clickOnView(solo.getView(R.id.publish_experiment_button));
    }

    private void delete(){
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

        //Click on delete button
        solo.clickOnView(solo.getView(R.id.delete_experiment_button));
    }
}
