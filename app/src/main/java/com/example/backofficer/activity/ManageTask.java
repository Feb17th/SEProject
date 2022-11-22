package com.example.backofficer.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.backofficer.adapter.ManageTaskAdapter;
import com.example.backofficer.model.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.DocumentMask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ManageTask extends AppCompatActivity implements View.OnClickListener, ManageTaskAdapter.ManageTaskClickListener{
    RecyclerView rvManageTask;
    ArrayList<Task> taskArrayList;
    ManageTaskAdapter manageTaskAdapter;
    TextView tvTimeLineManageTask;
    Button btnAddTaskManageTask;
    ImageButton ibBackPressManageTask;
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_task);

        tvTimeLineManageTask = findViewById(R.id.tvTimeLineManageTask);
        btnAddTaskManageTask = findViewById(R.id.btnAddTaskManageTask);
        ibBackPressManageTask = findViewById(R.id.ibBackPressManageTask);

        rvManageTask = findViewById(R.id.rvManageTask);
        rvManageTask.setHasFixedSize(true);
        rvManageTask.setLayoutManager(new LinearLayoutManager(this));

        taskArrayList = new ArrayList<>();
        manageTaskAdapter = new ManageTaskAdapter(taskArrayList, this);
        rvManageTask.setAdapter(manageTaskAdapter);

        app = (App) getApplication();

        tvTimeLineManageTask = setDate(tvTimeLineManageTask);

        btnAddTaskManageTask.setOnClickListener(this);
        ibBackPressManageTask.setOnClickListener(this);

        String textReceiveFromAnotherActivity = getIntent().getStringExtra("sendingText");
        /*
        there are 3 kinds of text:
            + ChooseFeatureBackOfficer
            + ChooseFeatureEmployee
            + Details
        */
        if(textReceiveFromAnotherActivity.equals("ChooseFeatureBackOfficer")){
            LoadDataTask();
        } else if(textReceiveFromAnotherActivity.equals("ChooseFeatureEmployee")){
            btnAddTaskManageTask.setVisibility(View.GONE);
            LoadDataEmployee();
        } else if(textReceiveFromAnotherActivity.equals("Details")){
            LoadDataTask();
        } else if(textReceiveFromAnotherActivity.equals("DetailsEmployee")){
            btnAddTaskManageTask.setVisibility(View.GONE);
            LoadDataEmployee();
        } else if(textReceiveFromAnotherActivity.equals("CreateTask")){
            LoadDataTask();
        }
    }

    private void LoadDataTask() {
        app.dataBase
                .collection("task")
                .orderBy("description", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(app, "Fail Loading Data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                taskArrayList.add(dc.getDocument().toObject(Task.class));
                            }
                        }

                        manageTaskAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void LoadDataEmployee(){
        FirebaseUser user = app.auth.getCurrentUser();
        app.dataBase
                .collection("task")
                .orderBy("description", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(app, "Fail Loading Data", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        
                        Task tempTask;
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                tempTask = dc.getDocument().toObject(Task.class);
                                if(tempTask.getEmailOfProject().equals(user.getEmail())){
                                    taskArrayList.add(tempTask);
                                }
                            }
                        }

                        manageTaskAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.ibBackPressManageTask){
            moveToChooseFeature();
        } else if(id == R.id.btnAddTaskManageTask){
            moveToCreateTask();
        }
    }

    @Override
    public void onClickDetails(Task task) {
        String checkRole = checkRole();
        if(checkRole.equals("backOfficer")){
            moveToDetails(task);
        } else if(checkRole.equals("employee")){
            moveToDetailsEmployee(task);
        }
    }

    private void moveToDetailsEmployee(Task task) {
        Intent intent = new Intent(ManageTask.this, DetailsEmployee.class);
        intent.putExtra("sendingTask", task);
        startActivity(intent);
        finish();
    }

    private void moveToDetails(Task task) {
        Intent intent = new Intent(ManageTask.this, Details.class);
        intent.putExtra("sendingTask", task);
        startActivity(intent);
        finish();
    }

    private void moveToChooseFeature() {
        String checkRole = checkRole();
        if(checkRole.equals("backOfficer")){
            Intent intent = new Intent(ManageTask.this, ChooseFeatureBackOfficer.class);
            startActivity(intent);
            finish();
        } else if(checkRole.equals("employee")){
            Intent intent = new Intent(ManageTask.this, ChooseFeatureEmployee.class);
            startActivity(intent);
            finish();
        }
    }

    private void moveToCreateTask(){
        Intent intent = new Intent(ManageTask.this, CreateTask.class);
        startActivity(intent);
        finish();
    }

    private TextView setDate (TextView textView){
        Date today = Calendar.getInstance().getTime();//getting date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");//formating according to my need
        String date = formatter.format(today);
        textView.setText(date);
        return textView;
    }
    
    private String checkRole(){
        FirebaseUser user = app.auth.getCurrentUser();
        String email = user.getEmail();
        String result = "";
        if(email == null) return result;
        if(email.charAt(0) == 'b' && email.charAt(1) == 'o'){
            result = "backOfficer";
        } else {
            result = "employee";
        }
        return result;
    }
}
