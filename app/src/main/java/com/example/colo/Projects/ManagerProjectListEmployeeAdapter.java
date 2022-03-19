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
    private OnNoteListener mOnNoteListener;

    ArrayList<ManagerProjectListEmployeeData> list;

    public ManagerProjectListEmployeeAdapter(Context context, ArrayList<ManagerProjectListEmployeeData> list, OnNoteListener mOnNoteListener) {
        this.context = context;
        this.list = list;
        this.mOnNoteListener = mOnNoteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.employee,parent, false);
        return new MyViewHolder(v, mOnNoteListener);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            name = itemView.findViewById(R.id.employee_name);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    // remove selected employees
    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }
}
