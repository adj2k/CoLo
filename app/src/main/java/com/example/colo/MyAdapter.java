package com.example.colo;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<CurrentAnnouncement> list;


    public MyAdapter(Context context, ArrayList<CurrentAnnouncement> list) {
        list = new ArrayList<>();
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,true);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CurrentAnnouncement announcement = list.get(position);
        holder.titleOutput.setText(announcement.getTitle());
        holder.descriptionOutput.setText(announcement.getDescription());

       String formattedTime = DateFormat.getDateTimeInstance().format(announcement.createdTime);
       holder.timeOutput.setText(formattedTime);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleOutput;
        TextView descriptionOutput;
        TextView timeOutput;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOutput = itemView.findViewById(R.id.titleOutput);
            descriptionOutput = itemView.findViewById(R.id.descriptionOutput);
            timeOutput = itemView.findViewById(R.id.timeOutput);

        }
    }

    @Override
    public int getItemCount() {
        Log.d("ListSize: ", String.valueOf(list.size()));
        return list.size();
    }
}
