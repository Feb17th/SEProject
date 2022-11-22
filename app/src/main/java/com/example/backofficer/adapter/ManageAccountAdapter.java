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

public class ManageAccountAdapter extends RecyclerView.Adapter<ManageAccountAdapter.ViewHolder> {
    ArrayList<Information> informationList;
    ManageAccountClickListener manageAccountClickListener;

    public ManageAccountAdapter(ArrayList<Information> informationList, ManageAccountClickListener manageAccountClickListener) {
        this.informationList = informationList;
        this.manageAccountClickListener = manageAccountClickListener;
    }

    public interface ManageAccountClickListener{
        void onClickInfo(Information information);
        void onClickDelete(Information information);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_manage_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageAccountAdapter.ViewHolder holder, int position) {
        Information information = informationList.get(position);
        holder.bind(information, position);
    }

    @Override
    public int getItemCount() {
        return informationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameItemManageAccount, tvJobTypeItemManageAccount, tvNumberItemManageAccount;
        ImageButton ibInfoItemManageAccount, ibDeleteItemManageAccount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumberItemManageAccount = itemView.findViewById(R.id.tvNumberItemManageAccount);
            tvNameItemManageAccount = itemView.findViewById(R.id.tvNameItemManageAccount);
            tvJobTypeItemManageAccount = itemView.findViewById(R.id.tvJobTypeItemManageAccount);
            ibInfoItemManageAccount = itemView.findViewById(R.id.ibInfoItemManageAccount);
            ibDeleteItemManageAccount = itemView.findViewById(R.id.ibDeleteItemManageAccount);
        }

        public void bind(Information information, int position) {
            tvNumberItemManageAccount.setText(String.valueOf(position + 1));
            tvNameItemManageAccount.setText(information.getFullName());
            tvJobTypeItemManageAccount.setText(information.getJobType());

            ibInfoItemManageAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageAccountClickListener.onClickInfo(information);
                }
            });
            ibDeleteItemManageAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageAccountClickListener.onClickDelete(information);
                }
            });
        }
    }
}
