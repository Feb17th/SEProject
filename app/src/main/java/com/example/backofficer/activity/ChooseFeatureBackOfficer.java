package com.example.backofficer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.ChooseFeatureBackOfficerBinding;

public class ChooseFeatureBackOfficer extends AppCompatActivity implements View.OnClickListener{
    private ChooseFeatureBackOfficerBinding binding;
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChooseFeatureBackOfficerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.btnViewInformationChooseFeatureBackOfficer.setOnClickListener(this);
        binding.btnManageAccountChooseFeatureBackOfficer.setOnClickListener(this);
        binding.btnManageTaskChooseFeatureBackOfficer.setOnClickListener(this);
        binding.btnSignOutChooseFeatureBackOfficer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == binding.btnViewInformationChooseFeatureBackOfficer.getId()){
            moveToViewInformation();
        } else if(id == binding.btnManageAccountChooseFeatureBackOfficer.getId()){
            moveToManageAccount();
        } else if(id == binding.btnManageTaskChooseFeatureBackOfficer.getId()){
            moveToManageTask();
        } else if(id == binding.btnSignOutChooseFeatureBackOfficer.getId()){
            signOut();
        }
    }

    private void moveToViewInformation(){
        String text = "ChooseFeatureBackOfficer";
        Intent intent = new Intent(ChooseFeatureBackOfficer.this, ViewInformation.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }

    private void moveToManageAccount(){
        Intent intent = new Intent(ChooseFeatureBackOfficer.this, ManageAccount.class);
        startActivity(intent);
        finish();
    }

    private void moveToManageTask() {
        String text = "ChooseFeatureBackOfficer";
        Intent intent = new Intent(ChooseFeatureBackOfficer.this, ManageTask.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }

    private void signOut(){
        app.auth.signOut();
        Intent intent = new Intent(ChooseFeatureBackOfficer.this, Login.class);
        startActivity(intent);
        finish();
    }
}
