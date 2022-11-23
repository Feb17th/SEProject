package com.example.backofficer.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.CreateTaskBinding;
import com.example.backofficer.model.Information;
import com.example.backofficer.model.Vehicle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateTask extends AppCompatActivity implements View.OnClickListener{
    private CreateTaskBinding binding;
    App app;

    int hour, minute;
    String emailOfProject = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.tvTimeLineCreateTask.setText(setDate());

        binding.btnDoneCreateTask.setOnClickListener(this);
        binding.ibBackPressCreateTask.setOnClickListener(this);
        binding.btnTimeStartCreateTask.setOnClickListener(this);
        binding.btnAssignVehicleCreateTask.setOnClickListener(this);
        binding.btnEmployeeCreateTask.setOnClickListener(this);

        String textReceiveFromAnotherActivity = getIntent().getStringExtra("sendingText");
        if(textReceiveFromAnotherActivity.equals("ListVehicleWithVehicle")){
            Vehicle vehicleFromListVehicle = (Vehicle) getIntent().getSerializableExtra("sendingVehicle");
            binding.btnAssignVehicleCreateTask.setText(String.valueOf(vehicleFromListVehicle.getRegisterNumber()));
        } else if(textReceiveFromAnotherActivity.equals("ListEmployeeWithInformation")){
            Information informationFromListEmployee = (Information) getIntent().getSerializableExtra("sendingInformation");
            binding.btnEmployeeCreateTask.setText(String.valueOf(informationFromListEmployee.getFullName()));
            emailOfProject = informationFromListEmployee.getEmailOfProject();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == binding.btnDoneCreateTask.getId()){
            onCreateSuccess();
        } else if(id == binding.ibBackPressCreateTask.getId()){
            moveToManageTask();
        } else if(id == binding.btnTimeStartCreateTask.getId()){
            pickTime();
        } else if(id == binding.btnAssignVehicleCreateTask.getId()){
            moveToListVehicle();
        } else if(id == binding.btnEmployeeCreateTask.getId()){
            moveToListEmployee();
        }
    }

    private void pickTime() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                binding.btnTimeStartCreateTask.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void moveToManageTask() {
        String text = "CreateTask";
        Intent intent = new Intent(CreateTask.this, ManageTask.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }

    private void moveToListVehicle(){
        Intent intent = new Intent(CreateTask.this, ListVehicle.class);
        startActivity(intent);
        finish();
    }

    private void moveToListEmployee() {
        Intent intent = new Intent(CreateTask.this, ListEmployee.class);
        startActivity(intent);
        finish();
    }

    private void onCreateSuccess() {
    }

    private String setDate (){
        Date today = Calendar.getInstance().getTime();//getting date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");//formating according to my need
        String date = formatter.format(today);
        return date;
    }
}
