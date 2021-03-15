package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CountExperiment extends Experiment {
    // The name of the unit of measurement for trials in this experiment.
    private String unit;
    public static final String type = "Count";

    public CountExperiment() {
    }

    public CountExperiment(User owner, String description, String unit) {
        super(owner, description);
        this.unit = unit;
    }

    @Override
    public String getType() {
        return type;
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

        tags.add(getUnit());

        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(tags);
        return tagsList;
    }
}
