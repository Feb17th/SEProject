package com.example.backofficer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.DetailsEmployeeBinding;
import com.example.backofficer.model.MCP;
import com.example.backofficer.model.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class DetailsEmployee extends AppCompatActivity implements View.OnClickListener{
    private DetailsEmployeeBinding binding;
    Task taskFromManageTask = new Task();
    App app;
    MCP mcp = new MCP();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailsEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.ibBackPressDetailsEmployee.setOnClickListener(this);
        binding.tvRouteDetailsEmployee.setOnClickListener(this);
        binding.btnDoneDetailsEmployee.setOnClickListener(this);
        binding.btnWorkingDetailsEmployee.setOnClickListener(this);

        taskFromManageTask = (Task) getIntent().getSerializableExtra("sendingTask");
        displayTask(taskFromManageTask);
    }

    private void displayTask(Task task){
        binding.tvTimeDetailsEmployee.setText(task.getTime());
        binding.tvVehicleDetailsEmployee.setText(task.getVehicle());
        binding.tvMCPDetailsEmployee.setText(task.getMcp());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == binding.ibBackPressDetailsEmployee.getId()){
            moveToManageTask();
        } else if(id == binding.tvRouteDetailsEmployee.getId()){
            takeMCPToOpenGoogleMap(taskFromManageTask.getMcp());
        } else if(id == binding.btnDoneDetailsEmployee.getId()){
            updateStatus(taskFromManageTask, "Done");
        } else if(id == binding.btnWorkingDetailsEmployee.getId()){
            updateStatus(taskFromManageTask, "Working");
        }
    }

    private void takeMCPToOpenGoogleMap(String getMCP){
        app.dataBase
                .collection("mcp")
                .document(getMCP)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        mcp = documentSnapshot.toObject(MCP.class);
                        openGoogleMap(String.valueOf(mcp.getGeoPoint().getLatitude()),
                                String.valueOf(mcp.getGeoPoint().getLongitude()),
                                mcp.getName());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(app, "Can't load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openGoogleMap(String latitude, String longitude, String name) {
        Uri uri = Uri.parse("geo:" +
                latitude + ", " +
                longitude + "?q=" +
                name);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private void moveToManageTask(){
        String text = "DetailsEmployee";
        Intent intent = new Intent(DetailsEmployee.this, ManageTask.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }

    private void updateStatus(Task task, String status){
        Map<String, Object> taskStatus = new HashMap<>();
        taskStatus.put("status", status);
        app.dataBase
                .collection("task")
                .document(task.getDescription())
                .update(taskStatus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(status == "Done"){
                            Toast.makeText(app, "Good Job!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(app, "You should finish this task soon", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(app, "Have error system!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
