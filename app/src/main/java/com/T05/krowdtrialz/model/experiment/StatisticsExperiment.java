package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Statistics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Super class for types of experiments for whiche statistics such as mean and standard deviation
 * can be calculated.
 */
public abstract class StatisticsExperiment extends Experiment {

    public StatisticsExperiment() {
    }

    public StatisticsExperiment(User owner, String description) {
        super(owner, description);
    }

    /**
     * Helper method to get trial data as a array of doubles.
     * This needs to be implemented by subclasses.
     *
     * @return Trial data as an array of doubles.
     */
    protected abstract double[] getDataAsArray();

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
     * Add information to tags
     *
     * @return tags
     *  Tags to ID this experiment
     */
    @Override
    public List<String> getTags() {
        Set<String> tags = new HashSet<>();
        tags.addAll(super.getTags());

        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(tags);
        return tagsList;
    }
}
