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
    public String generateID() {

        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        Random rand = new Random();
        localID = String.valueOf(rand.nextInt(10000));
        final String[] getID = {String.valueOf(9341)};
        Log.d("generateID id:", getID[0]);

        CountDownLatch done = new CountDownLatch(1);

        Query query = userCollectionReference.whereEqualTo("User.id", getID[0]); // Create a query to check if ID exists in the database already
//        List<Thread> workers = Stream
//                .generate(() -> new Thread (new Worker(userCollectionReference, query, getID, done)))
//                .limit(1)
//                .collect(toList());


//        for (Thread thread : workers)
//        {
//            thread.start();
//        }

        Thread queryThread = new HandlerThread("queryHandler") {
            @Override
            public void run(){
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty()) {
                            // create new user in the database
                            HashMap<String, Object> newUser = new HashMap<>();
                            newUser.put("User", new User("","","", getID[0]));
                            userCollectionReference
                                    .document(getID[0])
                                    .set(newUser)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Data has been added");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Data cannot be added" + e.toString());

                                        }
                                    });
                        }else {

                            Log.d("generateID", "Should be neg");
                            getID[0] = String.valueOf(-1);

                        }
                        done.countDown();
                    }
                });

            }
        };

        queryThread.start();

        // if ID does not exist then create a new user

        Log.d("generateID", "Made it to done.await");

        try {
            done.await(); //it will wait till the response is received from firebase.
            Log.d("generateID", "Made past await");
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("generateID", "Made it to return");
        return (getID[0]);
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
                newExperiment.put("Experiment", experiment);
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
     * NOT DONE
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     * @param owner
     *  This is the user that owns the experiments
     * @return
     *  returns a list of experiments that are owned by a given user
     */
    public ArrayList<Experiment> getExperimentsByOwner (User owner) {

        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        String ownerID = owner.getId();
        ArrayList<Experiment> ownedExperiments = new ArrayList<Experiment>();
        Task task = userCollectionReference.document(ownerID).collection("OwnedExperiments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    Log.d("getExperiment", "Started For");


                    String type = String.valueOf(document.getData());

                    int startIndex = type.indexOf("Type=");

                    type = type.substring(startIndex+5);

                    int endIndex = type.indexOf(",");

                    type = type.substring(0,endIndex);
                    Log.d("getExperiment",type);

                    if (type.equals("Measurement")){
                        Log.d("getExperiment","addedMES");
                        MeasurementExperiment experiment = document.toObject(MeasurementExperiment.class);
                        ownedExperiments.add(experiment);
                    }else if (type.equals("Binomial")){
                        Log.d("getExperiment","addedBi");
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

            }

        });

        return ownedExperiments;

    }// end getExperimentsByOwner

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

    }

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



    }


    /**
     * We think this class, or something similar, may be part of a solution to async problem
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     */
    private class Worker implements Runnable {

        private CollectionReference userCollectionReference;
        private Query query;
        private String[] getID;
        private CountDownLatch countDownLatch;

        public Worker(CollectionReference userCollectionReference, Query query, String[] getID, CountDownLatch countDownLatch) {
            this.userCollectionReference = userCollectionReference;
            this.query = query;
            this.getID = getID;
            this.countDownLatch = countDownLatch;
        }

        public String[] getGetID() {
            return getID;
        }

        @Override
        public void run() {
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.getDocuments().isEmpty()) {
                        // create new user in the database
                        HashMap<String, Object> newUser = new HashMap<>();
                        newUser.put("User", new User("","","", getID[0]));
                        userCollectionReference
                                .document(getID[0])
                                .set(newUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Data has been added");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Data cannot be added" + e.toString());

                                    }
                                });
                    }else {

                        Log.d("generateID", "Should be neg");
                        getID[0] = String.valueOf(-1);
                    }
                }
            });
            countDownLatch.countDown();
        }
    }

}// end Database
