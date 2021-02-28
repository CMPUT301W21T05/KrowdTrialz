package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.user.User;

public class BinomialExperiment extends Experiment {
    // The name of a fail event. (e.g. "heads")
    private String passUnit;
    // The name of a fail event. (e.g. "tails")
    private String failUnit;

    public BinomialExperiment(User owner, String description, String passUnit, String failUnit) {
        super(owner, description);
        this.passUnit = passUnit;
        this.failUnit = failUnit;
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
     * Calculates the total number of successes across all trials in this experiment.
     * @return The total number of successes.
     */
    public int getSuccessCount() {
        // TODO
        return 0;
    }

    /**
     * Calculates the total number of failures across all trials in this experiment.
     * @return The total number of failures.
     */
    public int getFailureCount() {
        // TODO
        return 0;
    }

    /**
     * Calculates the success rate as the number of success divided by the total number of events.
     * @return The success rate.
     */
    public double getSuccessRate() {
        // TODO
        return 0;
    }
}
