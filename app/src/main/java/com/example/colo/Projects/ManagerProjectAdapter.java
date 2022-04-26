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

public class ManagerProjectAdapter extends RecyclerView.Adapter<ManagerProjectAdapter.MyViewHolder> {

    Context context;
    ArrayList<ProjectData> list;
    private OnNoteListener mOnNoteListener;

    public ManagerProjectAdapter(Context context, ArrayList<ProjectData> list, OnNoteListener onNoteListener) {
        this.context = context;
        this.list = list;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.project_item, parent, false);
        return new MyViewHolder(v, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ProjectData data = list.get(position);
        holder.pName.setText(data.getpName());
        holder.pDescription.setText(data.getpDescription());
//        holder.pEmployee.setText((CharSequence) data.getpEmployee());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView pName, pDescription, pEmployee;
        OnNoteListener onNoteListener;


        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            pName = itemView.findViewById(R.id.pName);
            pDescription = itemView.findViewById(R.id.descriptionText);
//            pEmployee = itemView.findViewById(R.id.currentEmployeeText);

            this.onNoteListener = onNoteListener;
            //employees = itemView.findViewById(R.id.employees);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    // adds clicker to list
    public interface OnNoteListener{
        void onNoteClick(int position);
    }

}
