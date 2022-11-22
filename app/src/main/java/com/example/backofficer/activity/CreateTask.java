package com.example.backofficer.activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.databinding.CreateTaskBinding;
import com.example.backofficer.model.Information;
import com.example.backofficer.model.Vehicle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

    private void onCreateSuccess() {
    }
}
