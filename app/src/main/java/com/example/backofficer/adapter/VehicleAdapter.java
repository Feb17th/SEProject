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
import com.example.backofficer.VehicleClickListener;
import com.example.backofficer.activity.ListVehicle;
import com.example.backofficer.model.Vehicle;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder>{
    ArrayList<Vehicle> vehicleArrayList;
    VehicleClickListener vehicleClickListener;

    public VehicleAdapter(ArrayList<Vehicle> vehicleArrayList, VehicleClickListener vehicleClickListener) {
        this.vehicleArrayList = vehicleArrayList;
        this.vehicleClickListener = vehicleClickListener;
    }

    @NonNull
    @Override
    public VehicleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_vehicle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleAdapter.ViewHolder holder, int position) {
        Vehicle vehicle = vehicleArrayList.get(position);
        holder.bind(vehicle, position);
    }

    @Override
    public int getItemCount() {
        return vehicleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumberItemListVehicle, tvUserListVehicle;
        ImageButton ibChooseListVehicle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumberItemListVehicle = itemView.findViewById(R.id.tvNumberItemListVehicle);
            tvUserListVehicle = itemView.findViewById(R.id.tvUserListVehicle);
            ibChooseListVehicle = itemView.findViewById(R.id.ibChooseListVehicle);
        }

        public void bind(Vehicle vehicle, int position) {
            tvNumberItemListVehicle.setText(String.valueOf(vehicle.getRegisterNumber()));
            tvUserListVehicle.setText(vehicle.getUser());

            ibChooseListVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vehicleClickListener.onClickChoose(vehicle);
                }
            });
        }
    }
}
