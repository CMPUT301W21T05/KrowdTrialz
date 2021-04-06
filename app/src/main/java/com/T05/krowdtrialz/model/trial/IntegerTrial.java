package com.T05.krowdtrialz.model.trial;

import android.location.Location;

import com.T05.krowdtrialz.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * IntegerTrials hold a single value which is the
 * output of the trial. The integer value could represent
 * something like "number of laps completed in 10 min." for
 * instance.
 * */
public class IntegerTrial extends Trial {
    private int value;

    public IntegerTrial() {
        super();
    }

    public IntegerTrial(User user) {
        super(user);
    }

    public IntegerTrial(User user, double longitude, double latitude) {
        super(user, longitude, latitude);
    }

    public IntegerTrial(User user, double longitude, double latitude, String dateCreated) {
        super(user, longitude, latitude, dateCreated);
    }

    /**
     * Get integer trial value
     *
     * @return
     *  value of the trial result
     */
    public int getValue() {
        return value;
    }

    /**
     * Set value of the integer trial
     *
     * @param value trial result
     */
    public void setValue(int value) {
        this.value = value;
    }
}
