package com.example.backofficer.activity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.RegisterBinding;
import com.example.backofficer.model.Information;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private RegisterBinding binding;
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.btnDoneRegister.setOnClickListener(this);
        binding.ibBackPressRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == binding.btnDoneRegister.getId()){
            CreateNewAccount();
        } else if(id == binding.ibBackPressRegister.getId()){
            moveToManageAccount();
        }
    }

    private void CreateNewAccount() {
        String email = binding.txtEmailOfProjectRegister.getText().toString().trim();
        String password = binding.txtPasswordRegister.getText().toString().trim();
        String rePassword = binding.txtRePasswordRegister.getText().toString().trim();

        if (!rePassword.equals(password)) {
            Toast.makeText(app, "Can't Register", Toast.LENGTH_SHORT).show();
            return;
        }

        String check = checkEmpty();
        if(check.equals("Done")){
            app.auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            onCreateSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(app, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if(check.equals("Have empty element")){
            Toast.makeText(app, "Please type enough information", Toast.LENGTH_SHORT).show();
        }
    }

    private String checkEmpty(){
        if(binding.txtEmailOfProjectRegister.getText().toString().isEmpty()
        || binding.txtFirstNameRegister.getText().toString().isEmpty()
        || binding.txtLastNameRegister.getText().toString().isEmpty()
        || binding.txtJobTypeRegister.getText().toString().isEmpty()
        || binding.txtEmailRegister.getText().toString().isEmpty()
        || binding.txtPhoneNumberRegister.getText().toString().isEmpty()
        || binding.txtIdentityCardNumberRegister.getText().toString().isEmpty()){
            return "Have empty element";
        } else return "Done";
    }

    private void onCreateSuccess() {
        FirebaseUser user = app.auth.getCurrentUser();
        Information information = new Information(
                binding.txtEmailOfProjectRegister.getText().toString().trim(),
                binding.txtFirstNameRegister.getText().toString().trim(),
                binding.txtLastNameRegister.getText().toString().trim(),
                binding.txtJobTypeRegister.getText().toString().trim(),
                binding.txtEmailRegister.getText().toString().trim(),
                binding.txtPhoneNumberRegister.getText().toString().trim(),
                binding.txtIdentityCardNumberRegister.getText().toString().trim(),
                binding.txtFirstNameRegister.getText().toString().trim() + " " + binding.txtLastNameRegister.getText().toString().trim(),
                user.getUid()
        );
        app.dataBase
                .collection("information")
                .document(user.getEmail())
                .set(information)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(app,"Register successfully!", Toast.LENGTH_SHORT).show();
                        moveToManageAccount();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(app, "Cannot register", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void moveToManageAccount() {
        Intent intent = new Intent(Register.this, ManageAccount.class);
        startActivity(intent);
        finish();
    }
}
