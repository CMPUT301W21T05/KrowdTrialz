package com.T05.krowdtrialz.model.trial;

import android.location.Location;

import com.T05.krowdtrialz.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * MeasurementTrial holds a floating point trial result
 * which could represent something like "my height" for instance.
 * */
public class MeasurementTrial extends Trial {
    private Float measurementValue;

    public MeasurementTrial(User user, Location location) {
        super(user, location);
    }

    public MeasurementTrial(User user, Location location, LocalDateTime dateCreated) {
        super(user, location, dateCreated);
    }

    public Float getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(Float measurementValue) {
        this.measurementValue = measurementValue;
    }
}
