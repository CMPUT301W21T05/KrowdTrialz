package com.T05.krowdtrialz.model.trial;

import android.location.Location;

import com.T05.krowdtrialz.model.user.User;

import java.util.Date;

/**
 * IntegerTrials hold a single value which is the
 * output of the trial. The integer value could represent
 * something like "number of laps completed in 10 min." for
 * instance.
 * */
public class IntegerTrial extends Trial {
    private Integer value;

    public IntegerTrial(User user, Location location) {
        super(user, location);
    }

    public IntegerTrial(User user, Location location, Date dateCreated) {
        super(user, location, dateCreated);
    }

    public Integer getValue() {
        return value;
    }
}
