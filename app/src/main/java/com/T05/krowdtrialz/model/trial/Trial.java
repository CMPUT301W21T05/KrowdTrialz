package com.T05.krowdtrialz.model.trial;

import android.location.Location;

import com.T05.krowdtrialz.model.User;

import java.util.Date;

/**
 * The Trial class maintains metadata needed by all
 * trial types.
 * */
public abstract class Trial {
    private User experimenter;
    private Location location;
    private Date dateCreated;

    public Trial(User user, Location location) {
        this.experimenter = user;
        this.location = location;
        this.dateCreated = new Date();
    }

    public Trial(User user, Location location, Date dateCreated) {
        this(user, location);
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Location getLocation() {
        return location;
    }

    public User getExperimenter() {
        return experimenter;
    }
}
