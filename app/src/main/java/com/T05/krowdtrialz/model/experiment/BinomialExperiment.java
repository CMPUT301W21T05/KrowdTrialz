package com.T05.krowdtrialz.model.experiment;

import android.util.Log;

import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BinomialExperiment extends Experiment {
    public static final String type = "Binomial";

    private ArrayList<BinomialTrial> trials;
    // The name of a fail event. (e.g. "heads")
    private String passUnit;
    // The name of a fail event. (e.g. "tails")
    private String failUnit;

    public BinomialExperiment() {
        trials = new ArrayList<>();
    }

    public BinomialExperiment(User owner, String description, String passUnit, String failUnit) {
        super(owner, description);
        trials = new ArrayList<>();
        this.passUnit = passUnit;
        this.failUnit = failUnit;
    }

    @Override
    public ArrayList<? extends Trial> getTrials() {
        return trials;
    }

    @Override
    public <E extends Trial> void addTrial(E trial) {
        trials.add((BinomialTrial) trial);
    }

    @Override
    public String getType() {
        return type;
    }

    public String getPassUnit() {
        return passUnit;
    }

    public void setPassUnit(String passUnit) {
        this.passUnit = passUnit;
    }

    public String getFailUnit() {
        return failUnit;
    }

    public void setFailUnit(String failUnit) {
        this.failUnit = failUnit;
    }

    /**
     * Add units to tag set
     *
     * @return tags
     *  Tags to ID this experiment
     */
    @Override
    public List<String> getTags() {
        Set<String> tags = new HashSet<>();
        tags.addAll(super.getTags());

        tags.add(getFailUnit().toLowerCase());
        tags.add(getPassUnit().toLowerCase());

        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(tags);
        return tagsList;
    }

    /**
     * Calculates the total number of successes across all trials in this experiment.
     * NOTE: Ignores results of ignored users
     * @return The total number of successes.
     */
    public int getSuccessCount() {
        ArrayList<String> ignoredIDs = new ArrayList<String>();
        for (User user : this.getIgnoredUsers()) {
            ignoredIDs.add(user.getId());
        }
        int successCount = 0;
        for (Trial trial : getTrials()) {
            if (!ignoredIDs.contains(trial.getExperimenter().getId())) {
                successCount += ((BinomialTrial) trial).getPassCount();
            }
        }
        return successCount;
    }

    /**
     * Calculates the total number of failures across all trials in this experiment.
     * NOTE: Ignores results of ignored users
     * @return The total number of failures.
     */
    public int getFailureCount() {
        ArrayList<String> ignoredIDs = new ArrayList<String>();
        for (User user : this.getIgnoredUsers()) {
            ignoredIDs.add(user.getId());
        }
        int failureCount = 0;
        for (Trial trial : getTrials()) {
            if (!ignoredIDs.contains(trial.getExperimenter().getId())) {
                failureCount += ((BinomialTrial) trial).getFailCount();
            }
        }
        return failureCount;
    }

    /**
     * Calculates the success rate as the number of success divided by the total number of events.
     * @return The success rate.
     */
    public double getSuccessRate() {
        double successCount = getSuccessCount();
        double failureCount = getFailureCount();
        double total = successCount + failureCount;
        return successCount / total;
    }
}
