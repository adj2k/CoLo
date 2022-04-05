package com.example.colo.Announcements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colo.R;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.MyViewHolder> {

    // This is the adapter needed to inflate (load the data into) the RecyclerView
    // RecyclerView is used to generate a scrollable list of data, "recycling" the same item layout
    // In this case, the item layout is res/layout/announcement_view.xml

    Context context;
    ArrayList<AnnouncementList> list;

    public AnnouncementAdapter(Context context, ArrayList<AnnouncementList> list) {
        this.context = context;
        this.list = list;
    }

    // This function inflates the view with the pre-made announcement layout
    @NonNull
    @Override
    public AnnouncementAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.announcement_view,parent, false);
        return new MyViewHolder(v);
    }

    // Gets the current announcement and sets it's title and description
    @Override
    public void onBindViewHolder(@NonNull AnnouncementAdapter.MyViewHolder holder, int position) {

        AnnouncementList announcement = list.get(position);
        holder.title.setText(announcement.getaTitle());
        holder.description.setText(announcement.getaDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // This function sets the database information to the corresponding XML data
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // Here we setup the variables to assign to the different textItems in the announcement_item.xml
        TextView title, description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.announcement_title);
            description = itemView.findViewById(R.id.announcement_description);

        }
    }


}
