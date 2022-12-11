package com.example.backofficer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backofficer.MCPClickListener;
import com.example.backofficer.R;
import com.example.backofficer.model.MCP;

import java.util.ArrayList;

public class MCPAdapter extends RecyclerView.Adapter<MCPAdapter.ViewHolder> {
    ArrayList<MCP> mcpArrayList;
    MCPClickListener mcpClickListener;

    public MCPAdapter(ArrayList<MCP> mcpArrayList, MCPClickListener mcpClickListener) {
        this.mcpArrayList = mcpArrayList;
        this.mcpClickListener = mcpClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_mcp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MCP mcp = mcpArrayList.get(position);
        holder.bind(mcp, position);
    }

    @Override
    public int getItemCount() {
        return mcpArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumberItemListMCP, tvLocationListMCP;
        ImageButton ibChooseListMCP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumberItemListMCP = itemView.findViewById(R.id.tvNumberItemListMCP);
            tvLocationListMCP = itemView.findViewById(R.id.tvLocationListMCP);
            ibChooseListMCP = itemView.findViewById(R.id.ibChooseListMCP);
        }

        public void bind(MCP mcp, int position) {
            tvNumberItemListMCP.setText(String.valueOf(position + 1));
            tvLocationListMCP.setText(mcp.getMcp());

            ibChooseListMCP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcpClickListener.onClickChoose(mcp);
                }
            });
        }
    }
}
