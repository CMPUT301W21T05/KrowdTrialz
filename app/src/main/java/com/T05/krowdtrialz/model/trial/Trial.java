package com.T05.krowdtrialz.model.trial;

import android.location.Location;

import com.T05.krowdtrialz.model.user.User;

import java.time.LocalDate;

/**
 * The Trial class maintains metadata needed by all
 * trial types.
 * */
public abstract class Trial {
    private User experimenter;
    private Location location;
    private LocalDate dateCreated;

    public Trial(User user, Location location) {
        this.experimenter = user;
        this.location = location;
        this.dateCreated = LocalDate.now();
    }

    public Trial(User user, Location location, LocalDate dateCreated) {
        this(user, location);
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Location getLocation() {
        return location;
    }

    public User getExperimenter() {
        return experimenter;
    }
}
