package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BinomialExperiment extends Experiment {
    public static final String type = "Binomial";
    // The name of a fail event. (e.g. "heads")
    private String passUnit;
    // The name of a fail event. (e.g. "tails")
    private String failUnit;

    public BinomialExperiment() {
    }

    public BinomialExperiment(User owner, String description, String passUnit, String failUnit) {
        super(owner, description);
        this.passUnit = passUnit;
        this.failUnit = failUnit;
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

        tags.add(getFailUnit());
        tags.add(getPassUnit());

        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(tags);
        return tagsList;
    }

    /**
     * Calculates the total number of successes across all trials in this experiment.
     * @return The total number of successes.
     */
    public int getSuccessCount() {
        int successCount = 0;
        for (Trial trial : getTrials()) {
            successCount += ((BinomialTrial) trial).getPassCount();
        }
        return successCount;
    }

    /**
     * Calculates the total number of failures across all trials in this experiment.
     * @return The total number of failures.
     */
    public int getFailureCount() {
        int failureCount = 0;
        for (Trial trial : getTrials()) {
            failureCount += ((BinomialTrial) trial).getFailCount();
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
