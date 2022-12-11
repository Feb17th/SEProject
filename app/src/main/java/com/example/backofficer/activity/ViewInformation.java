package com.example.backofficer.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.ViewInformationBinding;
import com.example.backofficer.model.Information;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class ViewInformation extends AppCompatActivity implements View.OnClickListener{
    private ViewInformationBinding binding;
    Information information = new Information();
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.btnEditProfileViewInformation.setOnClickListener(this);
        binding.ibBackPressViewInformation.setOnClickListener(this);

        String textReceiveFromAnotherActivity = getIntent().getStringExtra("sendingText");
        if(textReceiveFromAnotherActivity.equals("ChooseFeatureBackOfficer")){
            FirebaseUser user = app.auth.getCurrentUser();
            takeInformation(user.getEmail());
        } else if(textReceiveFromAnotherActivity.equals("ManageAccount")){
            Information informationFromManageInformation = (Information) getIntent().getSerializableExtra("sendingInformation");
            takeInformation(informationFromManageInformation.getEmailOfProject());
        } else if(textReceiveFromAnotherActivity.equals("ChooseFeatureEmployee")){
            FirebaseUser user = app.auth.getCurrentUser();
            takeInformation(user.getEmail());
        }
    }

    private void takeInformation(String emailOfProject){
        app.dataBase
                .collection("information")
                .document(emailOfProject)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        information = documentSnapshot.toObject(Information.class);
                        binding.tvFirstNameViewInformation.setText(information.getFirstName());
                        binding.tvLastNameViewInformation.setText(information.getLastName());
                        binding.tvJobTypeViewInformation.setText(information.getJobType());
                        binding.tvEmailViewInformation.setText(information.getEmail());
                        binding.tvPhoneNumberViewInformation.setText(information.getPhoneNumber());
                        binding.tvIdentityCardNumberViewInformation.setText(information.getIdentityCardNumber());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(app, "Can't Load Information", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == binding.btnEditProfileViewInformation.getId()){

        } else if(id == binding.ibBackPressViewInformation.getId()){
            moveWithBackButton();
        }
    }

    private void moveWithBackButton(){
        String textReceiveFromAnotherActivity = getIntent().getStringExtra("sendingText");
        if(textReceiveFromAnotherActivity.equals("ChooseFeatureBackOfficer")){
            Intent intent = new Intent(ViewInformation.this, ChooseFeatureBackOfficer.class);
            startActivity(intent);
            finish();
        } else if(textReceiveFromAnotherActivity.equals("ManageAccount")){
            Intent intent = new Intent(ViewInformation.this, ManageAccount.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(ViewInformation.this, ChooseFeatureEmployee.class);
            startActivity(intent);
            finish();
        }
    }

    private void updateProfile(String emailOfProject){
        Information information;
        DocumentReference documentReference = app.dataBase.collection("information").document(emailOfProject);
        app.dataBase.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(documentReference);

                transaction.update(documentReference, "email", "abc");

                return null;
            }
        });
    }
}
