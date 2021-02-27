package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.user.User;

public class MeasurementExperiment extends Experiment {
    // The name of the unit of measurement for trials in this experiment.
    private String unit;

    public MeasurementExperiment(User owner, String description, String unit) {
        super(owner, description);
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Calculates the standard deviation of all trials in this experiment.
     * @return The standard deviation.
     */
    public double getStdDev() {
        // TODO
        return 0;
    }

    /**
     * Calculates the value of the qth quartile.
     * For example, getQuartile(2) returns the 2nd quartile (median).
     *
     * @param q The number of the quartile.
     * @return The value of the qth quartile.
     */
    public double getQuartile(int q) {
        // TODO
        return 0;
    }

    /**
     * Calculates the mean value of all trials in this experiment.
     * @return The mean.
     */
    public float getMean() {
        // TODO
        return 0;
    }
}
