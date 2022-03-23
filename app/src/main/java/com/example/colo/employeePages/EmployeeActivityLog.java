package com.example.colo.employeePages;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.colo.R;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeActivityLog extends AppCompatActivity {

    TableLayout hours_per_day_table;
    EditText elSu, elMo, elTu, elWe, elTh, elFr, elSa, elTot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_log);

        elSu = (EditText) findViewById(R.id.sunday_hours);
        elMo = (EditText) findViewById(R.id.monday_hours);
        elTu = (EditText) findViewById(R.id.tuesday_hours);
        elWe = (EditText) findViewById(R.id.wednesday_hours);
        elTh = (EditText) findViewById(R.id.thursday_hours);
        elFr = (EditText) findViewById(R.id.friday_hours);
        elSa = (EditText) findViewById(R.id.saturday_hours);
        elTot = (EditText) findViewById(R.id.act_week_total);
        elTot.setText(Integer.toString(Integer.valueOf(elSu.getText().toString()) + Integer.valueOf(elMo.getText().toString())
                + Integer.valueOf(elTu.getText().toString()) + Integer.valueOf(elWe.getText().toString()) + Integer.valueOf(elTh.getText().toString())
                + Integer.valueOf(elFr.getText().toString()) + Integer.valueOf(elSa.getText().toString())));

    }
}