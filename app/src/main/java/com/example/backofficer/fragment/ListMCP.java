package com.example.backofficer.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backofficer.MCPClickListener;
import com.example.backofficer.R;
import com.example.backofficer.adapter.MCPAdapter;
import com.example.backofficer.model.MCP;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ListMCP extends BottomSheetDialogFragment {
    ArrayList<MCP> mcpArrayList;
    MCPClickListener mcpClickListener;
    RecyclerView rvListMCP;
    MCPAdapter adapter;

    public ListMCP(ArrayList<MCP> mcpArrayList, MCPClickListener mcpClickListener) {
        this.mcpArrayList = mcpArrayList;
        this.mcpClickListener = mcpClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_mcp, null);
        bottomSheetDialog.setContentView(view);

        rvListMCP = view.findViewById(R.id.rvListMCP);
        rvListMCP.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MCPAdapter(mcpArrayList, mcpClickListener);
        rvListMCP.setAdapter(adapter);

        return bottomSheetDialog;
    }
}
