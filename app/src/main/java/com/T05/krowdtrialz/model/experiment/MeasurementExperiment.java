package com.T05.krowdtrialz.model.experiment;

import android.widget.ArrayAdapter;

import com.T05.krowdtrialz.model.trial.IntegerTrial;
import com.T05.krowdtrialz.model.trial.MeasurementTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MeasurementExperiment extends StatisticsExperiment {
    public static final String type = "Measurement";
    // The name of the unit of measurement for trials in this experiment.
    private String unit;

    private ArrayList<MeasurementTrial> trials;

    public MeasurementExperiment() {
        trials = new ArrayList<>();
    }

    public MeasurementExperiment(User owner, String description, String unit) {
        super(owner, description);
        this.unit = unit;
        trials = new ArrayList<>();
    }

    @Override
    public ArrayList<? extends Trial> getTrials() {
        return trials;
    }

    @Override
    public <E extends Trial> void addTrial(E trial) {
        trials.add((MeasurementTrial) trial);
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
     * Helper method to get trial data as a array of doubles.
     * @return Trial data as an array of doubles.
     */
    @Override
    protected double[] getDataAsArray() {
        return getValidTrials().stream()
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
    public List<String> getTags() {
        Set<String> tags = new HashSet<>();
        tags.addAll(super.getTags());

        tags.add(getUnit().toLowerCase());

        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(tags);
        return tagsList;
    }
}
