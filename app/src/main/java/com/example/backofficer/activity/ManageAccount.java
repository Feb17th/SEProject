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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backofficer.App;
import com.example.backofficer.R;
import com.example.backofficer.adapter.ManageAccountAdapter;
import com.example.backofficer.model.Information;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageAccount extends AppCompatActivity implements View.OnClickListener, ManageAccountAdapter.ManageAccountClickListener {
    RecyclerView rvManageAccount;
    ArrayList<Information> informationArrayList;
    ManageAccountAdapter manageAccountAdapter;
    ImageButton ibAddManageAccount, ibBackPressManageAccount;
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_account);

        ibAddManageAccount = findViewById(R.id.ibAddManageAccount);
        ibBackPressManageAccount = findViewById(R.id.ibBackPressManageAccount);

        rvManageAccount = findViewById(R.id.rvManageAccount);
        rvManageAccount.setHasFixedSize(true);
        rvManageAccount.setLayoutManager(new LinearLayoutManager(this));

        informationArrayList = new ArrayList<>();
        manageAccountAdapter = new ManageAccountAdapter(informationArrayList, this);
        rvManageAccount.setAdapter(manageAccountAdapter);

        app = (App) getApplication();

        ibAddManageAccount.setOnClickListener(this);
        ibBackPressManageAccount.setOnClickListener(this);

        LoadDataAccount();
    }

    private void LoadDataAccount() {
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

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                informationArrayList.add(dc.getDocument().toObject(Information.class));
                            }
                        }

                        manageAccountAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.ibAddManageAccount){
            moveToRegister();
        } else if(id == R.id.ibBackPressManageAccount){
            moveToChooseFeatureBackOfficer();
        }
    }

    private void moveToChooseFeatureBackOfficer() {
        Intent intent = new Intent(ManageAccount.this, ChooseFeatureBackOfficer.class);
        startActivity(intent);
        finish();
    }

    private void moveToRegister() {
        Intent intent = new Intent(ManageAccount.this, Register.class);
        startActivity(intent);
        finish();
    }

    private void moveToViewInformation(Information information){
        String text = "ManageAccount";
        Intent intent = new Intent(ManageAccount.this, ViewInformation.class);
        intent.putExtra("sendingText", text);
        intent.putExtra("sendingInformation", information);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClickInfo(Information information) {
        moveToViewInformation(information);
    }

    @Override
    public void onClickDelete(Information information) {
        openDialog(Gravity.CENTER, information);
    }

    private void openDialog(int gravity, Information information) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete_account);

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

        Button btnCancelDialogDeleteAccount = dialog.findViewById(R.id.btnCancelDialogDeleteAccount);
        Button btnOkDialogDeleteAccount = dialog.findViewById(R.id.btnOkDialogDeleteAccount);

        btnCancelDialogDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOkDialogDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmailOfProject(information);
                refreshActivity();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void refreshActivity(){
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
    }

    private void deleteEmailOfProject(Information information){
        //Delete on Firestore Database
        app.dataBase
                .collection("information")
                .document(information.getEmailOfProject())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(app, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(app, "Can't delete", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
