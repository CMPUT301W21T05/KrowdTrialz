package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.interfaces.Tagged;
import com.T05.krowdtrialz.model.scannable.Barcode;
import com.T05.krowdtrialz.model.scannable.QRCode;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base class to represent all types of experiments.
 */
public abstract class Experiment implements Tagged {
    private String id;
    private User owner;
    private ArrayList<Trial> trials;
    private String description;
    private String region;
    private String type;
    private boolean locationRequired = false;
    public boolean status;
    private int minTrials = 0;
    private ArrayList<Barcode> barcodes;
    private ArrayList<QRCode> qrCodes;
    private ArrayList<User> ignoredUsers;

    private final boolean active = true;
    private final boolean inactive = false;

    public Experiment() {
    }

    public Experiment(User owner, String description) {
        // TODO generate unique id
        this.owner = owner;
        this.description = description;

        trials = new ArrayList<Trial>();
        barcodes = new ArrayList<Barcode>();
        qrCodes = new ArrayList<QRCode>();
        status = active;
        ignoredUsers = new ArrayList<User>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public ArrayList<Trial> getTrials() {
        return trials;
    }

    public void addTrial(Trial trial) {
        trials.add(trial);
    }

    public ArrayList<Barcode> getBarcodes() {
        return barcodes;
    }

    public void addBarcode(Barcode barcode) {
        barcodes.add(barcode);
    }

    public ArrayList<QRCode> getQrCodes() {
        return qrCodes;
    }

    public void addQRCode(QRCode qrCode) {
        qrCodes.add(qrCode);
    }

    public User getOwner() {
        return owner;
    }

    public boolean isOwner(User user){
        if(owner.getId().equals(user.getId())){
            return true;
        } else{
            return false;
        }
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isLocationRequired() {
        return locationRequired;
    }

    public void setLocationRequired(boolean locationRequired) {
        this.locationRequired = locationRequired;
    }

    public int getMinTrials() {
        return minTrials;
    }

    public void setMinTrials(int minTrials) {
        this.minTrials = minTrials;
    }

    abstract public String getType();

    public ArrayList<User> getIgnoredUsers(){
        return ignoredUsers;
    }

    public boolean isIgnored(User user){
        for (User i : ignoredUsers){
            if(i.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

    public void ignoreUser(User user){
        ignoredUsers.add(user);
    }

    public void ignoreMultipleUsers(ArrayList<User> users){
        ignoredUsers.addAll(users);
    }

    public void removeIgnoredUser(User user){
        if(ignoredUsers.contains(user)){
            ignoredUsers.remove(user);
        }
    }

    /**
     * Set experiment status to active
     */
    public void setActive() {
        status = active;
    }

    public boolean isActive() {
        return status == active;
    }

    /**
     * Set experiment status to inactive
     */
    public void setInactive() {
        status = inactive;
    }

    public boolean isInactive() {
        return status == inactive;
    }

    /**
     * Adds all strings to a set that a user may want to search by
     *
     * @alert
     *  Make sure all tags are lower case since queries will be put to lower case
     *
     * @return tags
     *  A set of tags that can be searched
     *
     */
    @Override
    public List<String> getTags() {
        Set<String> tags = new HashSet<>();

        // add description tags - remove spaces and punctuation then filter any null strings from list
        if (getDescription() != null) {
            tags.addAll(Arrays.asList(getDescription().toLowerCase().split("[^A-Za-z1-9]"))
                    .stream()
                    .filter(item -> item != null && !item.isEmpty())
                    .collect(Collectors.toList()));
        }

        if (getOwner() != null) {
            tags.addAll(Arrays.asList(getOwner().getName().toLowerCase().split(" ")));
            tags.add(getOwner().getUserName().toLowerCase());
            tags.add(getOwner().getEmail().toLowerCase());
        }

        if (getRegion() != null) {
            tags.addAll(Arrays.asList(getRegion().toLowerCase().split(" ")));
        }

        tags.add(getType().toLowerCase());
        tags.add(((Integer) getMinTrials()).toString());


        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(tags);
        return tagsList;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Experiment)) {
            return false;
        }

        Experiment c = (Experiment) o;

        return getId().equals(c.getId());
    }

    @Override
    public int hashCode() {
        int result = 0x05;
        result = 31 * result + getId().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getType().hashCode();
        return result;
    }
}
