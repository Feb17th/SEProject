package com.example.backofficer.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backofficer.R;
import com.example.backofficer.VehicleClickListener;
import com.example.backofficer.adapter.VehicleAdapter;
import com.example.backofficer.model.Vehicle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ListVehicleFragment extends BottomSheetDialogFragment {
    ArrayList<Vehicle> vehicleArrayList;
    VehicleClickListener vehicleClickListener;
    RecyclerView rvListVehicleFragment;
    VehicleAdapter adapter;

    public ListVehicleFragment(ArrayList<Vehicle> vehicleArrayList, VehicleClickListener vehicleClickListener) {
        this.vehicleArrayList = vehicleArrayList;
        this.vehicleClickListener = vehicleClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_vehicle_fragment, null);
        bottomSheetDialog.setContentView(view);

        rvListVehicleFragment = view.findViewById(R.id.rvListVehicleFragment);
        rvListVehicleFragment.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new VehicleAdapter(vehicleArrayList, vehicleClickListener);
        rvListVehicleFragment.setAdapter(adapter);

        return bottomSheetDialog;
    }
}
