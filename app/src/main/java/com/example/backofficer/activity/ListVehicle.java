package com.example.backofficer.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backofficer.App;
import com.example.backofficer.R;
import com.example.backofficer.adapter.VehicleAdapter;
import com.example.backofficer.model.Information;
import com.example.backofficer.model.Vehicle;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListVehicle extends AppCompatActivity implements View.OnClickListener, VehicleAdapter.VehicleClickListener{
    RecyclerView rvListVehicle;
    ArrayList<Vehicle> vehicleArrayList;
    VehicleAdapter vehicleAdapter;
    ImageButton ibBackPressListVehicle;
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_vehicle);

        ibBackPressListVehicle = findViewById(R.id.ibBackPressListVehicle);

        rvListVehicle = findViewById(R.id.rvListVehicle);
        rvListVehicle.setHasFixedSize(true);
        rvListVehicle.setLayoutManager(new LinearLayoutManager(this));

        vehicleArrayList = new ArrayList<>();
        vehicleAdapter = new VehicleAdapter(vehicleArrayList, this);
        rvListVehicle.setAdapter(vehicleAdapter);

        app = (App) getApplication();

        ibBackPressListVehicle.setOnClickListener(this);

        loadVehicle();
    }

    private void loadVehicle(){
        app.dataBase
                .collection("vehicle")
                .orderBy("registerNumber", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(app, "Fail Loading Data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                vehicleArrayList.add(dc.getDocument().toObject(Vehicle.class));
                            }
                        }

                        vehicleAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.ibBackPressListVehicle){
            moveToCreateTask();
        }
    }

    private void moveToCreateTask() {
        Intent intent = new Intent(ListVehicle.this, CreateTask.class);
        startActivity(intent);
        finish();
    }

    private void moveToCreateTaskWithVehicle(Vehicle vehicle){
        String text = "ListVehicle";
        Intent intent = new Intent(ListVehicle.this, CreateTask.class);
        intent.putExtra("sendingText", text);
        intent.putExtra("sendingVehicle", vehicle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClickChoose(Vehicle vehicle) {
        openDialog(Gravity.CENTER, vehicle);
    }

    private void openDialog(int gravity, Vehicle vehicle) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_vehicle);

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

        Button btnCancelDialogAddVehicle = dialog.findViewById(R.id.btnCancelDialogAddVehicle);
        Button btnOkDialogAddVehicle = dialog.findViewById(R.id.btnOkDialogAddVehicle);

        btnCancelDialogAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOkDialogAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToCreateTaskWithVehicle(vehicle);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
