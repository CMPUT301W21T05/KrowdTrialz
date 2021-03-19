package com.T05.krowdtrialz.model.experiment;

import com.T05.krowdtrialz.model.interfaces.Tagged;
import com.T05.krowdtrialz.model.scannable.Barcode;
import com.T05.krowdtrialz.model.scannable.QRCode;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base class to represent all types of experiments. Note: experiments are active by default.
 */
public abstract class Experiment implements Tagged {
    private String id;
    private User owner;
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
        barcodes = new ArrayList<Barcode>();
        qrCodes = new ArrayList<QRCode>();
        status = active;
        ignoredUsers = new ArrayList<User>();
    }

    /**
     * Get the trials for this experiment
     *
     * @return generic list of trials
     */
    abstract public ArrayList<? extends Trial> getTrials();

    /**
     * Add a new trial to the experiment
     *
     * @param trial the trial to be added
     * @param <E> trial type matching the experiment type
     *
     * @alert trial type E must match the experiment type.
     */
    abstract public <E extends Trial> void addTrial(E trial);

    /**
     *
     * @return
     *  ID of the current experiment
     */
    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    /**
     * Get the barcodes associated with this Experiment
     *
     * @return array of associated barcodes
     */
    public ArrayList<Barcode> getBarcodes() {
        return barcodes;
    }

    /**
     * Add a new barcode to be associated with this experiment
     *
     * @param barcode
     *      barcode to add
     */
    public void addBarcode(Barcode barcode) {
        barcodes.add(barcode);
    }


    /**
     * Get the QR codes associated with this experimet
     *
     * @return
     *  Array of associated QR codes
     */
    public ArrayList<QRCode> getQrCodes() {
        return qrCodes;
    }

    /**
     * Add new QR code associated with this experiment
     *
     * @param qrCode
     *  QR code to add
     */
    public void addQRCode(QRCode qrCode) {
        qrCodes.add(qrCode);
    }

    /**
     * Get owner of this experiment
     *
     * @return
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Query if the given user owns the current experiment
     *
     * @param user user to check if owner
     * @return
     *  true if owner, otherwise false
     */
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

    /**
     * Get array of users to be excluded from results
     *
     * @return array of ignored users
     */
    public ArrayList<User> getIgnoredUsers(){
        return ignoredUsers;
    }

    /**
     * Check if the give user is to be ignored
     *
     * @param user
     * @return true if user is ignored, false otherwise
     */
    public boolean isIgnored(User user){
        for (User i : ignoredUsers){
            if(i.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set user to be ignored in this experiment's results
     *
     * @param user user to be ignored
     */
    public void ignoreUser(User user){
        ignoredUsers.add(user);
    }

    /**
     * Ignore users as a batch action
     *
     * @param users
     */
    public void ignoreMultipleUsers(ArrayList<User> users){
        ignoredUsers.addAll(users);
    }

    public void removeIgnoredUser(User user){
        if(ignoredUsers.contains(user)){
            ignoredUsers.remove(user);
        }
    }

    /**
     * Get all contriubtors to this experiment regardless of if they're ignored
     *
     * @return
     *  Set of users
     */
    @Exclude
    public Set<User> getContributors() {
        ArrayList<Trial> trials = (ArrayList<Trial>) getTrials();
        Set<User> users = new HashSet<>();
        trials.stream().forEach(trial -> users.add(trial.getExperimenter()));
        return users;
    }

    /**
     * Get the trials only from the non-ignored contributors
     *
     * @return array of trials from valid users
     */
    @Exclude
    public ArrayList<? extends Trial> getValidTrials() {
        ArrayList<? extends Trial> trials = getTrials();
        ArrayList<Trial> validTrials = new ArrayList<>();

        for (Trial trial : trials) {
            if (!isIgnored(trial.getExperimenter())) {
                validTrials.add(trial);
            }
        }

        return validTrials;
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

    /**
     * Query if the experiment is inactive
     *
     * @return true if inactive
     */
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
            if (!getOwner().getName().equals("None")) {
                tags.addAll(Arrays.asList(getOwner().getName().toLowerCase().split(" ")));
            }
            if (!getOwner().getUserName().equals("None")) {
                tags.add(getOwner().getUserName().toLowerCase());
            }
            if (!getOwner().getEmail().equals("None")) {
                tags.add(getOwner().getEmail().toLowerCase());
            }
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
