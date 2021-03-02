package com.T05.krowdtrialz.util;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 * Utility class for implementing common statistics calculations such as standard deviation and
 * quartiles.
 *
 * This class acts as a thin wrapper over the Apache Commons Math library.
 */
public class Statistics {
    private static final Mean meanInstance = new Mean();
    private static final StandardDeviation standardDeviationInstance = new StandardDeviation();
    private static final Percentile percentileInstance = new Percentile();

    /**
     * Calculates the mean of the data.
     * Returns NaN if data is empty array.
     *
     * @param data The data to calculate the mean for.
     * @return The mean.
     */
    public static double mean(double[] data) {
        return meanInstance.evaluate(data);
    }

    /**
     * Calculates the standard deviation of the data.
     * Returns NaN if data is empty array.
     *
     * @param data The data to calculate the standard deviation for.
     * @return The standard deviation.
     */
    public static double standardDeviation(double[] data) {
        return standardDeviationInstance.evaluate(data);
    }

    /**
     * Calculates the value of the qth quartile.
     * For example, quartile(2) returns the 2nd quartile (median).
     *
     * @param q The number of the quartile.
     * @return The value of the qth quartile.
     */
    public static double quartile(double[] data, int q) {
        double percentile = 25 * q;
        return percentileInstance.evaluate(data, percentile);
    }
}
