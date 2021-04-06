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

    public MeasurementTrial() {
        super();
    }

    public MeasurementTrial(User user) {
        super(user);
    }

    public MeasurementTrial(User user, double longitude, double latitude) {
        super(user, longitude, latitude);
    }

    public MeasurementTrial(User user, double longitude, double latitude, String dateCreated) {
        super(user, longitude, latitude, dateCreated);
    }

    /**
     * Get value of the measurement trial
     * @return
     *  floating point measurement
     */
    public Float getMeasurementValue() {
        return measurementValue;
    }

    /**
     * Add result to the trial
     *
     * @param measurementValue trial result
     */
    public void setMeasurementValue(Float measurementValue) {
        this.measurementValue = measurementValue;
    }
}
