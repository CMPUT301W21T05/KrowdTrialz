package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.user.User;

import java.util.ArrayList;
import java.util.Set;

public class MeasurementExperiment extends StatisticsExperiment {
    // The name of the unit of measurement for trials in this experiment.
    private String unit;

    public MeasurementExperiment() {
    }

    public MeasurementExperiment(User owner, String description, String unit) {
        super(owner, description);
        this.unit = unit;
        this.setType("Measurement");
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Helper method to get trial data as a array of doubles.
     * @return Trial data as an array of doubles.
     */
    @Override
    protected double[] getDataAsArray() {
        return getTrials().stream()
                .mapToDouble(trial -> ((MeasurementTrial) trial).getMeasurementValue())
                .toArray();
    }

    /**
     * Add units to tag set
     *
     * @return tags
     *  Tags to ID this experiment
     */
    @Override
    public ArrayList<String> getTags() {
        ArrayList<String> tags = super.getTags();

        tags.add(getUnit());

        return tags;
    }
}
