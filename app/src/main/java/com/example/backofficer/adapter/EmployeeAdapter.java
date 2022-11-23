package com.example.backofficer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backofficer.R;
import com.example.backofficer.model.Information;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder>{
    ArrayList<Information> informationArrayList;
    InformationClickListener informationClickListener;

    public EmployeeAdapter(ArrayList<Information> informationArrayList, InformationClickListener informationClickListener) {
        this.informationArrayList = informationArrayList;
        this.informationClickListener = informationClickListener;
    }

    public interface InformationClickListener{
        void onClickChoose(Information information);
    }

    @NonNull
    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.ViewHolder holder, int position) {
        Information information = informationArrayList.get(position);
        holder.bind(information, position);
    }

    @Override
    public int getItemCount() {
        return informationArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumberItemListEmployee, tvFullNameListEmployee, tvJobTypeListEmployee;
        ImageButton ibChooseListEmployee;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumberItemListEmployee = itemView.findViewById(R.id.tvNumberItemListEmployee);
            tvFullNameListEmployee = itemView.findViewById(R.id.tvFullNameListEmployee);
            tvJobTypeListEmployee = itemView.findViewById(R.id.tvJobTypeListEmployee);
            ibChooseListEmployee = itemView.findViewById(R.id.ibChooseListEmployee);
        }

        public void bind(Information information, int position) {
            tvNumberItemListEmployee.setText(String.valueOf(position + 1));
            tvFullNameListEmployee.setText(information.getFullName());
            tvJobTypeListEmployee.setText(information.getJobType());

            ibChooseListEmployee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    informationClickListener.onClickChoose(information);
                }
            });
        }
    }
}
