package com.example.backofficer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.ChooseFeatureEmployeeBinding;

public class ChooseFeatureEmployee extends AppCompatActivity implements View.OnClickListener{
    private ChooseFeatureEmployeeBinding binding;
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChooseFeatureEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.btnViewInformationChooseFeatureEmployee.setOnClickListener(this);
        binding.btnViewTaskChooseFeatureEmployee.setOnClickListener(this);
        binding.btnSignOutChooseFeatureEmployee.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == binding.btnViewInformationChooseFeatureEmployee.getId()){
            moveToViewInformation();
        } else if(id == binding.btnViewTaskChooseFeatureEmployee.getId()){
            moveToManageTask();
        } else if(id == binding.btnSignOutChooseFeatureEmployee.getId()){
            signOut();
        }
    }

    private void signOut() {
        app.auth.signOut();
        Intent intent = new Intent(ChooseFeatureEmployee.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void moveToViewInformation(){
        String text = "ChooseFeatureEmployee";
        Intent intent = new Intent(ChooseFeatureEmployee.this, ViewInformation.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }

    private void moveToManageTask() {
        String text = "ChooseFeatureEmployee";
        Intent intent = new Intent(ChooseFeatureEmployee.this, ManageTask.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }
}
