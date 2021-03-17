package com.T05.krowdtrialz.model.trial;

import android.location.Location;

import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The CountTrial holds a count of something. The number
 * of blue cars seen on the road in Edmonton, for example.
 * */
public class CountTrial extends Trial {

    public CountTrial() {
        super();
    }

    public CountTrial(User user, int longitude, int latitude) {
        super(user, longitude, latitude);
    }

    public CountTrial(User user, int longitude, int latitude, String dateCreated) {
        super(user, longitude, latitude, dateCreated);
    }
}
