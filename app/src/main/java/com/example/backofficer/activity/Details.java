package com.example.backofficer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.DetailsBinding;
import com.example.backofficer.model.Information;
import com.example.backofficer.model.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class Details extends AppCompatActivity implements View.OnClickListener{
    private DetailsBinding binding;
    Task task = new Task();
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.ibBackPressDetails.setOnClickListener(this);

        Task taskFromManageTask = (Task) getIntent().getSerializableExtra("sendingTask");
        takeTask(taskFromManageTask.getDescription());
    }

    private void takeTask(String description){
        app.dataBase
                .collection("task")
                .document(description)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        task = documentSnapshot.toObject(Task.class);
                        binding.tvTimeDetails.setText(task.getTime());
                        binding.tvVehicleDetails.setText(task.getVehicle());
                        binding.tvMCPDetails.setText(task.getMcp());
                        binding.tvOwnerDetails.setText(task.getFullName());
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

        if(id == binding.ibBackPressDetails.getId()){
            moveBackToManageTask();
        }
    }

    private void moveBackToManageTask() {
        String text = "Details";
        Intent intent = new Intent(Details.this, ManageTask.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }
}
