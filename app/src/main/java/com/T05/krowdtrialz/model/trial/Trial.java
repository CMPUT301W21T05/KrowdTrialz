package com.T05.krowdtrialz.model.trial;

import android.location.Location;

import com.T05.krowdtrialz.model.user.User;
import com.google.type.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The Trial class maintains metadata needed by all
 * trial types.
 * */
public abstract class Trial {
    private User experimenter;
    private Location location;
    private LocalDateTime dateCreated;

    public Trial(User user, Location location) {
        this.experimenter = user;
        this.location = location;
        this.dateCreated = LocalDateTime.now();
    }

    public Trial(User user, Location location, LocalDateTime dateCreated) {
        this(user, location);
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateCreated() { return dateCreated; }

    public Location getLocation() {
        return location;
    }

    public User getExperimenter() {
        return experimenter;
    }
}
