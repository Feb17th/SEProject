package com.example.backofficer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.EditProfileBinding;
import com.example.backofficer.model.Information;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements View.OnClickListener{
    private EditProfileBinding binding;
    App app;
    Information information = new Information();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.btnUpdateEditProfile.setOnClickListener(this);
        binding.ibBackPressEditProfile.setOnClickListener(this);

        String textReceiveFromAnotherActivity = getIntent().getStringExtra("sendingText");
        if(textReceiveFromAnotherActivity.equals("ViewInformation")){
            information = (Information) getIntent().getSerializableExtra("sendingInformation");
            binding.txtFirstNameEditProfile.setText(information.getFirstName());
            binding.txtLastNameEditProfile.setText(information.getLastName());
            binding.txtJobTypeEditProfile.setText(information.getJobType());
            binding.txtEmailEditProfile.setText(information.getEmail());
            binding.txtPhoneNumberEditProfile.setText(information.getPhoneNumber());
            binding.txtIdentityCardNumberEditProfile.setText(information.getIdentityCardNumber());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == binding.btnUpdateEditProfile.getId()){
            updateProfileToFirebase(information);
        } else if(id == binding.ibBackPressEditProfile.getId()){
            moveToViewInformation();
        }
    }

    private void updateProfileToFirebase(Information information) {
        Map<String, Object> informationMap = new HashMap<>();
        informationMap.put("firstName", binding.txtFirstNameEditProfile.getText().toString().trim());
        informationMap.put("lastName", binding.txtLastNameEditProfile.getText().toString().trim());
        informationMap.put("jobType", binding.txtJobTypeEditProfile.getText().toString().trim());
        informationMap.put("email", binding.txtEmailEditProfile.getText().toString().trim());
        informationMap.put("phoneNumber", binding.txtPhoneNumberEditProfile.getText().toString().trim());
        informationMap.put("identityCardNumber", binding.txtIdentityCardNumberEditProfile.getText().toString().trim());
        informationMap.put("fullName", binding.txtFirstNameEditProfile.getText().toString().trim() + " " + binding.txtLastNameEditProfile.getText().toString().trim());
        app.dataBase
                .collection("information")
                .document(information.getEmailOfProject())
                .update(informationMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(app, "Update profile successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(app, "Have some wrong during updating profile!", Toast.LENGTH_SHORT).show();
                    }
                });
        updateInformationBeforeMoveToViewInformation(information);
    }

    private void updateInformationBeforeMoveToViewInformation(Information information){
        information.setFirstName(binding.txtFirstNameEditProfile.getText().toString().trim());
        information.setLastName(binding.txtLastNameEditProfile.getText().toString().trim());
        information.setJobType(binding.txtJobTypeEditProfile.getText().toString().trim());
        information.setEmail(binding.txtEmailEditProfile.getText().toString().trim());
        information.setPhoneNumber(binding.txtPhoneNumberEditProfile.getText().toString().trim());
        information.setIdentityCardNumber(binding.txtIdentityCardNumberEditProfile.getText().toString().trim());
        information.setFullName(binding.txtFirstNameEditProfile.getText().toString().trim() + " " + binding.txtLastNameEditProfile.getText().toString().trim());
    }

    private void moveToViewInformation() {
        String text = "EditProfile";
        Intent intent = new Intent(EditProfile.this, ViewInformation.class);
        intent.putExtra("sendingText", text);
        intent.putExtra("sendingInformation", information);
        startActivity(intent);
        finish();
    }
}
