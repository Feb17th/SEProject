package com.example.backofficer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backofficer.R;
import com.example.backofficer.model.Task;

import java.util.ArrayList;

public class ManageTaskAdapter extends RecyclerView.Adapter<ManageTaskAdapter.ViewHolder>{
    ArrayList<Task> taskList;
    ManageTaskClickListener manageTaskClickListener;

    public ManageTaskAdapter(ArrayList<Task> taskList, ManageTaskClickListener manageTaskClickListener) {
        this.taskList = taskList;
        this.manageTaskClickListener = manageTaskClickListener;
    }

    public interface ManageTaskClickListener{
        void onClickDetails(Task task);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_manage_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task, position);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumberItemManageTask, tvDescriptionItemManageTask;
        Button btnDetailsItemManageTask;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumberItemManageTask = itemView.findViewById(R.id.tvNumberItemManageTask);
            tvDescriptionItemManageTask = itemView.findViewById(R.id.tvDescriptionItemManageTask);
            btnDetailsItemManageTask = itemView.findViewById(R.id.btnDetailsItemManageTask);
        }

        public void bind(Task task, int position) {
            tvNumberItemManageTask.setText(String.valueOf(position + 1));
            tvDescriptionItemManageTask.setText(task.getDescription());

            btnDetailsItemManageTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageTaskClickListener.onClickDetails(task);
                }
            });
        }
    }
}
