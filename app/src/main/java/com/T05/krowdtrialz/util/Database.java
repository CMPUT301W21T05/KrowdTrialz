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
import java.util.List;
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

    // The unique id associated with this device.
    // This is generated the first time the app is opened.
    private String localID;

    // The single instance for the singleton pattern
    private static Database instance = null;

    /**
     * Initializes the single instance.
     * This must be called before getInstance() is called.
     * This should be called from MainActivity when the app starts.
     * @author Ryan Shukla
     * @param sharedPreferences The app's instance of SharedPreferences.
     */
    public static void initializeInstance(SharedPreferences sharedPreferences) {
        if (instance == null) {
            instance = new Database(sharedPreferences);
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
     * This saves unique id after loadID generates a unique id
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    private void saveID() {

        // allows variable to be saved
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String convertedID = gson.toJson(localID); // converts arrayList to json
        editor.putString("ID",convertedID); // saves converted arrayList
        editor.apply();
        Log.d("saveID","Saved"+localID);

    }// end saveID

    /**
     * This checks if the device already has an id saved if not it generates unique id and saves it to device
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    public void loadID() {

        SharedPreferences sharedP = sharedPreferences;
        Gson gson = new Gson();
        String convertedID = sharedP.getString("ID", null);// gets saved arrayList (in json form) null if there is none saved
        Type type = new TypeToken<String>() {}.getType();
        localID = gson.fromJson(convertedID, type);


        if (localID == null) {
            UUID uuid = UUID.randomUUID();
            localID = uuid.toString();
            verifyID(localID.toString(),new Database.GenerateIDCallback() {
                @Override
                public void onSuccess(String id) {
                    saveID();
                }

                @Override
                public void onFailure() {
                    loadID();
                }
            });
        }
    }// end loadID

    /**
     * Gets the unique ID associated with this user's device.
     * In order for this to return the correct ID, the app should have called loadID first and
     * allowed ample time to confirm the ID with the database.
     * @return The ID.
     */
    public String getID() {
        return localID;
    }

    /**
     * This method checks if user already exits in the database and returns a new unique id if they don't
     * or existing one if they do
     * @return
     *  valid unique id
     */
    public void verifyID(String localID, GenerateIDCallback callback) {

        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");

        Query query = userCollectionReference.whereEqualTo("User.id", localID); // Create a query to check if ID exists in the database already

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().isEmpty()) {
                    // create new user in the database
                    // This means the id generated is unique
                    HashMap<String, Object> newUser = new HashMap<>();
                    newUser.put("User", new User("","","", localID));
                    userCollectionReference
                            .document(localID)
                            .set(newUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Data has been added");
                                    callback.onSuccess(localID);
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
        public void onSuccess(String id);
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

}// end Database