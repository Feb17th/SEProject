package com.example.backofficer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.ViewInformationBinding;
import com.example.backofficer.model.Information;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

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
            binding.btnEditProfileViewInformation.setVisibility(View.INVISIBLE);
            takeInformation(user.getEmail());
        } else if(textReceiveFromAnotherActivity.equals("ManageAccount")){
            information = (Information) getIntent().getSerializableExtra("sendingInformation");
//            takeInformation(information.getEmailOfProject());
            displayInformation(information);
        } else if(textReceiveFromAnotherActivity.equals("ChooseFeatureEmployee")){
            FirebaseUser user = app.auth.getCurrentUser();
            binding.btnEditProfileViewInformation.setVisibility(View.INVISIBLE);
            takeInformation(user.getEmail());
        } else if(textReceiveFromAnotherActivity.equals("EditProfile")){
            information = (Information) getIntent().getSerializableExtra("sendingInformation");
//            takeInformation(information.getEmailOfProject());
            displayInformation(information);
        }
    }

    private void displayInformation(Information tempInformation){
        binding.tvFirstNameViewInformation.setText(tempInformation.getFirstName());
        binding.tvLastNameViewInformation.setText(tempInformation.getLastName());
        binding.tvJobTypeViewInformation.setText(tempInformation.getJobType());
        binding.tvEmailViewInformation.setText(tempInformation.getEmail());
        binding.tvPhoneNumberViewInformation.setText(tempInformation.getPhoneNumber());
        binding.tvIdentityCardNumberViewInformation.setText(tempInformation.getIdentityCardNumber());
    }

    private void takeInformation(String emailOfProject){
        app.dataBase
                .collection("information")
                .document(emailOfProject)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Information tempInformation = documentSnapshot.toObject(Information.class);
                        binding.tvFirstNameViewInformation.setText(tempInformation.getFirstName());
                        binding.tvLastNameViewInformation.setText(tempInformation.getLastName());
                        binding.tvJobTypeViewInformation.setText(tempInformation.getJobType());
                        binding.tvEmailViewInformation.setText(tempInformation.getEmail());
                        binding.tvPhoneNumberViewInformation.setText(tempInformation.getPhoneNumber());
                        binding.tvIdentityCardNumberViewInformation.setText(tempInformation.getIdentityCardNumber());
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
            moveToEditProfile(information);
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
        } else if(textReceiveFromAnotherActivity.equals("EditProfile")){
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

    private void moveToEditProfile(Information information){
        String text = "ViewInformation";
        Intent intent = new Intent(ViewInformation.this, EditProfile.class);
        intent.putExtra("sendingText", text);
        intent.putExtra("sendingInformation", information);
        startActivity(intent);
        finish();
    }
}
