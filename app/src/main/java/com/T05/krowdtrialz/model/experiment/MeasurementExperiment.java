package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Statistics;

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
     *
     * @return The standard deviation.
     */
    public double getStdDev() {
        double[] data = getDataAsArray();
        return Statistics.standardDeviation(data);
    }

    /**
     * Calculates the value of the qth quartile.
     * For example, getQuartile(2) returns the 2nd quartile (median).
     *
     * @param q The number of the quartile.
     * @return The value of the qth quartile.
     */
    public double getQuartile(int q) {
        double[] data = getDataAsArray();
        return Statistics.quartile(data, q);
    }

    /**
     * Calculates the mean value of all trials in this experiment.
     *
     * @return The mean.
     */
    public double getMean() {
        double[] data = getDataAsArray();
        return Statistics.mean(getDataAsArray());
    }

    /**
     * Helper method to get trial data as a array of doubles.
     * @return Trial data as an array of doubles.
     */
    private double[] getDataAsArray() {
        return getTrials().stream()
                .mapToDouble(trial -> ((MeasurementTrial) trial).getMeasurementValue())
                .toArray();
    }
}
