package com.T05.krowdtrialz;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.T05.krowdtrialz.ui.QnA.QuestionDetailsActivity;
import com.T05.krowdtrialz.ui.QnA.QuestionsActivity;
import com.T05.krowdtrialz.ui.SplashActivity;
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
 * This class contains tests for user stories related to questions and answers
 * @author Ryan Shukla
 */
public class QnATests {
    private Solo solo;
    private String description = "Determine what rate of success a coin has"; private String minTrials = "13";
    private String region = "Antarctica";
    private String passCriteria = "Heads";
    private String failCriteria = "Tails";
    private String searchTerm = "Determine what rate of success a coin";

    private String questionText = "What is the purpose of this experiment?";
    private String answerText = "To determine what rate of success a coin has.";

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
     * @alert Verification for US 02.01.01, US 02.02.01, and US 02.03.01
     */
    public void testAskAndReplyToQuestion(){
        publish();

        search();

        //Make sure the experiment shows up
        assertTrue(solo.waitForText(description));

        //Click on experiment
        solo.clickOnText(description);

        //Click on more tab
        solo.waitForView(R.id.subscribe_button_experiment);
        solo.clickOnText("More");

        //Click on Q&A button
        solo.clickOnView(solo.getView(R.id.view_qanda_button));

        solo.waitForActivity(QuestionsActivity.class);

        // Click on add question button
        solo.clickOnView(solo.getView(R.id.add_question_button));

        // Click on text box and enter question text
        solo.clickOnView(solo.getView(R.id.ask_question_editText));
        solo.typeText(0, questionText);
        solo.clickOnView(solo.getView(R.id.ask_question_submit_button));

        // Wait to be taken back to questions activity and check to see if the question that was
        // just added appears in the list.
        solo.waitForView(solo.getView(R.id.question_listView));
        assertTrue(solo.waitForText(questionText));

        // Add an answer
        solo.clickOnText(questionText);
        solo.waitForActivity(QuestionDetailsActivity.class);
        solo.clickOnView(solo.getView(R.id.submit_answer_editText));
        solo.typeText(0, answerText);
        solo.clickOnView(solo.getView(R.id.submit_answer_button));

        // Wait for answer text to appear
        solo.sleep(500); // Give time for the typed text to disappear
        assertTrue(solo.waitForText(answerText));

        // Go back to questions activity
        solo.hideSoftKeyboard();
        solo.goBack();
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
}
