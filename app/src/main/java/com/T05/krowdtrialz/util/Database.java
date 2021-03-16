package com.T05.krowdtrialz.util;


import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.content.ContentValues.TAG;


/**
 * This class handles everything to do with reading and writing to firestore database
 * This class is a singleton.
 * @author
 *  Furmaan Sekhon and Jacques Leong-Sit
 */


public class Database {

    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    // The user with a unique id associated with this device.
    // This is populated when the app is opened.
    private User deviceUser;

    // The single instance for the singleton pattern
    private static Database instance = null;

    /**
     * Initializes the single instance.
     * This must be called before getInstance() is called.
     * This should be called from MainActivity when the app starts.
     * @author Ryan Shukla
     * @param sharedPreferences The app's instance of SharedPreferences.
     * @param callback Callback to be called when all initialization is finished.
     */
    public static void initializeInstance(
            SharedPreferences sharedPreferences,
            InitializeDatabaseCallback callback) {
        if (instance == null) {
            instance = new Database(sharedPreferences);
            instance.initializeDeviceUser(callback);
        }
    }

    /**
     * Gets the single instance of the Database class.
     * This method throws a RuntimeException if it is called before initializeInstance.
     * @return The instance.
     */
    public static Database getInstance() {
        if (instance == null) {
            throw new RuntimeException("Tried to get Database instance before initializeInstance was called");
        }
        return instance;
    }

    /**
     * This constructor has been made private to enforce the singleton pattern.
     * @param sharedPreferences The app's instance of SharedPreferences.
     */
    private Database(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }


    /**
     * This gets an experiment of a unique ID
     * @param expID An Experiment ID
     * @author Vasu Gupta
     */
    public void getExperimentByID(String expID, GetExperimentCallback callback){
        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("AllExperiments");

        Query query = userCollectionReference.whereEqualTo("id", expID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().size() == 1){
                    Experiment experiment = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.get("type").toString().equals("Binomial")){
                            experiment = document.toObject(BinomialExperiment.class);
                        }else if(document.get("type").toString().equals("Count")){
                            experiment = document.toObject(CountExperiment.class);
                        }else if(document.get("type").toString().equals("Measurement")){
                            experiment = document.toObject(MeasurementExperiment.class);
                        }else if(document.get("type").toString().equals("Integer")){
                            experiment = document.toObject(IntegerExperiment.class);
                        }else {
                            Log.e(TAG, "Unknown experiment type");
                            callback.onFailure();
                            return;
                        }
                    }
                    Log.d(TAG, "Experiment of ID " + expID.toString() + " found.");
                    callback.onSuccess(experiment);
                } else if (task.getResult().size() > 1) {
                    Log.e(TAG, "Multiple experiments with same ID " + expID.toString() + " found.");
                    callback.onFailure();
                } else {
                    Log.e(TAG, "No experiment of ID: " + expID.toString() + " found.");
                    callback.onFailure();
                }
            }
        });
    }


    /**
     * This saves unique id after initializeDeviceUser generates a unique id
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    private void saveID(InitializeDatabaseCallback callback) {

        // allows variable to be saved
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String localID = deviceUser.getId();
        String convertedID = gson.toJson(localID); // converts arrayList to json
        editor.putString("ID",convertedID); // saves converted arrayList
        editor.apply();
        Log.d("saveID","Saved ID: "+localID);

        callback.onSuccess();

    }// end saveID

    /**
     * This initializes a User instance with a unique id associated with this device.
     * To generate the id, this code checks if the device already has an id saved and if not, it
     * generates a unique id and saves it to device and also to the database.
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    public void initializeDeviceUser(InitializeDatabaseCallback callback) {
        Log.d(TAG, "initializeDeviceUser");

        Gson gson = new Gson();
        String convertedID = sharedPreferences.getString("ID", null);// gets saved arrayList (in json form) null if there is none saved
        Type type = new TypeToken<String>() {}.getType();
        String localID = gson.fromJson(convertedID, type);

        if (localID == null) {
            // No ID has been generated for this device.
            // We must generate a new User with a new ID.

            UUID uuid = UUID.randomUUID();
            localID = uuid.toString();

            // Create new User with default contact information
            Log.d(TAG, "construct deviceUser");
            deviceUser = new User(localID.toString());

            verifyUserID(deviceUser, new Database.GenerateIDCallback() {
                @Override
                public void onSuccess(User user) {
                    saveID(callback);
                }

                @Override
                public void onFailure() {
                    // ID was not unique, try again
                    initializeDeviceUser(callback);
                }
            });
        } else {
            // User exists, restore the user's info from the database
            getUserById(localID, new GetUserCallback() {
                @Override
                public void onSuccess(User user) {
                    deviceUser = user;
                    Log.d(TAG, "Successfully retrieved existing user information from database."
                            + " ID: " + deviceUser.getId());
                    callback.onSuccess();
                }

                @Override
                public void onFailure() {
                    Log.e(TAG, "Failed to find user in database with ID from SharedPreferences.");
                    callback.onFailure();
                }
            });
        }

    }// end loadID

    /**
     * Gets a User instance with a unique id associated with this device.
     * In order for this to return the correct User, the app should have called initializeUser first
     * and allowed ample time to confirm the ID with the database.
     * @return The User associated with this device..
     */
    public User getDeviceUser() {
        return deviceUser;
    }

    /**
     * Updates the User instance associated with this device and also updates the user's
     * information in the database.
     *
     * Note: this cannot be used to change the ID associated with this device.
     *   The argument "user" should have the same ID as the current user.
     *
     * @param user A User instance with updated info
     * @author Ryan Shukla
     */
    public void updateDeviceUser(User user) {
        // TODO do we need to update the user in every experiment?

        // Update the user in the database
        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        userCollectionReference.document(deviceUser.getId()).set(user);

        // Update the user locally
        deviceUser = user;
    }

    /**
     * Queries for the user associated with the given ID.
     * @param id The ID of the user to search for.
     * @param callback The callback to be called when the query is finished.
     * @author Ryan Shukla
     */
    public void getUserById(String id, GetUserCallback callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");

        Query query = userCollectionReference.whereEqualTo("id", id); // Create a query to check if ID exists in the database already
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().size() == 1) {
                    User user = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        user = document.toObject(User.class);
                    }
                    callback.onSuccess(user);
                } else if (task.getResult().size() > 1) {
                    Log.e(TAG, "Multiple users with same ID found.");
                    callback.onFailure();
                } else {
                    Log.e(TAG, "No user with given ID found.");
                    callback.onFailure();
                }
            }
        });
    }

    /**
     * This method checks if user already exits in the database* and returns a new unique id if they don't
     * or existing one if they do
     */
    public void verifyUserID(User user, GenerateIDCallback callback) {

        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");

        Query query = userCollectionReference.whereEqualTo("id", user.getId()); // Create a query to check if ID exists in the database already

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().isEmpty()) {
                    // create new user in the database
                    // This means the id generated is unique
                    userCollectionReference
                            .document(user.getId())
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Data has been added");
                                    callback.onSuccess(user);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Data cannot be added" + e.toString());
                                    callback.onFailure();

                                }
                            });

                }else {

                    // This means the id generated is not unique
                    callback.onFailure();

                }
            }
        });

    }// end generateID

    /**
     * This method adds the given experiment to the database
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    public void addExperiment (Experiment experiment) {

        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        CollectionReference lookupCollectionReference = db.collection("ExperimentLookups");
        CollectionReference allExpCollectionReference = db.collection("AllExperiments");

        String id = experiment.getOwner().getId();

        // Get a document reference to a document in the user's OwnedExperiments collection
        DocumentReference expRef = userCollectionReference.document(id).collection("OwnedExperiments").document();

        // set experiments ID to the firestore auto generated ID
        String expID = expRef.getId();
        experiment.setId(expID);

        // set the document to the experiment
        expRef.set(experiment);
        DocumentReference allExpRef = allExpCollectionReference.document(expID);
        allExpRef.set(experiment);

        // update lookups
        HashMap<String, Object> newLookup = new HashMap<>();
        newLookup.put("Path", String.format("Users/%s/OwnedExperiments/%s",id,expID ));
        lookupCollectionReference.document(expID).collection("Paths").add(newLookup);
        newLookup.put("Path", String.format("AllExperiments/%s",expID ));
        lookupCollectionReference.document(expID).collection("Paths").add(newLookup);
    }// addExperiment

    /**
     * This method adds the given experiment to the SubscribedExperiments collection in the database and adds its path to the lookups
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     * @param subscriber
     *  This is the user who is subscribing
     * @param experiment
     *  This is the experiment that is being subscribed to
     */
    public void addSubscription (User subscriber, Experiment experiment) {
        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        CollectionReference lookupCollectionReference = db.collection("ExperimentLookups");

        String id = subscriber.getId();
        String expID = experiment.getId(); // get experiment id

        // Get a document reference to a document in the user's SubscribedExperiments collection
        DocumentReference expRef = userCollectionReference.document(id).collection("SubscribedExperiments").document(expID);

        // set the document to the experiment
        expRef.set(experiment);

        // update lookups
        HashMap<String, Object> newLookup = new HashMap<>();
        newLookup.put("Path", String.format("Users/%s/SubscribedExperiments/%s",id,expID ));
        lookupCollectionReference.document(expID).collection("Paths").add(newLookup);

    }// end addSubscription

    /**
     * This method adds the given trial to the given experiment
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     * @param trial
     *  this is the trial to be added
     * @param experiment
     *  this is the experiment that the trial is being added to
     */
    public void addTrial(Trial trial, Experiment experiment) {

        db = FirebaseFirestore.getInstance();
        CollectionReference lookupCollectionReference = db.collection("ExperimentLookups");

        lookupCollectionReference.document(String.valueOf(experiment.getId())).collection("Paths").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // if successful get list of all paths that need updating
                    List<String> pathList = new ArrayList<>();
                    experiment.addTrial(trial);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        pathList.add(String.valueOf(document.get("Path")));
                    }
                    // update each path
                    for (int i = 0; i < pathList.size(); i++) {
                        db.document(pathList.get(i)).set(experiment);
                    }

                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }// end on complete
        });

    }// end addTrial

    /**
     * This method returns a list of experiments that are owned by a given user
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     * @param owner
     *  This is the user that owns the experiments
     */
    public void getExperimentsByOwner (User owner, QueryExperimentsCallback callback) {

        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        String ownerID = owner.getId();
        ArrayList<Experiment> ownedExperiments = new ArrayList<Experiment>();
        userCollectionReference.document(ownerID).collection("OwnedExperiments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(document.get("type").toString().equals("Binomial")){
                        BinomialExperiment experiment = document.toObject(BinomialExperiment.class);
                        ownedExperiments.add(experiment);
                    }else if(document.get("type").toString().equals("Count")){
                        CountExperiment experiment = document.toObject(CountExperiment.class);
                        ownedExperiments.add(experiment);
                    }else if(document.get("type").toString().equals("Measurement")){
                        MeasurementExperiment experiment = document.toObject(MeasurementExperiment.class);
                        ownedExperiments.add(experiment);
                    }else if(document.get("type").toString().equals("Integer")){
                        IntegerExperiment experiment = document.toObject(IntegerExperiment.class);
                        ownedExperiments.add(experiment);
                    }

                }
                if(ownedExperiments.size() > 0){
                    callback.onSuccess(ownedExperiments);
                }else{
                    callback.onFailure();
                }
            }
        });
    }// end getExperimentsByOwner

    /**
     * This method returns a list of experiments that are subscribed to by a given user
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     * @param subscriber
     *  This is the user that is subscribed to the experiments
     */
    public void getExperimentsBySubscriber (User subscriber, QueryExperimentsCallback callback) {

        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        String subscriberID = subscriber.getId();
        ArrayList<Experiment> subscribedExperiments = new ArrayList<Experiment>();
        userCollectionReference.document(subscriberID).collection("SubscribedExperiments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(document.get("type").toString().equals("Binomial")){
                        BinomialExperiment experiment = document.toObject(BinomialExperiment.class);
                        subscribedExperiments.add(experiment);
                    }else if(document.get("type").toString().equals("Count")){
                        CountExperiment experiment = document.toObject(CountExperiment.class);
                        subscribedExperiments.add(experiment);
                    }else if(document.get("type").toString().equals("Measurement")){
                        MeasurementExperiment experiment = document.toObject(MeasurementExperiment.class);
                        subscribedExperiments.add(experiment);
                    }else if(document.get("type").toString().equals("Integer")){
                        IntegerExperiment experiment = document.toObject(IntegerExperiment.class);
                        subscribedExperiments.add(experiment);
                    }

                }
                if(subscribedExperiments.size() > 0){
                    callback.onSuccess(subscribedExperiments);

                }else{
                    callback.onFailure();
                }
            }
        });
    }// end getExperimentsBySubscriber

    /**
     * This method returns a list of experiments where given keyword matches description
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     * @param keyWord
     *  This is the search term
     */
    public void getExperimentsByDescription (String keyWord, QueryExperimentsCallback callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference allExperimentsCollectionReference = db.collection("AllExperiments");
        ArrayList<String> keyWords = new ArrayList<String>();
        keyWords.add(keyWord);

        allExperimentsCollectionReference.whereIn("description",keyWords).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Experiment> matchingExperiments = new ArrayList<Experiment>();
                for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                    if(document.get("type").toString().equals("Binomial")){
                        BinomialExperiment experiment = document.toObject(BinomialExperiment.class);
                        matchingExperiments.add(experiment);
                    }else if(document.get("type").toString().equals("Count")){
                        CountExperiment experiment = document.toObject(CountExperiment.class);
                        matchingExperiments.add(experiment);
                    }else if(document.get("type").toString().equals("Measurement")){
                        MeasurementExperiment experiment = document.toObject(MeasurementExperiment.class);
                        matchingExperiments.add(experiment);
                    }else if(document.get("type").toString().equals("Integer")){
                        IntegerExperiment experiment = document.toObject(IntegerExperiment.class);
                        matchingExperiments.add(experiment);
                    }
                }
                callback.onSuccess(matchingExperiments);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure();
            }
        });
    }// end getExperimentsBySearch

    /**
     * This method returns a list of experiments where given tags match description, owner name, owner username, or owner email, region
     * and unit(s)
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     * @param tags
     *  This is the tags to search for
     */
    public void getExperimentsByTags (ArrayList<String> tags, QueryExperimentsCallback callback) {

        for (int i = 0; i < tags.size(); i++) {
            tags.set(i, tags.get(i).toLowerCase());
        }

        db = FirebaseFirestore.getInstance();

        Set<Experiment> resultSet = new HashSet<>();

        CollectionReference allExperimentsCollectionReference = db.collection("AllExperiments");

        for (String tag: tags) {
            allExperimentsCollectionReference.whereArrayContains("tags", tag).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    Set<Experiment> matchingExperiments = new HashSet<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.get("type").toString().equals("Binomial")) {
                            BinomialExperiment experiment = document.toObject(BinomialExperiment.class);
                            matchingExperiments.add(experiment);
                        } else if (document.get("type").toString().equals("Count")) {
                            CountExperiment experiment = document.toObject(CountExperiment.class);
                            matchingExperiments.add(experiment);
                        } else if (document.get("type").toString().equals("Measurement")) {
                            MeasurementExperiment experiment = document.toObject(MeasurementExperiment.class);
                            matchingExperiments.add(experiment);
                        } else if (document.get("type").toString().equals("Integer")) {
                            IntegerExperiment experiment = document.toObject(IntegerExperiment.class);
                            matchingExperiments.add(experiment);
                        }
                    }

                    if (resultSet.isEmpty()) {
                        resultSet.addAll(matchingExperiments);
                    } else {
                        resultSet.retainAll(matchingExperiments);
                    }

                    if (matchingExperiments.size() > 0) {
                        ArrayList<Experiment> output = new ArrayList<>();
                        output.addAll(resultSet);
                        callback.onSuccess(output);
                    } else {
                        callback.onFailure();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onFailure();
                }
            });
        }
    }// end getExperimentsByTags

    /**
     * This method updates an existing experiment in the database based on id
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     * @param experiment
     *  This is the experiment to be updated
     */
    public void updateExperiment (Experiment experiment) {

        db = FirebaseFirestore.getInstance();
        CollectionReference lookupCollectionReference = db.collection("ExperimentLookups");

        String expId = experiment.getId();

        lookupCollectionReference.document(expId).collection("Paths").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot :task.getResult().getDocuments()){
                    db.document(documentSnapshot.getData().get("Path").toString()).set(experiment);
                }
            }
        });
    }// end updateExperiment

    public void deleteExperiment (Experiment experiment) {
        db = FirebaseFirestore.getInstance();
        CollectionReference lookupCollectionReference = db.collection("ExperimentLookups");

        String expId = experiment.getId();

        lookupCollectionReference.document(expId).collection("Paths").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot :task.getResult().getDocuments()){
                    db.document(documentSnapshot.getData().get("Path").toString()).delete();
                    documentSnapshot.getReference().delete();
                }
                lookupCollectionReference.document(expId).delete();
            }
        });
    }// end removeExperiment

    /**
     * For this call back onSuccess indicates that the id does not exist in the database
     * and therefore should be added
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit and Ryan Shukla
     */
    public interface GenerateIDCallback  {
        public void onSuccess(User user);
        public void onFailure();

    }// end GenerateIDCallback

    /**
     * For this call back onSuccess indicates that at least one result was found
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit and Ryan Shukla
     */
    public interface QueryExperimentsCallback  {
        public void onSuccess(ArrayList<Experiment> experiments);
        public void onFailure();

    }// end GenerateIDCallback

    /**
     * Callback for methods that query for one user.
     * @author Ryan Shukla
     */
    public interface GetUserCallback {
        public void onSuccess(User user);
        public void onFailure();
    }

    /**
     * Callback for methods that query for one experiment
     * @author Vasu Gupta
     */
    public interface GetExperimentCallback {
        public void onSuccess(Experiment experiment);
        public void onFailure();
    }
    /**
     * Callback for when initializeInstance is finished.
     * @author Ryan Shukla
     */
    public interface InitializeDatabaseCallback {
        public void onSuccess();
        public void onFailure();
    }
}// end Database