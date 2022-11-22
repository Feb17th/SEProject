package com.example.backofficer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @Override
    public void onClickChoose(Vehicle vehicle) {

    }
}
