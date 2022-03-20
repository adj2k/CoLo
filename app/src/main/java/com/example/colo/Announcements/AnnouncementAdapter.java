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

    Context context;
    private OnNoteListener aOnNoteListener;

    ArrayList<AnnouncementList> list;

    public AnnouncementAdapter(Context context, ArrayList<AnnouncementList> list, OnNoteListener aOnNoteListener) {
        this.context = context;
        this.list = list;
        this.aOnNoteListener = aOnNoteListener;
    }

    @NonNull
    @Override
    public AnnouncementAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.announcement_view,parent, false);
        return new MyViewHolder(v, aOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementAdapter.MyViewHolder holder, int position) {

        AnnouncementList announcement = list.get(position);
        holder.title.setText(announcement.getTitle());
        holder.description.setText(announcement.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, description;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            title = itemView.findViewById(R.id.announcement_title);
            description = itemView.findViewById(R.id.announcement_description);
            this.onNoteListener = onNoteListener;

           // itemView.setOnClickListener((View.OnClickListener) this);

        }

        /*@Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }*/
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }
}
