package com.example.backofficer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.DetailsEmployeeBinding;
import com.example.backofficer.model.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class DetailsEmployee extends AppCompatActivity implements View.OnClickListener{
    private DetailsEmployeeBinding binding;
    Task task = new Task();
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailsEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.ibBackPressDetailsEmployee.setOnClickListener(this);

        Task taskFromManageTask = (Task) getIntent().getSerializableExtra("sendingTask");
        takeTask(taskFromManageTask.getDescription());
    }

    private void takeTask(String description) {
        app.dataBase
                .collection("task")
                .document(description)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        task = documentSnapshot.toObject(Task.class);
                        binding.tvTimeDetailsEmployee.setText(task.getTime());
                        binding.tvVehicleDetailsEmployee.setText(task.getVehicle());
                        binding.tvMCPDetailsEmployee.setText(task.getMcp());
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

        if(id == binding.ibBackPressDetailsEmployee.getId()){
            moveToManageTask();
        }
    }

    private void moveToManageTask(){
        String text = "DetailsEmployee";
        Intent intent = new Intent(DetailsEmployee.this, ManageTask.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }
}
