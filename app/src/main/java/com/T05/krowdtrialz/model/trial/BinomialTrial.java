package com.T05.krowdtrialz.model.trial;

import android.location.Location;

import com.T05.krowdtrialz.model.user.User;

import java.time.LocalDate;

/**
 * the BinomialTrial maintains a count of the number of
 * "passes" and "fails" in a trial. For instance, the number
 * of heads and tails for 5 coin flips. Definition of "pass"
 * and "fail" are maintained by the experiment.
 * */
public class BinomialTrial extends Trial {
    private int passCount, failCount;

    public BinomialTrial(User user, Location location) {
        super(user, location);
    }

    public BinomialTrial(User user, Location location, LocalDate dateCreated) {
        super(user, location, dateCreated);
    }

    public Integer getPassCount() {
        return passCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }
}
