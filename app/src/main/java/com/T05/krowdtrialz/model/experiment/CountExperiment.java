package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.trial.CountTrial;
import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CountExperiment extends Experiment {
    public static final String type = "Count";
    // The name of the unit of measurement for trials in this experiment.
    private ArrayList<CountTrial> trials;

    private String unit;

    public CountExperiment() {
        trials = new ArrayList<>();
    }

    public CountExperiment(User owner, String description, String unit) {
        super(owner, description);
        trials = new ArrayList<>();
        this.unit = unit;
    }

    @Override
    public ArrayList<? extends Trial> getTrials() {
        return trials;
    }

    @Override
    public <E extends Trial> void addTrial(E trial) {
        trials.add((CountTrial) trial);
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

        tags.add(getUnit().toLowerCase());

        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(tags);
        return tagsList;
    }
}
