package com.T05.krowdtrialz.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.CountExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.experiment.IntegerExperiment;
import com.T05.krowdtrialz.model.experiment.MeasurementExperiment;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.installations.FirebaseInstallations;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * This class handles everything to do with reading and writing to firestore database
 * @author
 *  Furmaan Sekhon and Jacques Leong-Sit
 */


public class Database {

    private FirebaseFirestore db;

    /**
     * This method checks if installation ID is already in database, if so return it otherwise add to the database and return it
     * @author
     *  Furmaan Sekhon and Jacques Leong-Sit
     * @return
     *  This is the installation ID
     */
    public String generateID() {
        db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = db.collection("Users");
        Task<String> getID = FirebaseInstallations.getInstance().getId(); // Gets a unique id for each installation

        Query query = userCollectionReference.whereEqualTo("ID", String.valueOf(getID)); // Create a query to check if ID exists in the database already
        Log.d("generateID id:", String.valueOf(getID));

        // if ID does not exist then create a new user
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().isEmpty()) {
                    // create new user in the database
                    HashMap<String, Object> newUser = new HashMap<>();
                    newUser.put("User", new User("","","",String.valueOf(getID)));
                    userCollectionReference
                            .document(String.valueOf(getID))
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
                }

            }
        });
        return (String.valueOf(getID));
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
        newExperiment.put("Experiment", experiment);

        userCollectionReference.document(id).collection("OwnedExperiments").add(newExperiment);
        Query query = userCollectionReference.document(id).collection("OwnedExperiments").whereEqualTo("Experiment", experiment);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String expId = queryDocumentSnapshots.getDocuments().get(0).getId();
                HashMap<String, Object> newLookup = new HashMap<>();
                newLookup.put("Path", String.format("Users/%s/OwnedExperiments/%s",id,expId ));
                lookupCollectionReference.document(expId).collection("Paths").add(newLookup);
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
        CollectionReference userCollectionReference = db.collection("Users");

//        Query query = userCollectionReference.whereEqualTo("", String.valueOf(getID)); // Create a query to check if ID exists in the database already


    }


}// end Database
