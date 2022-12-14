package com.example.backofficer.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.backofficer.App;
import com.example.backofficer.InformationClickListener;
import com.example.backofficer.MCPClickListener;
import com.example.backofficer.R;
import com.example.backofficer.VehicleClickListener;
import com.example.backofficer.databinding.CreateTaskBinding;
import com.example.backofficer.fragment.ListEmployee;
import com.example.backofficer.fragment.ListMCP;
import com.example.backofficer.fragment.ListVehicle;
import com.example.backofficer.model.Information;
import com.example.backofficer.model.MCP;
import com.example.backofficer.model.Task;
import com.example.backofficer.model.Vehicle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateTask extends AppCompatActivity implements View.OnClickListener{
    private CreateTaskBinding binding;
    App app;
    Task taskFromDetails = new Task();

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
        binding.btnAssignMCPCreateTask.setOnClickListener(this);

        String textReceiveFromAnotherActivity = getIntent().getStringExtra("sendingText");
        if(textReceiveFromAnotherActivity.equals("Details")){
            taskFromDetails = (Task) getIntent().getSerializableExtra("sendingTask");
            displayFromDetails(taskFromDetails);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String textReceiveFromAnotherActivity = getIntent().getStringExtra("sendingText");

        if(id == binding.btnDoneCreateTask.getId()){
            if(textReceiveFromAnotherActivity.equals("ManageTask")){
                onSuccess();
            } else if(textReceiveFromAnotherActivity.equals("Details")){
                updateTaskToFirebase(taskFromDetails);
            }
        } else if(id == binding.ibBackPressCreateTask.getId()){
            if(textReceiveFromAnotherActivity.equals("ManageTask")){
                moveToManageTask();
            } else if(textReceiveFromAnotherActivity.equals("Details")){
                moveToDetails(taskFromDetails);
            }
        } else if(id == binding.btnTimeStartCreateTask.getId()){
            pickTime();
        } else if(id == binding.btnAssignVehicleCreateTask.getId()){
            openVehicleBottomSheetFragment();
        } else if(id == binding.btnEmployeeCreateTask.getId()){
            openEmployeeBottomSheetFragment();
        } else if(id == binding.btnAssignMCPCreateTask.getId()){
            openMCPBottomSheetFragment();
        }
    }

    private void updateTaskToFirebase(Task task){
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("time", binding.btnTimeStartCreateTask.getText().toString().trim());
        taskMap.put("vehicle", binding.btnAssignVehicleCreateTask.getText().toString().trim());
        taskMap.put("mcp", binding.btnAssignMCPCreateTask.getText().toString().trim());
        taskMap.put("fullName", binding.btnEmployeeCreateTask.getText().toString().trim());
        app.dataBase
                .collection("task")
                .document(binding.txtDescriptionCreateTask.getText().toString().trim())
                .update(taskMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(app, "Update successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(app, "Have some wrong during updating task!", Toast.LENGTH_SHORT).show();
                    }
                });

        updateTaskBeforeMoveToDetails(task);
    }

    private void updateTaskBeforeMoveToDetails(Task task){
        task.setTime(binding.btnTimeStartCreateTask.getText().toString().trim());
        task.setVehicle(binding.btnAssignVehicleCreateTask.getText().toString().trim());
        task.setMcp(binding.btnAssignMCPCreateTask.getText().toString().trim());
        task.setFullName(binding.btnEmployeeCreateTask.getText().toString().trim());
    }

    private void displayFromDetails(Task task){
        String tittle = "Update Task";
        String updateButton = "Update";

        binding.tvTittleCreateTask.setText(tittle);
        binding.btnDoneCreateTask.setText(updateButton);

        binding.btnTimeStartCreateTask.setText(task.getTime());
        binding.btnAssignVehicleCreateTask.setText(task.getVehicle());
        binding.txtDescriptionCreateTask.setText(task.getDescription());
        binding.btnAssignMCPCreateTask.setText(task.getMcp());
        binding.btnEmployeeCreateTask.setText(task.getFullName());

        binding.txtDescriptionCreateTask.setFocusable(false);
        binding.txtDescriptionCreateTask.setTextColor(getResources().getColor(R.color.black));
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

    private void openVehicleBottomSheetFragment() {
        ArrayList<Vehicle> vehicleArrayList = new ArrayList<>();
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
                    }
                });

        // ------------------------------------
        /*
        This line is used to run vehicleArrayList, if you delete this line, vehicleArrayList will not run.
        I don't know the reason.
        */
        String temp = checkEmptyVehicle(vehicleArrayList);
        // ------------------------------------

        ListVehicle listVehicle = new ListVehicle(vehicleArrayList, new VehicleClickListener() {
            @Override
            public void onClickChoose(Vehicle vehicle) {
                openDialogListVehicle(Gravity.CENTER, vehicle);
            }
        });
        listVehicle.show(getSupportFragmentManager(), listVehicle.getTag());
    }

    private void openEmployeeBottomSheetFragment() {
        ArrayList<Information> informationArrayList = new ArrayList<>();
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
                    }
                });

        // ------------------------------------
        /*
        This line is used to run informationArrayList, if you delete this line, informationArrayList will not run.
        I don't know the reason.
        */
        String temp = checkEmptyEmployee(informationArrayList);
        // ------------------------------------

        ListEmployee listEmployee = new ListEmployee(informationArrayList, new InformationClickListener() {
            @Override
            public void onClickChoose(Information information) {
                openDiaLogListEmployee(Gravity.CENTER, information);
            }
        });
        listEmployee.show(getSupportFragmentManager(), listEmployee.getTag());
    }

    private void openMCPBottomSheetFragment(){
        ArrayList<MCP> mcpArrayList = new ArrayList<>();
        app.dataBase
                .collection("mcp")
                .orderBy("mcp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(app, "Fail Loading Data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                mcpArrayList.add(dc.getDocument().toObject(MCP.class));
                            }
                        }
                    }
                });

        // ------------------------------------
        /*
        This line is used to run vehicleArrayList, if you delete this line, vehicleArrayList will not run.
        I don't know the reason.
        */
        String temp = checkEmptyMCP(mcpArrayList);
        // ------------------------------------

        ListMCP listMCP = new ListMCP(mcpArrayList, new MCPClickListener() {
            @Override
            public void onClickChoose(MCP mcp) {
                openDialogListMCP(Gravity.CENTER, mcp);
            }
        });
        listMCP.show(getSupportFragmentManager(), listMCP.getTag());
    }

    private String checkEmptyVehicle(ArrayList<Vehicle> arrayList){
        if(arrayList.isEmpty()){
            return "true";
        } else return "false";
    }

    private String checkEmptyEmployee(ArrayList<Information> arrayList){
        if(arrayList.isEmpty()){
            return "true";
        } else return "false";
    }

    private String checkEmptyMCP(ArrayList<MCP> arrayList){
        if(arrayList.isEmpty()){
            return "true";
        } else return "false";
    }

    private void openDialogListVehicle(int gravity, Vehicle vehicle) {
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
                binding.btnAssignVehicleCreateTask.setText(String.valueOf(vehicle.getRegisterNumber()));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDiaLogListEmployee(int gravity, Information information){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_employee);

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

        Button btnCancelDialogAddEmployee = dialog.findViewById(R.id.btnCancelDialogAddEmployee);
        Button btnOkDialogAddEmployee = dialog.findViewById(R.id.btnOkDialogAddEmployee);

        btnCancelDialogAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOkDialogAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnEmployeeCreateTask.setText(String.valueOf(information.getFullName()));
                emailOfProject = information.getEmailOfProject();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogListMCP(int gravity, MCP mcp){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_mcp);

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

        Button btnCancelDialogAddMCP = dialog.findViewById(R.id.btnCancelDialogAddMCP);
        Button btnOkDialogAddMCP = dialog.findViewById(R.id.btnOkDialogAddMCP);

        btnCancelDialogAddMCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOkDialogAddMCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnAssignMCPCreateTask.setText(String.valueOf(mcp.getMcp()));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void moveToManageTask() {
        String text = "CreateTask";
        Intent intent = new Intent(CreateTask.this, ManageTask.class);
        intent.putExtra("sendingText", text);
        startActivity(intent);
        finish();
    }

    private void moveToDetails(Task task){
        String text = "CreateTask";
        Intent intent = new Intent(CreateTask.this, Details.class);
        intent.putExtra("sendingText", text);
        intent.putExtra("sendingTask", task);
        startActivity(intent);
        finish();
    }

    private void saveDataToDatabase() {
        Task task = new Task(
                binding.btnEmployeeCreateTask.getText().toString().trim(),
                binding.txtDescriptionCreateTask.getText().toString().trim(),
                binding.btnTimeStartCreateTask.getText().toString().trim(),
                binding.btnAssignVehicleCreateTask.getText().toString().trim(),
                binding.btnAssignMCPCreateTask.getText().toString().trim(),
                emailOfProject,
                "Working"
        );
        app.dataBase
                .collection("task")
                .document(binding.txtDescriptionCreateTask.getText().toString().trim())
                .set(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(app, "Create successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(app, "Create task fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String checkSuccess(){
        if(binding.btnEmployeeCreateTask.getText().toString().trim().isEmpty()
        || binding.txtDescriptionCreateTask.getText().toString().trim().isEmpty()
        || binding.btnTimeStartCreateTask.getText().toString().trim().isEmpty()
        || binding.btnAssignVehicleCreateTask.getText().toString().trim().isEmpty()
        || binding.btnAssignMCPCreateTask.getText().toString().trim().isEmpty()
        || emailOfProject.equals("")){
            return "Have a empty element";
        } else return "Done";
    }

    private void changeUserVehicle(){
        Map<String, Object> vehicleMap = new HashMap<>();
        vehicleMap.put("user", binding.btnEmployeeCreateTask.getText().toString().trim());
        app.dataBase
                .collection("vehicle")
                .document(binding.btnAssignVehicleCreateTask.getText().toString().trim())
                .update(vehicleMap);
    }

    private void onSuccess(){
        String check = checkSuccess();
        if(check.equals("Done")){
            saveDataToDatabase();
            changeUserVehicle();
            moveToManageTask();
        } else if(check.equals("Have a empty element")){
            Toast.makeText(app, "Please type enough information", Toast.LENGTH_SHORT).show();
        }
    }

    private String setDate (){
        Date today = Calendar.getInstance().getTime();//getting date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");//formating according to my need
        String date = formatter.format(today);
        return date;
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
