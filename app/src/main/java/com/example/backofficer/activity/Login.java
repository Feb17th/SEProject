package com.example.backofficer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.LoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private LoginBinding binding;
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignIn.setOnClickListener(this);

        app = (App) getApplication();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == binding.btnSignIn.getId()){
            SignIn();
        }
    }

    private void moveToChooseFeatureBO(){
        Intent intent = new Intent(Login.this, ChooseFeatureBackOfficer.class);
        startActivity(intent);
        finish();
    }

    private void moveToChooseFeatureEmployee(){
        Intent intent = new Intent(Login.this, ChooseFeatureEmployee.class);
        startActivity(intent);
        finish();
    }

    private void onSignInSuccess(){
        String email = binding.txtEmailLogin.getText().toString().trim();
        if(email.charAt(0) == 'b' && email.charAt(1) == 'o'){
            moveToChooseFeatureBO();
        } else {
            moveToChooseFeatureEmployee();
        }
    }

    private void SignIn() {
        String email = binding.txtEmailLogin.getText().toString().trim();
        String password = binding.txtPasswordLogin.getText().toString().trim();

        app.auth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        onSignInSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(app, "Wrong Account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
