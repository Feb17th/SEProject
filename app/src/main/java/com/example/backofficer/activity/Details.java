package com.example.backofficer.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.R;
import com.example.backofficer.databinding.DetailsBinding;
import com.example.backofficer.model.MCP;
import com.example.backofficer.model.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class Details extends AppCompatActivity implements View.OnClickListener{
    private DetailsBinding binding;
    App app;
    Task taskFromManageTask = new Task();
    MCP mcp = new MCP();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.ibBackPressDetails.setOnClickListener(this);
        binding.btnDeleteDetails.setOnClickListener(this);
        binding.tvRouteDetails.setOnClickListener(this);
        binding.btnEditTaskDetails.setOnClickListener(this);

        String textReceiveFromAnotherActivity = getIntent().getStringExtra("sendingText");
        if(textReceiveFromAnotherActivity.equals("ManageTask")){
            taskFromManageTask = (Task) getIntent().getSerializableExtra("sendingTask");
        } else if(textReceiveFromAnotherActivity.equals("CreateTask")){
            taskFromManageTask = (Task) getIntent().getSerializableExtra("sendingTask");
        }

        displayTask(taskFromManageTask);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == binding.ibBackPressDetails.getId()){
            moveBackToManageTask();
        } else if(id == binding.btnDeleteDetails.getId()){
            openDialogDeleteTask(Gravity.CENTER, taskFromManageTask);
        } else if(id == binding.tvRouteDetails.getId()){
            takeMCPToOpenGoogleMap(taskFromManageTask.getMcp());
        } else if(id == binding.btnEditTaskDetails.getId()){
            moveToCreateTask(taskFromManageTask);
        }
    }

    private void displayTask(Task task){
        binding.tvDescriptionDetails.setText(task.getDescription());
        binding.tvTimeDetails.setText(task.getTime());
        binding.tvVehicleDetails.setText(task.getVehicle());
        binding.tvMCPDetails.setText(task.getMcp());
        binding.tvOwnerDetails.setText(task.getFullName());
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

    private void openDialogDeleteTask(int gravity, Task taskFromManageTask) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete_task);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        Button btnCancelDialogDeleteTask = dialog.findViewById(R.id.btnCancelDialogDeleteTask);
        Button btnOkDialogDeleteTask = dialog.findViewById(R.id.btnOkDialogDeleteTask);

        btnCancelDialogDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOkDialogDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(taskFromManageTask);
                moveBackToManageTask();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void deleteTask(Task taskFromManageTask){
        app.dataBase
                .collection("task")
                .document(taskFromManageTask.getDescription())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(app, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(app, "Can't delete", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void moveBackToManageTask() {
        String text = "Details";
        Intent intent = new Intent(Details.this, ManageTask.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }

    private void moveToCreateTask(Task task){
        String text = "Details";
        Intent intent = new Intent(Details.this, CreateTask.class);
        intent.putExtra("sendingText", text);
        intent.putExtra("sendingTask", task);
        startActivity(intent);
        finish();
    }
}
