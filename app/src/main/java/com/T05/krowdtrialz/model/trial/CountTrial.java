package com.T05.krowdtrialz.model.trial;

import android.location.Location;

import com.T05.krowdtrialz.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The CountTrial holds a count of something. The number
 * of blue cars seen on the road in Edmonton, for example.
 * */
public class CountTrial extends Trial {
    public CountTrial(User user, Location location) {
        super(user, location);
    }

    public CountTrial(User user, Location location, LocalDateTime dateCreated) {
        super(user, location, dateCreated);
    }
}
