package com.T05.krowdtrialz;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.T05.krowdtrialz.ui.SplashActivity;
import com.T05.krowdtrialz.util.Database;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class StatisticsUITest {
    private Solo solo;
    private Database db = null;
    private String search_tag = "Statistics IntentTest";
    private static final String TAG = "StatisticsUITest";


    @ClassRule
    public static ActivityTestRule<SplashActivity> classRule =
            new ActivityTestRule<>(SplashActivity.class, true, true);

    @Rule
    public ActivityTestRule<MainActivity> rule
            = new ActivityTestRule<>(MainActivity.class, true, true);


    @BeforeClass
    public static void beforeClass(){
        Solo solo = new Solo(InstrumentationRegistry.getInstrumentation(), classRule.getActivity());
        Activity activity = classRule.getActivity();
        solo.waitForActivity(SplashActivity.class);
        Log.d(TAG, "Setting up Intent test class.");
    }


    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        Activity activity = rule.getActivity();
        solo.waitForActivity(MainActivity.class);
        Log.d(TAG, "Instantiating Test...");
    }


    /**
     * This tests the plots tab in the Experiment Details screen.It makes sure
     * app reaches there (doesn't crash). Also, takes a screenshot for manual verification of results.
     *
     *
     * @alert Verification for US 01.06.01 and US 01.07.01
     */
    @Test
    public void testPlots(){
        publish();

        searchAndFindExperiment();

        assertTrue(solo.waitForText(search_tag));

        solo.clickOnText(search_tag);

        addTrials("100");
        solo.sleep(500);
        addTrials("101");
        solo.sleep(500);

        solo.goBack();
        solo.goBack();

        searchAndFindExperiment();

        assertTrue(solo.waitForText(search_tag));

        solo.clickOnText(search_tag);

        solo.waitForText("Plots");

        solo.clickOnText("Plots");
        assertTrue(solo.waitForView(R.id.experiment_time_plot));
        assertTrue(solo.waitForView(R.id.experiment_histogram));

        // Take a screen shot of the plots for manual verification.
        String path = Environment.getExternalStorageDirectory().getPath() + "/StatIntentTest";
        solo.getConfig().screenshotSavePath = path;
        solo.takeScreenshot("plot_test");

        solo.goBack();
        solo.goBack();

        delete();
    }


    /**
     * This tests the stats tab in the Experiment Details screen. It makes sure
     * app reaches there (doesn't crash).  Also, takes a screenshot for manual verification of results.
     *
     * @alert Verification for US 01.09.01
     */
    @Test
    public void testStats(){
        publish();

        searchAndFindExperiment();

        assertTrue(solo.waitForText(search_tag));

        solo.clickOnText(search_tag);

        addTrials("100");
        solo.sleep(500);
        addTrials("101");
        solo.sleep(500);

        solo.goBack();
        solo.goBack();

        searchAndFindExperiment();

        assertTrue(solo.waitForText(search_tag));

        solo.clickOnText(search_tag);

        solo.waitForText("Stats");

        solo.clickOnText("Stats");
        assertTrue(solo.waitForView(R.id.q1));
        assertTrue(solo.waitForView(R.id.q2));
        assertTrue(solo.waitForView(R.id.q3));
        assertTrue(solo.waitForView(R.id.q4));
        assertTrue(solo.waitForView(R.id.stddev));
        assertTrue(solo.waitForView(R.id.mean));
        assertTrue(solo.waitForView(R.id.median));

        // Take a screen shot of the plots for manual verification.
        String path = Environment.getExternalStorageDirectory().getPath() + "/StatIntentTest";
        solo.getConfig().screenshotSavePath = path;
        solo.takeScreenshot("stats_test");

        solo.goBack();
        solo.goBack();

        delete();
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


    /**
     * Search for the test experiment and click on it.
     */
    private void searchAndFindExperiment(){
        //Click on search icon
        solo.waitForView(R.id.search_action_button);
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.search_action_button));

        //Click on search bar
        SearchView searchBar = (SearchView) solo.getView(R.id.search_experiment_query);
        solo.clickOnView(searchBar);
        solo.sleep(500);

        //Type in a word from the description
        solo.typeText(0, "Statistics");
    }


    /**
     * Publishes a test experiment
     */
    private void publish(){
        //Click on publish tab
        solo.waitForView(R.id.search_action_button);
        solo.clickOnText("Publish");

        // Click on "Publish New Experiment" button
        solo.clickOnView(solo.getView(R.id.new_publish_button));
        solo.waitForView(R.id.geo_location_toggle);

        //Click on integer
        solo.clickOnView(solo.getView(R.id.integer_experiment_radio));
        solo.sleep(500);

        //Click on add a description editText
        EditText descriptionBox = (EditText) solo.getView(R.id.experiment_description_input);
        solo.clickOnView(descriptionBox);
        solo.sleep(1000);

        //Type in a description
        solo.typeText(descriptionBox, search_tag);

        //Click on min trials editText
        EditText minTrialsBox = (EditText) solo.getView(R.id.minimum_trials_input);
        solo.clickOnView(minTrialsBox);
        solo.sleep(500);

        //Type in minTrials
        solo.typeText(minTrialsBox, "3");

        //Click on experiment region
        EditText regionBox = (EditText) solo.getView(R.id.experiment_region_input);
        solo.clickOnView(regionBox);
        solo.sleep(500);

        //Type in a region
        solo.typeText(regionBox, "Kenya");

        //Click on unit
        EditText unitBox = (EditText) solo.getView(R.id.experiment_variable_name_input);
        solo.clickOnView(unitBox);
        solo.sleep(500);

        //Type in a unit
        solo.typeText(unitBox, "Eggs Dropped");

        //Click on publish
        solo.clickOnView(solo.getView(R.id.publish_experiment_button));
        solo.sleep(500);
    }


    /**
     * Un-publishes the test experiment created using publish()
     */
    private void delete(){
        //Click on search icon
        solo.waitForView(R.id.search_action_button);
        solo.clickOnView(solo.getView(R.id.search_action_button));

        //Click on search bar
        SearchView searchBar = (SearchView) solo.getView(R.id.search_experiment_query);
        solo.clickOnView(searchBar);
        solo.sleep(500);

        //Type in a word from the description
        solo.typeText(0, "Statistics");

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(search_tag));

        //Click on experiment
        solo.clickOnText(search_tag);

        //Click on more tab
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnText("More");

        //Click on unpublish button
        solo.clickOnView(solo.getView(R.id.delete_experiment_button));
    }


    /**
     * This adds a trial to the test experiment created using publish()
     *
     * @param value Value to add to trial
     */
    private void addTrials(String value){
        //Click add trials button
        solo.waitForView(solo.getView(R.id.add_trials_experiment));
        solo.sleep(1000);
        solo.clickOnText("ADD Trials");

        //Click integer value EditText
        solo.waitForView(solo.getView(R.id.submit_trial_button));
        EditText integerEditText = (EditText) solo.getView(R.id.integer_editText);
        solo.clickOnView(integerEditText);

        //Type a measurement value
        solo.typeText(integerEditText, value);

        //Click submit button
        solo.clickOnView(solo.getView(R.id.submit_trial_button));
    }
}



