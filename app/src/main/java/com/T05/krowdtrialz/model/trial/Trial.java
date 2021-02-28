package com.T05.krowdtrialz.model.trial;

import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Trial(User user, Location location) {
        this.experimenter = user;
        this.location = location;
        this.dateCreated = LocalDate.now();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
