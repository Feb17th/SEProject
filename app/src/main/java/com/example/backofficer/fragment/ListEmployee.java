package com.example.backofficer.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backofficer.InformationClickListener;
import com.example.backofficer.R;
import com.example.backofficer.adapter.EmployeeAdapter;
import com.example.backofficer.model.Information;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ListEmployee extends BottomSheetDialogFragment {
    ArrayList<Information> informationArrayList;
    InformationClickListener informationClickListener;
    RecyclerView rvListEmployee;
    EmployeeAdapter adapter;

    public ListEmployee(ArrayList<Information> informationArrayList, InformationClickListener informationClickListener) {
        this.informationArrayList = informationArrayList;
        this.informationClickListener = informationClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_employee, null);
        bottomSheetDialog.setContentView(view);

        rvListEmployee = view.findViewById(R.id.rvListEmployee);
        rvListEmployee.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new EmployeeAdapter(informationArrayList, informationClickListener);
        rvListEmployee.setAdapter(adapter);

        return bottomSheetDialog;
    }
}
