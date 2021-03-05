package com.T05.krowdtrialz.util;


import android.os.HandlerThread;
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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.installations.FirebaseInstallations;


import org.apache.commons.math3.analysis.function.Exp;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;


import static android.content.ContentValues.TAG;
import static java.util.stream.Collectors.toList;


/**
 * This class handles everything to do with reading and writing to firestore database
 * @author
 *  Furmaan Sekhon and Jacques Leong-Sit
 */


public class Database {

    private FirebaseFirestore db;
    private String localID;

    /**
     * This method checks if user already exits in the database and returns a new unique id if they don't
     * or existing one if they do
     * NOT DONE (This one uses FirebaseInstallations.getInstance().getId() instead of manually generated one)
     * NOT DONE (Can generate non unique ids we don't really understand the issue caused by this method)
     * @return
     *  valid unique id
     */
//    public String generateID() {
//
//        db = FirebaseFirestore.getInstance();
//        CollectionReference userCollectionReference = db.collection("Users");
//        Task<String> getID = FirebaseInstallations.getInstance().getId(); // Gets a unique id for each installation
//
//        Query query = userCollectionReference.whereEqualTo("ID", String.valueOf(getID)); // Create a query to check if ID exists in the database already
//        Log.d("generateID id:", String.valueOf(getID));
//
//        // if ID does not exist then create a new user
//        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if(queryDocumentSnapshots.getDocuments().isEmpty()) {
//                    // create new user in the database
//                    HashMap<String, Object> newUser = new HashMap<>();
//                    newUser.put("User", new User("","","",String.valueOf(getID)));
//                    userCollectionReference
//                            .document(String.valueOf(getID))
//                            .set(newUser)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d(TAG, "Data has been added");
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.d(TAG, "Data cannot be added" + e.toString());
//
//                                }
//                            });
//                }
//
//            }
//        });
//        return (String.valueOf(getID));
//    }// end generateID

    /**
     * This method checks if user already exits in the database and returns a new unique id if they don't
     * or existing one if they do
     * NOT DONE (This version is closer to complete than above version)
     * @return
     *  valid unique id
     */
    public void verifyID(String localID, GenerateIDCallback callback) {

        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        Log.d("generateID id:", localID);

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
     * ISSUE: if an experiment is added that is identical in all its fields to an existing experiment then there is a chance that
     * id is overwritten in one and remains null in the other (likely not a problem for our uses)
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    public void addExperiment (Experiment experiment) {

        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        CollectionReference lookupCollectionReference = db.collection("ExperimentLookups");

        String id = experiment.getOwner().getId();

        HashMap<String, Object> newExperiment = new HashMap<>();

        if (experiment.getClass() == MeasurementExperiment.class) { newExperiment.put("Type", "Measurement"); }
        else if (experiment.getClass() == BinomialExperiment.class) { newExperiment.put("Type", "Binomial"); }
        else if (experiment.getClass() == IntegerExperiment.class) { newExperiment.put("Type", "Integer"); }
        else if (experiment.getClass() == CountExperiment.class) { newExperiment.put("Type", "Count"); }

        // add experiment to OwnedExperiment Collection
        newExperiment.put("Experiment", experiment);
        userCollectionReference.document(id).collection("OwnedExperiments").add(newExperiment);

        Query query = userCollectionReference.document(id).collection("OwnedExperiments").whereEqualTo("Experiment", experiment);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                // update lookups
                String expId = queryDocumentSnapshots.getDocuments().get(0).getId();
                HashMap<String, Object> newLookup = new HashMap<>();
                newLookup.put("Path", String.format("Users/%s/OwnedExperiments/%s",id,expId ));
                lookupCollectionReference.document(expId).collection("Paths").add(newLookup);

                // add id to OwnedExperiment
                experiment.setId(expId);
                userCollectionReference.document(id).collection("OwnedExperiments").document(expId).update("Experiment.id",expId);
            }
        });

    }// addExperiment

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
                        Log.d("generateID", "Made IT");
                        pathList.add(String.valueOf(document.get("Path")));
                    }
                    Log.d("generateID", pathList.toString());

                    // update each path
                    for (int i = 0; i < pathList.size(); i++) {
                        HashMap<String, Object> updatedExperiment = new HashMap<>();
                        updatedExperiment.put("Experiment", experiment);
                        db.document(pathList.get(i)).set(updatedExperiment);
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
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
                    // Find the type of the experiment
                    String type = String.valueOf(document.getData());
                    int startIndex = type.indexOf("Type=");
                    type = type.substring(startIndex+5);
                    int endIndex = type.indexOf(",");
                    type = type.substring(0,endIndex);

                    // Depending on the type add the corresponding type of experiment to the ArrayList
                    if (type.equals("Measurement")){
                        MeasurementExperiment experiment = document.toObject(MeasurementExperiment.class);
                        ownedExperiments.add(experiment);
                    }else if (type.equals("Binomial")){
                        BinomialExperiment experiment = document.toObject(BinomialExperiment.class);
                        ownedExperiments.add(experiment);
                    }else if (type.equals("Count")){
                        CountExperiment experiment = document.toObject(CountExperiment.class);
                        ownedExperiments.add(experiment);
                    }else if (type.equals("Integer")){
                        IntegerExperiment experiment = document.toObject(IntegerExperiment.class);
                        ownedExperiments.add(experiment);
                    }else{
                        ownedExperiments.add(null);
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
        String ownerID = subscriber.getId();
        ArrayList<Experiment> ownedExperiments = new ArrayList<Experiment>();
        userCollectionReference.document(ownerID).collection("SubscribedExperiments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Find the type of the experiment
                    String type = String.valueOf(document.getData());
                    int startIndex = type.indexOf("Type=");
                    type = type.substring(startIndex+5);
                    int endIndex = type.indexOf(",");
                    type = type.substring(0,endIndex);

                    // Depending on the type add the corresponding type of experiment to the ArrayList
                    if (type.equals("Measurement")){
                        MeasurementExperiment experiment = document.toObject(MeasurementExperiment.class);
                        ownedExperiments.add(experiment);
                    }else if (type.equals("Binomial")){
                        BinomialExperiment experiment = document.toObject(BinomialExperiment.class);
                        ownedExperiments.add(experiment);
                    }else if (type.equals("Count")){
                        CountExperiment experiment = document.toObject(CountExperiment.class);
                        ownedExperiments.add(experiment);
                    }else if (type.equals("Integer")){
                        IntegerExperiment experiment = document.toObject(IntegerExperiment.class);
                        ownedExperiments.add(experiment);
                    }else{
                        ownedExperiments.add(null);
                    }
                }
                if(ownedExperiments.size() > 0){
                    callback.onSuccess(ownedExperiments);
                }else{
                    callback.onFailure();
                }
            }
        });
    }// end getExperimentsBySubscriber

    private void checkRegions(DocumentSnapshot userDocumentSnapshot,ArrayList<Experiment> descriptionMatches, QueryExperimentsCallback callback)  {
        userDocumentSnapshot.getReference().collection("OwnedExperiments").whereIn("Experiment.region", keyWords).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()){
                    callback.onFailure();
                }else{
                    List<DocumentSnapshot> experimentDocumentSnapshots = task.getResult().getDocuments();
                    for (DocumentSnapshot experimentDocumentSnapshot: experimentDocumentSnapshots){
                        // Find the type of the experiment
                        String type = String.valueOf(experimentDocumentSnapshot.getData());
                        int startIndex = type.indexOf("Type=");
                        type = type.substring(startIndex+5);
                        int endIndex = type.indexOf(",");
                        type = type.substring(0,endIndex);

                        // Depending on the type add the corresponding type of experiment to the ArrayList
                        if (type.equals("Measurement")){
                            MeasurementExperiment experiment = experimentDocumentSnapshot.toObject(MeasurementExperiment.class);
                            descriptionMatches.add(experiment);
                        }else if (type.equals("Binomial")){
                            BinomialExperiment experiment = experimentDocumentSnapshot.toObject(BinomialExperiment.class);
                            descriptionMatches.add(experiment);
                        }else if (type.equals("Count")){
                            CountExperiment experiment = experimentDocumentSnapshot.toObject(CountExperiment.class);
                            descriptionMatches.add(experiment);
                        }else if (type.equals("Integer")){
                            IntegerExperiment experiment = experimentDocumentSnapshot.toObject(IntegerExperiment.class);
                            descriptionMatches.add(experiment);
                        }else{
                            descriptionMatches.add(null);
                        }
                    }
                }
            }
        });
    }

    private void checkDescriptions (Task<QuerySnapshot> task,DocumentSnapshot userDocumentSnapshot,ArrayList<Experiment> descriptionMatches, QueryExperimentsCallback callback) {
        if (task.getResult().isEmpty()) {

            checkRegions(userDocumentSnapshot, descriptionMatches, new QueryExperimentsCallback() {
                @Override
                public void onSuccess(ArrayList<Experiment> experiments) {
                    callback.onSuccess(descriptionMatches);
                }

                @Override
                public void onFailure() {
                    callback.onFailure();
                }
            });

        }else{

            List<DocumentSnapshot> experimentDocumentSnapshots = task.getResult().getDocuments();
            for (DocumentSnapshot experimentDocumentSnapshot: experimentDocumentSnapshots){
                // Find the type of the experiment
                String type = String.valueOf(experimentDocumentSnapshot.getData());
                int startIndex = type.indexOf("Type=");
                type = type.substring(startIndex+5);
                int endIndex = type.indexOf(",");
                type = type.substring(0,endIndex);

                Log.d("test",type);

                // Depending on the type add the corresponding type of experiment to the ArrayList
                if (type.equals("Measurement")){
                    MeasurementExperiment experiment = experimentDocumentSnapshot.toObject(MeasurementExperiment.class);
                    descriptionMatches.add(experiment);
                }else if (type.equals("Binomial")){
                    BinomialExperiment experiment = experimentDocumentSnapshot.toObject(BinomialExperiment.class);
                    Log.d("test","BIn");
                    descriptionMatches.add(experiment);
                }else if (type.equals("Count")){
                    CountExperiment experiment = experimentDocumentSnapshot.toObject(CountExperiment.class);
                    descriptionMatches.add(experiment);
                }else if (type.equals("Integer")){
                    IntegerExperiment experiment = experimentDocumentSnapshot.toObject(IntegerExperiment.class);
                    descriptionMatches.add(experiment);
                }else{
                    descriptionMatches.add(null);
                }
            }
        }
    }

    /**
     * This method returns a list of experiments where given keyword matches description, region, or owner username
     * NOT DONE
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     * @param keyWord
     *  This is the search term
     */
    public void getExperimentsBySearch (String keyWord, QueryExperimentsCallback callback) {

        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        ArrayList<Experiment> matchingExperiments = new ArrayList<Experiment>();
        ArrayList<String> keyWords = new ArrayList<String>();
        keyWords.add(keyWord);
        userCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<DocumentSnapshot> userDocumentSnapshots = task.getResult().getDocuments();

                for (DocumentSnapshot userDocumentSnapshot: userDocumentSnapshots){

                    userDocumentSnapshot.getReference().collection("OwnedExperiments").whereIn("Experiment.description", keyWords).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            checkDescriptions(task, userDocumentSnapshot, matchingExperiments, new QueryExperimentsCallback() {
                                @Override
                                public void onSuccess(ArrayList<Experiment> experiments) {
                                    callback.onSuccess(matchingExperiments);
                                }

                                @Override
                                public void onFailure() {

                                }
                            });
                        }
                    });
                }
                Log.d("test",matchingExperiments.toString());
                callback.onSuccess(matchingExperiments);
            }
        });

    }// end getExperimentsBySearch

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
        String expId = experiment.getId();

        Log.d("addSubscription", String.valueOf(expId));


        HashMap<String, Object> newExperiment = new HashMap<>();
        newExperiment.put("Experiment", experiment);
        if (experiment.getClass() == MeasurementExperiment.class) { newExperiment.put("Type", "Measurement"); }
        else if (experiment.getClass() == BinomialExperiment.class) { newExperiment.put("Type", "Binomial"); }
        else if (experiment.getClass() == IntegerExperiment.class) { newExperiment.put("Type", "Integer"); }
        else if (experiment.getClass() == CountExperiment.class) { newExperiment.put("Type", "Count"); }


        userCollectionReference.document(id).collection("SubscribedExperiments").document(String.valueOf(expId)).set(newExperiment);
        Query query = userCollectionReference.document(id).collection("SubscribedExperiments").whereEqualTo("Experiment", experiment);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                HashMap<String, Object> newLookup = new HashMap<>();
                newLookup.put("Path", String.format("Users/%s/SubscribedExperiments/%s",id,expId ));
                lookupCollectionReference.document(expId).collection("Paths").add(newLookup);
            }
        });

    }// end addSubscription

    /**
     * This method updates an existing experiment in the database based on id
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     * @param experiment
     *  This is the experiment to be updated
     */
    public void updateExperiment (Experiment experiment) {

        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        CollectionReference lookupCollectionReference = db.collection("ExperimentLookups");

        String id = experiment.getOwner().getId();
        String expId = experiment.getId();

        HashMap<String, Object> updatedExperiment = new HashMap<>();
        updatedExperiment.put("Experiment", experiment);

        lookupCollectionReference.document(expId).collection("Paths").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                for (DocumentSnapshot doc:task.getResult().getDocuments()){
                    Log.d("updateExperiment",doc.getData().get("Path").toString());
                    db.document(doc.getData().get("Path").toString()).set(experiment);
                }

            }
        });



    }// end updateExperiment


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

    public interface DocumentSnapshotCallback {
        public void onSuccess(DocumentSnapshot documentSnapshot);
        public void onFailure();
    }

}// end Database

