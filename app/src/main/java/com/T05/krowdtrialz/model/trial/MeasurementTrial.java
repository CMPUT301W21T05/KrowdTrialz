package com.T05.krowdtrialz.model.trial;

import android.location.Location;

import com.T05.krowdtrialz.model.user.User;

import java.time.LocalDate;

/**
 * MeasurementTrial holds a floating point trial result
 * which could represent something like "my height" for instance.
 * */
public class MeasurementTrial extends Trial {
    private Float measurementValue;

    public MeasurementTrial(User user, Location location) {
        super(user, location);
    }

    public MeasurementTrial(User user, Location location, LocalDate dateCreated) {
        super(user, location, dateCreated);
    }

    public Float getMeasurementValue() {
        return measurementValue;
    }
}
