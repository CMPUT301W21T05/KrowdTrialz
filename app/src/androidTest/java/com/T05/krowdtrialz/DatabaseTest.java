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
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private Database db;

    private String measureExperimentID = "123MES";
    private String binomialExperimentID = "123BIN";
    private String countExperimentID = "123COU";
    private String integerExperimentID = "123INT";
    private String uid = "JB123";

    private class MockSharedPreferences implements SharedPreferences {
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
            return null;
        }

        @Override
        public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

        }

        @Override
        public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

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

    @Before
    public void setup() {
        final SharedPreferences sharedPreferences = new MockSharedPreferences();
        Database.initializeInstance(sharedPreferences, new Database.InitializeDatabaseCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure() {
            }
        } );
        db = Database.getInstance();
    }

    @Test
    public void testSmokeTestGetExperimentsByOwner(){

        db.addExperiment(mockBinomialExperiment());

        db.getExperimentsByOwner(mockUser(), new Database.QueryExperimentsCallback() {
            @Override
            public void onSuccess(ArrayList<Experiment> experiments) {
                assertTrue(experiments.size() == 1);
            }

            @Override
            public void onFailure() {
                fail("Empty List Returned");
            }
        });
    }

    @Test
    public void testSetupAddNewExperiment(){
        IntegerExperiment experiment = mockIntegerExperiment();
        experiment.setRegion("China");
        //        db.addExperiment(experiment);
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
}
