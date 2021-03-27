package com.T05.krowdtrialz.model.trial;

import com.T05.krowdtrialz.model.user.User;
import com.google.firebase.firestore.Exclude;

import java.time.LocalDateTime;

/**
 * The Trial class maintains metadata needed by all
 * trial types such as location, creator, and date created.
 *
 * @alert non-serializable classes cannot be used in this class because of the
 * restrictions imposed by firestore
 * */
public abstract class Trial {
    private User experimenter;
    private double longitude;
    private double latitude;
    private String dateCreated;

    public Trial() {}

    public Trial(User user) {
        this.experimenter = user;
    }

    public Trial(User user, double longitude, double latitude) {
        this.experimenter = user;
        this.longitude = longitude;
        this.latitude = latitude;
        LocalDateTime dateTime = LocalDateTime.now();
        this.dateCreated = String.format("%s/%s/%s", dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
    }

    public Trial(User user, double longitude, double latitude, String dateCreated) {
        this(user, longitude, latitude);
        this.dateCreated = dateCreated;
    }

    public String getDateCreated() { return dateCreated; }

    @Exclude
    public int getYearCreated() { return Integer.parseInt(dateCreated.substring(0,dateCreated.indexOf("/")));}

    @Exclude
    public int getMonthCreated() { return Integer.parseInt(dateCreated.substring(dateCreated.indexOf("/") + 1,dateCreated.lastIndexOf("/")));}

    @Exclude
    public int getDayCreated() { return Integer.parseInt(dateCreated.substring(dateCreated.lastIndexOf("/") + 1, dateCreated.length()));}

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() { return latitude; }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get the user who created the trial
     *
     * @return user who added this trial
     */
    public User getExperimenter() {
        return experimenter;
    }
}
