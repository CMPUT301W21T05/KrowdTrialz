package com.T05.krowdtrialz;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.app.Activity;
import android.view.View;

import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.ui.SplashActivity;
import com.T05.krowdtrialz.ui.search.SearchActivity;
import com.T05.krowdtrialz.util.Database;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;


/**
 * Intent testing class for the search screen user stories
 */
@RunWith(AndroidJUnit4.class)
public class SearchIntentTest{

    private Solo solo;
    private Database db = null;
    private User user = new User("TESTNAME", "TESTUSERNAME", "TEST@.com", "1A2S#C");
    private Experiment testExperiment1 = new MeasurementExperiment(user, "TEST1 EXPERIMENT DESCRIPTION", "LENGTH"),
            testExperiment2 = new MeasurementExperiment(user, "TEST2 EXPERIMENT DESCRIPTION", "TIME"),
            testExperiment3 = new MeasurementExperiment(user, "TEST3 EXPERIMENT DESCRIPTION", "TIME");

    @ClassRule
    public static ActivityTestRule<SplashActivity> classRule =
            new ActivityTestRule<>(SplashActivity.class, true, true);

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Start the SpashScreen activity before the tests begin, so that the
     * database is properly instantiated.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void beforeClass() throws Exception{
        Solo solo = new Solo(InstrumentationRegistry.getInstrumentation(), classRule.getActivity());

        Activity activity = classRule.getActivity();

        solo.waitForActivity(SplashActivity.class);
    }

    /**
     * Get the solo instance and create the database
     *
     * @throws Exception
     */
    @Before
    public void before() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        db = Database.getInstance();
        addTextExperiment();
    }

    /**
     * Start the main activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Test that the search view starts when the search button is selected from the home
     * screen
     */
    @Test
    public void openSearchView(){

//        Activity activity = rule.getActivity();
        solo.waitForFragmentById(R.id.subscribed_experiment_fragment, 1000);

        View search = solo.getView(R.id.search_action_button);
        solo.clickOnView(search);

        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Cannot open Search activity", SearchActivity.class);

//        solo.goBack();
    }

    /**
     * Test that the search query correctly brings up the test experiment
     *
     * @alert Verification for US 05.02.01
     *
     * @throws InterruptedException
     */
    @Test
    public void testSearchQuery1result() throws InterruptedException {
        openSearchView();
        solo.enterText(0, "TESTUSERNAME testusername length test1");
        assertTrue(solo.waitForText("TESTUSERNAME", 1, 1000));
        assertTrue(solo.searchText("TEST1 EXPERIMENT DESCRIPTION"));
        assertTrue(solo.isCheckBoxChecked(0)); // its valid
//        solo.goBack();
    }

    /**
     * Test that the search query correctly brings up all available results
     *
     * @alert Verification for US 05.01.01
     *
     * @throws InterruptedException
     */
    @Test
    public void testSearchQueryMultipleResults() throws InterruptedException {
        openSearchView();
        solo.enterText(0, "TESTUSERNAME");
        assertTrue(solo.waitForText("TESTUSERNAME", 3, 1000));
        assertTrue(solo.searchText("TEST1 EXPERIMENT DESCRIPTION", 1));
        assertTrue(solo.searchText("TEST2 EXPERIMENT DESCRIPTION", 1));
        assertTrue(solo.searchText("TEST3 EXPERIMENT DESCRIPTION", 1));
//        solo.goBack();
    }

    /**
     * Test ability to search by common attribute, unit.
     *
     * @throws InterruptedException
     */
    @Test
    public void testSearchByUnit() throws InterruptedException {
        openSearchView();
        solo.enterText(0, "TIME");
        assertTrue(solo.waitForText("TESTUSERNAME", 2, 1000));
        assertTrue(solo.searchText("TEST2 EXPERIMENT DESCRIPTION", 1));
        assertTrue(solo.searchText("TEST3 EXPERIMENT DESCRIPTION", 1));
//        solo.goBack();
    }

    /**
     * Close activity after each test and delete the test experiment from
     * the database. Yes, this causes a lot of traffic for the database
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
        clearTestExperiment();
    }

    @AfterClass
    public static void afterClass() throws InterruptedException {
        Database db = Database.getInstance();
    }

    public void addTextExperiment() {
        db.addExperiment(testExperiment1);
        db.addExperiment(testExperiment2);
        db.addExperiment(testExperiment3);
    }

    public void clearTestExperiment() throws InterruptedException {
        db.deleteExperiment(testExperiment1);
        db.deleteExperiment(testExperiment2);
        db.deleteExperiment(testExperiment3);
    }

}