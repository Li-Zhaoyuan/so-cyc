package com.dubailizards.so_cyc.control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dubailizards.so_cyc.entity.EventDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 *  A Singleton, control class for the Database.
 *  Description: Manages the communication between the application and the Google Firebase Firestore
 */
public final class DatabaseManager {
    /**
     * private static final String variable.
     * Holds the TAG value for debug purposes.
     */
    private static final String TAG = "DatabaseManager";

    /**
     * a private static final DatabaseManager variable.
     * Holds current application's reference to the Singleton instance of this class.
     */
    private static final DatabaseManager dm = new DatabaseManager();

    /**
     * a private final FirebaseFirestore variable.
     * Holds current application's reference to the Firebase Firestore.
     */
    private final FirebaseFirestore db;

    /**
     * a private Map<String, Object> variable.
     * Holds the last fetched data from the Firestore.
     */
    private Map<String, Object> dataFromFireStore;

    /**
     * A constructor.
     * Creates the instance of this class.
     */
    private DatabaseManager()
    {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * A getter.
     * Return the instance of the Database Manager.
     */
    public static DatabaseManager GetInstance()
    {
        return dm;
    }

    /**
     * A getter.
     * Return the instance of the FirebaseFirestore.
     */
    public FirebaseFirestore GetFireStore()
    {
        return db;
    }


    /**
     * An add request function. Take note that it runs Asynchronously
     * Adds data in the form of a Map<String, Object> to the Firestore
     * @param collectionName is the collection that holds the document that will hold the data
     * @param documentName is the document that holds all the map data
     * @param doc is the map data that you want to pass into the database
     */
    public void AddData(String collectionName, String documentName,Map<String, Object> doc)
    {
        // Add a new document with a generated ID
        db.collection(collectionName).document(documentName)
                .set(doc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    /**
     * An add request function. Take note that it runs Asynchronously
     * Adds data in the form of a Map<String, Object> to the Firestore
     * @param collectionName is the collection that holds the document that will hold the data
     * @param documentName is the document that holds all the map data
     * @param ed is the EventDetail class entity that you want to pass into the database
     */
    public void AddData(String collectionName, String documentName, EventDetails ed)
    {
        // Add a new document with a generated ID
        db.collection(collectionName).document(documentName)
                .set(ed)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }



    /**
     * A get request function.
     * Gets/Query map data from the Firestore. Take note that it runs Asynchronously.
     * This will pass the fetched data to the private variable dataFromFireStore.
     * Deprecated, only use this function as a reference
     * @param collectionName is the collection that holds the document that will hold the data
     * @param documentName is the document that holds all the map data
     */
    public void GetData(String collectionName, String documentName)
    {
        DocumentReference docRef = db.collection(collectionName).document(documentName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        dataFromFireStore = (Map<String, Object>)document.getData();
                        DebugPrintMap(dataFromFireStore);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //return db.collection(collectionName).document(documentName).get().getResult().getData();
    }

    /**
     * A get request function.
     * Gets/Query map data from the Firestore. Take note that it runs Asynchronously.
     * Deprecated, only use this function as a reference
     * @param documentName is the unique key that points to the EventDetails object data
     */
    public void GetEventDetailsData(String documentName)
    {
        String collectionName = "EventDetails";
        DocumentReference docRef = db.collection(collectionName).document(documentName);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                EventDetails ed = documentSnapshot.toObject(EventDetails.class);
                ed.PrintAll();
            }
        });
    }


    /**
     * A debug function.
     * prints the key and value of the Map<String, Object>
     * @param map the Map<String, Object> variable that you want to print out.
     */
    public void DebugPrintMap(Map<String, Object> map)
    {
        if(map == null)
            return;
        for (Map.Entry<String, Object> element : map.entrySet()) {
            System.out.println(element.getKey() + ":" + element.getValue().toString());
        }
    }

    /**
     * A delete request function.
     * deletes a specific document from a specific collection
     * note: to fully delete a collection you need to go online and do it.
     * @param collectionName is the collection that holds the document that will hold the data
     * @param documentName is the document that holds all the map data
     */
    public void DeleteData(String collectionName, String documentName)
    {
        db.collection(collectionName).document(documentName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
}
