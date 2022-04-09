package com.example.colo.Projects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.ArrayList;

public class ManagerProjectAdapter extends RecyclerView.Adapter<ManagerProjectAdapter.MyViewHolder> {

    Context context;
    ArrayList<ProjectData> list;
    public ManagerProjectAdapter(Context context, ArrayList<ProjectData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.project_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ProjectData data = list.get(position);
        holder.pName.setText(data.getpName());
        holder.pDescription.setText(data.getpDescription());
        //holder.employees.setText(data.getEmployees());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pName, pDescription, employees;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pName = itemView.findViewById(R.id.pName);
            pDescription = itemView.findViewById(R.id.pDescription);
            //employees = itemView.findViewById(R.id.employees);
        }
    }

}
