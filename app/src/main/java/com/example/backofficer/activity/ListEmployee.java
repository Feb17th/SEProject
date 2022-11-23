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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backofficer.App;
import com.example.backofficer.R;
import com.example.backofficer.adapter.EmployeeAdapter;
import com.example.backofficer.model.Information;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListEmployee extends AppCompatActivity implements View.OnClickListener, EmployeeAdapter.InformationClickListener {
    RecyclerView rvListEmployee;
    ArrayList<Information> informationArrayList;
    EmployeeAdapter employeeAdapter;
    ImageButton ibBackPressListEmployee;
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_employee);

        ibBackPressListEmployee = findViewById(R.id.ibBackPressListEmployee);

        rvListEmployee = findViewById(R.id.rvListEmployee);
        rvListEmployee.setHasFixedSize(true);
        rvListEmployee.setLayoutManager(new LinearLayoutManager(this));

        informationArrayList = new ArrayList<>();
        employeeAdapter = new EmployeeAdapter(informationArrayList, this);
        rvListEmployee.setAdapter(employeeAdapter);

        app = (App) getApplication();

        ibBackPressListEmployee.setOnClickListener(this);
        
        loadEmployee();
    }

    private void loadEmployee() {
        app.dataBase
                .collection("information")
                .orderBy("jobType", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(app, "Fail Loading Data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Information information;
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                information = dc.getDocument().toObject(Information.class);
                                if(checkRole(information.getEmailOfProject()).equals("employee")){
                                    informationArrayList.add(information);
                                }
                            }
                        }

                        employeeAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.ibBackPressListEmployee){
            moveToCreateTask();
        }
    }

    private void moveToCreateTask() {
        String text = "ListEmployee";
        Intent intent = new Intent(ListEmployee.this, CreateTask.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }

    private void moveToCreateTaskWithInformation(Information information){
        String text = "ListEmployeeWithInformation";
        Intent intent = new Intent(ListEmployee.this, CreateTask.class);
        intent.putExtra("sendingText", text);
        intent.putExtra("sendingInformation", information);
        startActivity(intent);
    }

    @Override
    public void onClickChoose(Information information) {
        openDialog(Gravity.CENTER, information);
    }

    //Use the same xml with ListVehicle
    private void openDialog(int gravity, Information information) {
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

        TextView tvDescriptionDialog = dialog.findViewById(R.id.tvDescriptionDialog);
        Button btnCancelDialogAddVehicle = dialog.findViewById(R.id.btnCancelDialogAddVehicle);
        Button btnOkDialogAddVehicle = dialog.findViewById(R.id.btnOkDialogAddVehicle);

        String chosenText = "Do you want to choose this employee?";
        tvDescriptionDialog.setText(chosenText);

        btnCancelDialogAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOkDialogAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToCreateTaskWithInformation(information);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private String checkRole(String EmailOfProject){
        String result = "";
        if(EmailOfProject == null) return result;
        if(EmailOfProject.charAt(0) == 'b' && EmailOfProject.charAt(1) == 'o'){
            result = "backOfficer";
        } else {
            result = "employee";
        }
        return result;
    }
}
