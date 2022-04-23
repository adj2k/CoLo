package com.example.colo.ViewEmployeesFolder;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colo.ShowTimesDialog;
import com.example.colo.ViewEmployeesFolder.ViewEmployeeAdapter;
import com.example.colo.ViewEmployeesFolder.EmployeeList;
import com.example.colo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewEmployeeAdapter extends RecyclerView.Adapter<ViewEmployeeAdapter.MyViewHolder> {

    // This is the adapter needed to inflate (load the data into) the RecyclerView
    // RecyclerView is used to generate a scrollable list of data, "recycling" the same item layout
    // In this case, the item layout is res/layout/announcement_view.xml

    Context context;
    ArrayList<EmployeeList> list;
    DatabaseReference ref;


    public ViewEmployeeAdapter(Context context, ArrayList<EmployeeList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewEmployeeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.employee_view_item,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewEmployeeAdapter.MyViewHolder holder, int position) {

        EmployeeList employee = list.get(position);
        holder.name.setText(employee.getName());
        holder.ID.setText(employee.getEmployeeID());
        holder.email.setText(employee.getEmail());
        holder.position = position;
        holder.context = this.context;
        holder.list = this.list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // Here we setup the variables to assign to the different textItems in the announcement_item.xml
        TextView name, ID, email;
        int position;
        ArrayList<EmployeeList> list;
        Context context;
        DatabaseReference ref;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.employeeNameText);
            ID = itemView.findViewById(R.id.employeeIDtext);
            email = itemView.findViewById(R.id.employeeEmailtext);


            itemView.findViewById(R.id.employeeDeleteButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO Make dialog to as if sure to remove manager/employee
                    ref = FirebaseDatabase.getInstance().getReference("Companies/" +
                                                                        list.get(position).getCompanyName() + '/' +
                                                                        list.get(position).getFirebaseId());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

            itemView.findViewById(R.id.employeeEditButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(list.get(position).getFirebaseId());
                }
            });

            // displays employee's timesheet
            itemView.findViewById(R.id.displayTime).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    if(list.get(position).getRole().equals("Employee")) {
                        ShowTimesDialog showTimesDialog = new ShowTimesDialog(list.get(position).getFirebaseId(), list.get(position).getCompanyName());
                        showTimesDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "TEST");
                    }
                }
            });

        }
    }

}