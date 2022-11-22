package com.example.backofficer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.CreateTaskBinding;
import com.example.backofficer.model.Information;
import com.example.backofficer.model.Vehicle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CreateTask extends AppCompatActivity implements View.OnClickListener{
    private CreateTaskBinding binding;
    ArrayList<Information> informationArrayList;
    ArrayList<Vehicle> vehicleArrayList;
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.btnDoneCreateTask.setOnClickListener(this);
        binding.ibBackPressCreateTask.setOnClickListener(this);

        getInformation();
        getVehicle();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == binding.btnDoneCreateTask.getId()){
            onCreateSuccess();
        } else if(id == binding.ibBackPressCreateTask.getId()){
            moveToManageTask();
        }
    }

    private void moveToManageTask() {
        String text = "CreateTask";
        Intent intent = new Intent(CreateTask.this, ManageTask.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }

    private void getInformation(){
        app.dataBase
                .collection("information")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                informationArrayList.add(documentSnapshot.toObject(Information.class));
                            }
                        } else {
                            Toast.makeText(app, "Fail Loading Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getVehicle(){
        app.dataBase
                .collection("vehicle")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                vehicleArrayList.add(documentSnapshot.toObject(Vehicle.class));
                            }
                        } else {
                            Toast.makeText(app, "Fail Loading Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onCreateSuccess() {
    }
}
