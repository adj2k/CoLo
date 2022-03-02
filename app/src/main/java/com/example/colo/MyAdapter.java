package com.example.colo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    //RealmResults<Note> notesList;

    /*
    public MyAdapter(Context context, RealmResults<note> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

     */

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Note note = notesList.get(position);
        //holder.titleOutput.setText(note.getTitle());
        //holder.descriptionOutput.setText(note.getOutput());

       //String formattedTime = DateFormat.getDateTimeInstance().format(note.createdTime);
        //holder.timeOutput.setText(formattedTime));
    }

    @Override
    public int getItemCount() {
       //return notesList;
        return 0;
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
