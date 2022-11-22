package com.example.backofficer.activity;

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
import java.util.Locale;

public class CreateTask extends AppCompatActivity implements View.OnClickListener{
    private CreateTaskBinding binding;
    App app;

    int hour, minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (App) getApplication();

        binding.btnDoneCreateTask.setOnClickListener(this);
        binding.ibBackPressCreateTask.setOnClickListener(this);
        binding.btnTimeStartCreateTask.setOnClickListener(this);
        binding.btnAssignVehicleCreateTask.setOnClickListener(this);
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

    private void onCreateSuccess() {
    }
}
