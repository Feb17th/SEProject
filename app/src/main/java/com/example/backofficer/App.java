package com.example.backofficer;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class App extends Application {
    public FirebaseAuth auth;
    public FirebaseFirestore dataBase;
    public FirebaseStorage storage;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        dataBase = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }
}
