package com.example.colo.Projects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colo.R;

import java.util.ArrayList;

public class ProjectEmployeeAdapter extends RecyclerView.Adapter<ProjectEmployeeAdapter.ViewHolder> {

    View view;
    Context context;
    ArrayList<ManagerProjectListEmployeeData> list;
    ArrayList<String> EmployeeUIDS;

    ArrayList<String> returnUIDS = new ArrayList<>();

    QuantityListener quantityListener;

    public ProjectEmployeeAdapter(Context context, ArrayList<ManagerProjectListEmployeeData> list, ArrayList<String> UIDList, QuantityListener quantityListener) {
        this.context = context;
        this.list = list;
        this.EmployeeUIDS = UIDList;
        this.quantityListener = quantityListener;
    }

    public  View getView() {
        return view;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.employee_check_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(list != null && list.size() > 0) {
            ManagerProjectListEmployeeData user = list.get(position);
            holder.employee_checkbox.setText(user.getUserName());
            holder.employee_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.employee_checkbox.isChecked()) {
                        returnUIDS.add(EmployeeUIDS.get(position));
                    } else {
                        returnUIDS.remove(EmployeeUIDS.get(position));
                    }
                    quantityListener.onQuantityChange(returnUIDS);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox employee_checkbox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            employee_checkbox = itemView.findViewById(R.id.employee_checkbox);
        }
    }
}
