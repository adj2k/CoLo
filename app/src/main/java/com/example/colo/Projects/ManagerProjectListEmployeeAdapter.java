package com.example.colo.Projects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colo.R;

import java.util.ArrayList;

public class ManagerProjectListEmployeeAdapter extends RecyclerView.Adapter<ManagerProjectListEmployeeAdapter.MyViewHolder> {

    Context context;

    ArrayList<ManagerProjectListEmployeeData> list;

    public ManagerProjectListEmployeeAdapter(Context context, ArrayList<ManagerProjectListEmployeeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.employee,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ManagerProjectListEmployeeData user = list.get(position);
        holder.name.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.employee_name);
        }
    }
}
