package com.T05.krowdtrialz;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.CountTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;
import com.google.firebase.firestore.ListenerRegistration;

import org.apache.commons.math3.analysis.function.Exp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private Database db;

    // Time to sleep (in ms) while waiting for firestore to update.
    private static final int WAIT_TIME_MS = 1000;

    private Experiment returnedExperiment;
    private User returnedUser;

    // List of experiments to be deleted from the Database after a test is run.
    private ArrayList<Experiment> experimentsToDelete;

    ListenerRegistration registration;

    private class MockSharedPreferences implements SharedPreferences {
        private class MockEditor implements SharedPreferences.Editor {
            @Override
            public Editor putString(String key, @Nullable String value) {
                return null;
            }

            @Override
            public Editor putStringSet(String key, @Nullable Set<String> values) {
                return null;
            }

            @Override
            public Editor putInt(String key, int value) {
                return null;
            }

            @Override
            public Editor putLong(String key, long value) {
                return null;
            }

            @Override
            public Editor putFloat(String key, float value) {
                return null;
            }

            @Override
            public Editor putBoolean(String key, boolean value) {
                return null;
            }

            @Override
            public Editor remove(String key) {
                return null;
            }

            @Override
            public Editor clear() {
                return null;
            }

            @Override
            public boolean commit() {
                return false;
            }

            @Override
            public void apply() {

            }
        }

        @Override
        public Map<String, ?> getAll() {
            return null;
        }

        @Nullable
        @Override
        public String getString(String key, @Nullable String defValue) {
            return null;
        }

        @Nullable
        @Override
        public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
            return null;
        }

        @Override
        public int getInt(String key, int defValue) {
            return 0;
        }

        @Override
        public long getLong(String key, long defValue) {
            return 0;
        }

        @Override
        public float getFloat(String key, float defValue) {
            return 0;
        }

        @Override
        public boolean getBoolean(String key, boolean defValue) {
            return false;
        }

        @Override
        public boolean contains(String key) {
            return false;
        }

        @Override
        public Editor edit() {
            return new MockEditor();
        }

        @Override
        public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

        }

        @Override
        public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

        }
    }

    /**
     * Mock callback that just counts the number of times onSuccess and onFailure were called.
     */
    private class MockGetExperimentCallback implements Database.GetExperimentCallback {
        public int onSuccessCount = 0;
        public int onFailureCount = 0;

        @Override
        public void onSuccess(Experiment experiment) {
            ++onSuccessCount;
        }

        @Override
        public void onFailure() {
            ++onFailureCount;
        }
    }

    public User mockUser () {
        User user = new User("Joe Bob","jbeast","jb@gmail.com", "856c7d10-364d-40ea-ad2d-3aedd6993c5b");
        return user;
    }

    public MeasurementExperiment mockMeasurementExperiment (){
        MeasurementExperiment experiment = new MeasurementExperiment(mockUser(),"Test measurement experiment", "cm");
        return experiment;
    }
    public BinomialExperiment mockBinomialExperiment (){
        BinomialExperiment experiment = new BinomialExperiment(mockUser(),"Test binomial experiment", "Heads","Tails");
        return experiment;
    }
    public CountExperiment mockCountExperiment (){
        CountExperiment experiment = new CountExperiment(mockUser(),"Test count experiment","Cars Seen");
        return experiment;
    }
    public IntegerExperiment mockIntegerExperiment (){
        IntegerExperiment experiment = new IntegerExperiment(mockUser(),"Test Integer experiment", "Eggs dropped");
        return experiment;
    }

    public CountTrial mockCountTrial() {
        return new CountTrial();
    }

    @Before
    public void setup() throws InterruptedException {
        final SharedPreferences sharedPreferences = new MockSharedPreferences();
        Database.initializeInstance(sharedPreferences, new Database.InitializeDatabaseCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure() {
            }
        } );
        Thread.sleep(WAIT_TIME_MS);
        db = Database.getInstance();
        returnedExperiment = null;
        returnedUser = null;
        experimentsToDelete = new ArrayList<>();
    }

    @After
    public void cleanup() {
        if (registration != null) {
            // Stop listening for changes to the database
            registration.remove();
        }
        for (Experiment experiment : experimentsToDelete) {
            db.deleteExperiment(experiment);
        }
    }

    @Test
    public void testSmokeTestGetExperimentsByOwner() throws InterruptedException {

        Experiment experiment = mockBinomialExperiment();

        db.addExperiment(experiment);
        experimentsToDelete.add(experiment);

        Thread.sleep(WAIT_TIME_MS);

        registration = db.getExperimentsByOwner(mockUser(), new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
                assertEquals(1, experiments.size());
            }

            @Override
            public void onFailure() {
                fail("Empty List Returned");
            }
        });

    }

    @Test
    public void testSmokeTestGetExperimentsByTags () {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("bob");
        db.getExperimentsByTags(tags, new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
                for(Experiment experiment : experiments){
                    Log.d("test Tags",experiment.getId());
                }

            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Test
    public void testGetUserById() throws InterruptedException {
        String id = db.getDeviceUser().getId();
        registration = db.getUserById(id, new Database.GetUserCallback() {
            @Override
            public void onSuccess(User user) {
                returnedUser = user;
            }

            @Override
            public void onFailure() {
                fail("onFailure called");
            }
        });

        Thread.sleep(WAIT_TIME_MS);

        assertNotNull(returnedUser);
        assertEquals(id, returnedUser.getId());
    }

    @Test
    public void testAddExperiment() throws InterruptedException {
        Experiment experiment = mockIntegerExperiment();
        experiment.setRegion("China");
        db.addExperiment(experiment);
        experimentsToDelete.add(experiment);
        Thread.sleep(WAIT_TIME_MS);
        assertNotNull(experiment.getId());
    }

    @Test
    public void testGetExperimentById() throws InterruptedException {
        Experiment experiment = mockIntegerExperiment();
        db.addExperiment(experiment);
        experimentsToDelete.add(experiment);
        Thread.sleep(WAIT_TIME_MS);

        registration = db.getExperimentByID(experiment.getId(),
                new Database.GetExperimentCallback() {
                    @Override
                    public void onSuccess(Experiment experiment) {
                        returnedExperiment = experiment;
                    }

                    @Override
                    public void onFailure() {
                        fail("onFailure called");
                    }
                });

        Thread.sleep(WAIT_TIME_MS);

        assertNotNull(returnedExperiment);
        assertEquals(experiment.getId(), returnedExperiment.getId());
    }

    /**
     * Tests addSubscription and getExperimentsBySubscriber.
     * @throws InterruptedException
     */
    @Test
    public void testSubscribe() throws InterruptedException {
        Experiment experiment = mockIntegerExperiment();
        db.addExperiment(experiment);
        experimentsToDelete.add(experiment);
        Thread.sleep(WAIT_TIME_MS);

        db.addSubscription(db.getDeviceUser(), experiment);
        Thread.sleep(WAIT_TIME_MS);

        ArrayList<Experiment> returnedExperiments = new ArrayList<>();
        registration = db.getExperimentsBySubscriber(db.getDeviceUser(),
                new Database.QueryExperimentsCallback() {
                    @Override
                    public void onSuccess(ArrayList<Experiment> experiments) {
                        experiments.forEach(
                                e -> returnedExperiments.add(e)
                        );
                    }

                    @Override
                    public void onFailure() {
                        fail("onFailure called");
                    }
                });
        Thread.sleep(WAIT_TIME_MS);

        assertTrue(returnedExperiments.contains(experiment));
    }

    /**
     * FAILING
     *  The assertTrue at the end of this method fails.
     * @throws InterruptedException
     */
    @Test
    public void testGetExperimentsByOwner() throws InterruptedException {
        Experiment experiment = mockIntegerExperiment();
        db.addExperiment(experiment);
        experimentsToDelete.add(experiment);
        Thread.sleep(WAIT_TIME_MS);

        ArrayList<Experiment> returnedExperiments = new ArrayList<>();
        registration = db.getExperimentsByOwner(db.getDeviceUser(),
                new Database.QueryExperimentsCallback() {
                    @Override
                    public void onSuccess(ArrayList<Experiment> experiments) {
                        experiments.forEach(
                                e -> returnedExperiments.add(e)
                        );
                    }

                    @Override
                    public void onFailure() {
                        fail("onFailure called");
                    }
                });
        Thread.sleep(WAIT_TIME_MS);

        assertTrue(returnedExperiments.contains(experiment));
    }

    @Test
    public void testGetExperimentsByTags() throws InterruptedException {
        BinomialExperiment experiment = mockBinomialExperiment();
        db.addExperiment(experiment);
        experimentsToDelete.add(experiment);
        Thread.sleep(WAIT_TIME_MS);

        ArrayList<String> tags = new ArrayList<>();
        tags.add(experiment.getPassUnit());
        tags.add(experiment.getFailUnit());

        ArrayList<Experiment> returnedExperiments = new ArrayList<>();
        db.getExperimentsByTags(tags,
                new Database.QueryExperimentsCallback() {
                    @Override
                    public void onSuccess(ArrayList<Experiment> experiments) {
                        experiments.forEach(
                                e -> returnedExperiments.add(e)
                        );
                    }

                    @Override
                    public void onFailure() {
                        fail("onFailure called");
                    }
                });
        Thread.sleep(WAIT_TIME_MS);

        assertTrue(returnedExperiments.contains(experiment));
    }

    @Test
    public void testAddTrial() throws InterruptedException {
        Experiment experiment = mockCountExperiment();
        db.addExperiment(experiment);
        experimentsToDelete.add(experiment);
        Thread.sleep(WAIT_TIME_MS);

        Trial trial = mockCountTrial();
        db.addTrial(trial, experiment);
        Thread.sleep(WAIT_TIME_MS);

        registration = db.getExperimentByID(experiment.getId(),
                new Database.GetExperimentCallback() {
                    @Override
                    public void onSuccess(Experiment experiment) {
                        returnedExperiment = experiment;
                    }

                    @Override
                    public void onFailure() {
                        fail("onFailure called");
                    }
                });
        Thread.sleep(WAIT_TIME_MS);

        assertTrue(returnedExperiment.getTrials().size() > 0);
    }

    @Test
    public void testUpdateExperiment() throws InterruptedException {
        MeasurementExperiment experiment = mockMeasurementExperiment();
        db.addExperiment(experiment);
        experimentsToDelete.add(experiment);
        Thread.sleep(WAIT_TIME_MS);

        experiment.setUnit("new unit");
        db.updateExperiment(experiment);
        Thread.sleep(WAIT_TIME_MS);

        registration = db.getExperimentByID(experiment.getId(),
                new Database.GetExperimentCallback() {
                    @Override
                    public void onSuccess(Experiment experiment) {
                        returnedExperiment = experiment;
                    }

                    @Override
                    public void onFailure() {
                        fail("onFailure called");
                    }
                });
        Thread.sleep(WAIT_TIME_MS);

        assertEquals("new unit", ((MeasurementExperiment) returnedExperiment).getUnit());
    }

    @Test
    public void testDeleteExperiment() throws InterruptedException {
        MeasurementExperiment experiment = mockMeasurementExperiment();
        db.addExperiment(experiment);
        Thread.sleep(WAIT_TIME_MS);

        db.deleteExperiment(experiment);
        Thread.sleep(WAIT_TIME_MS);

        registration = db.getExperimentByID(experiment.getId(),
                new Database.GetExperimentCallback() {
                    @Override
                    public void onSuccess(Experiment experiment) {
                        fail("onSuccess called but should have failed");
                    }

                    @Override
                    public void onFailure() {
                    }
                });

        Thread.sleep(WAIT_TIME_MS);

        assertNull(returnedExperiment);
    }

    /**
     * Tests to see if the callback is called each time an experiment is updated.
     */
    @Test
    public void testLiveUpdateExperiment() throws InterruptedException {
        MeasurementExperiment experiment = mockMeasurementExperiment();
        db.addExperiment(experiment);
        experimentsToDelete.add(experiment);
        Thread.sleep(WAIT_TIME_MS);

        MockGetExperimentCallback callback = new MockGetExperimentCallback();
        registration = db.getExperimentByID(experiment.getId(), callback);

        experiment.setUnit("new unit");
        db.updateExperiment(experiment);
        Thread.sleep(WAIT_TIME_MS);

        // Verify that the onSuccess callback was called twice.
        // (Once when the experiment was first queried and again when it was updated)
        assertEquals(2, callback.onSuccessCount);
    }
}
