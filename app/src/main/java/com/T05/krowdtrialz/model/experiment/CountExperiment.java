package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.user.User;

public class CountExperiment extends Experiment {
    // The name of the unit of measurement for trials in this experiment.
    private String unit;

    public CountExperiment() {
    }

    public CountExperiment(User owner, String description, String unit) {
        super(owner, description);
        this.unit = unit;
    }

    /**
     * Calculates the total count across all trials in this experiment.
     * @return The total count.
     */
    public int getCount() {
        return getTrials().size();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
