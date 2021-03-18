package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IntegerExperiment extends StatisticsExperiment {
    public static final String type = "Integer";
    private String unit;

    private ArrayList<IntegerTrial> trials;

    public IntegerExperiment() {
        trials = new ArrayList<>();
    }

    public IntegerExperiment(User owner, String description, String unit) {
        super(owner, description);
        trials = new ArrayList<>();
        this.unit = unit;
    }

    @Override
    public <E extends Trial> void addTrial(E trial) {
        trials.add((IntegerTrial) trial);
    }

    @Override
    public ArrayList<? extends Trial> getTrials() {
        return trials;
    }

    @Override
    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Calculates the sum of the values of all trials in this experiment.
     * @return The total.
     */
    public int getTotal() {
        return getTrials().stream()
                .mapToInt(trial -> ((IntegerTrial) trial).getValue())
                .sum();
    }

    /**
     * Helper method to get trial data as a array of doubles.
     * @return Trial data as an array of doubles.
     */
    @Override
    protected double[] getDataAsArray() {
        return getValidTrials().stream()
                .mapToDouble(trial -> ((IntegerTrial) trial).getValue())
                .toArray();
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

        tags.add(getUnit().toLowerCase());

        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(tags);
        return tagsList;
    }
}
