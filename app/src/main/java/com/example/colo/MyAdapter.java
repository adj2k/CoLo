package com.example.colo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Announcements> AnnouncementsList;


    public MyAdapter(Context context, ArrayList<Announcements> AnnouncementsList) {
        this.context = context;
        this.AnnouncementsList = AnnouncementsList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Announcements Announcement = AnnouncementsList.get(position);
        holder.titleOutput.setText(Announcement.getAnnouncementTitle());
        holder.descriptionOutput.setText(Announcement.getDescription());

       String formattedTime = DateFormat.getDateTimeInstance().format(Announcement.createdTime);
        holder.timeOutput.setText(formattedTime);
    }

    @Override
    public int getItemCount() {
       return AnnouncementsList.size();
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
}
