package com.T05.krowdtrialz.model.trial;

import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.T05.krowdtrialz.model.user.User;

import java.time.LocalDate;

/**
 * The CountTrial holds a count of something. The number
 * of blue cars seen on the road in Edmonton, for example.
 * */
public class CountTrial extends Trial {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public CountTrial(User user, Location location) {
        super(user, location);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CountTrial(User user, Location location, LocalDate dateCreated) {
        super(user, location, dateCreated);
    }
}
