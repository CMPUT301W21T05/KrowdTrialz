package com.T05.krowdtrialz;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.T05.krowdtrialz.ui.SplashActivity;
import com.T05.krowdtrialz.ui.experimentDetails.ExperimentDetailsActivity;
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
 * This class contains tests for user stories related to the owner screen
 * @author Ryan Shukla
 */
public class OwnerTests {
    private Solo solo;
    private String description = "Determine what rate of success a coin has";
    private String minTrials = "13";
    private String region = "Antarctica";
    private String passCriteria = "Heads";
    private String failCriteria = "Tails";
    private String searchTerm = "Determine what rate of success a coin";

    private String name = "Test Name";
    private String username = "Test Username";
    private String email = "testemail@test.com";

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

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    @Test
    /**
     * @alert Verification for US 04.01.01 and 04.02.01
     */
    public void testEditUserInfo(){
        // Clear user info
        setUserInfo("None", "None", "None");
        solo.sleep(500);
        setUserInfo(name, username, email);

        // Verify that new info is displayed
        assertTrue(solo.waitForText(name));
        assertTrue(solo.waitForText(username));
        assertTrue(solo.waitForText(email));
    }

    @Test
    /**
     * @alert Verification for US 04.03.01
     */
    public void testRetrieveProfile(){
        // Clear user info
        setUserInfo(name, username, email);

        publish();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        // Go to experiment details and click on username
        solo.clickOnText(description);
        solo.waitForActivity(ExperimentDetailsActivity.class);
        solo.clickOnView(solo.getView(R.id.owner_textView_experiment));

        // Wait to go to the owner fragment
        solo.waitForView(solo.getView(R.id.profile_name));

        // Verify that the correct user information is shown
        assertTrue(solo.waitForText(name));
        assertTrue(solo.waitForText(username));
        assertTrue(solo.waitForText(email));

        // Go back to experiment details
        solo.goBack();
        // Go back to search
        solo.goBack();
        //Go back to subscribed page
        solo.hideSoftKeyboard();
        solo.goBack();

        unpublish();
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
        solo.waitForView(R.id.geo_location_toggle);

        // Click on "Publish New Experiment" button
        solo.clickOnView(solo.getView(R.id.new_publish_button));

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
    }

    private void unpublish(){
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

        //Click on unpublish button
        solo.clickOnView(solo.getView(R.id.unpublish_experiment_button));
    }

    private void setUserInfo(String name, String username, String email) {
        // Click on owner tab
        solo.clickOnView(solo.getView(R.id.navigation_owner));
        solo.waitForView(solo.getView(R.id.profile_name));

        // Set name
        solo.clickOnView(solo.getView(R.id.profile_name));
        solo.waitForView(solo.getView(R.id.edit_user_text));
        solo.clickOnView(solo.getView(R.id.edit_user_text));
        solo.clearEditText((EditText) solo.getView(R.id.edit_user_text));
        solo.typeText(0, name);
        solo.clickOnText("Ok");

        // Set username
        solo.clickOnView(solo.getView(R.id.profile_username));
        solo.waitForView(solo.getView(R.id.edit_user_text));
        solo.clickOnView(solo.getView(R.id.edit_user_text));
        solo.clearEditText((EditText) solo.getView(R.id.edit_user_text));
        solo.typeText(0, username);
        solo.clickOnText("Ok");

        // Set email
        solo.clickOnView(solo.getView(R.id.profile_email));
        solo.waitForView(solo.getView(R.id.edit_user_text));
        solo.clickOnView(solo.getView(R.id.edit_user_text));
        solo.clearEditText((EditText) solo.getView(R.id.edit_user_text));
        solo.typeText(0, email);
        solo.clickOnText("Ok");
    }
}
