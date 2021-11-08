package com.example.myseboadminstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myseboadminstaff.databinding.ActivityPickUpBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class PickUpActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private ActivityPickUpBinding activityPickUpBinding;

    private static final String TAG = "Report";

    private TextInputEditText tietName;
    private TextInputEditText tietPickDate;
    private TextInputEditText tietStaffID;
    private TextInputEditText tietPhone;

    private Button btnSubmit;
    private String reservationId;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPickUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_up);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(
                this, this, currentYear, currentMonth, currentDay);

        reservationId = getIntent().getStringExtra("reservationId");
        tietName = activityPickUpBinding.tietName;
        tietPickDate = activityPickUpBinding.tietPickDate;
        tietStaffID = activityPickUpBinding.tietStaffId;
        tietPhone = activityPickUpBinding.tietPhone;

        btnSubmit = activityPickUpBinding.btnSubmit;

        tietPickDate.setInputType(InputType.TYPE_NULL);
        tietPickDate.setKeyListener(null);

        tietPickDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datePickerDialog.show();
            }
        });


        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            String name =  tietName.getText().toString();
            String pickUpDate = tietPickDate.getText().toString();
            String staffId = tietStaffID.getText().toString();
            String phone = tietPhone.getText().toString();

            firebaseHelper.pickupReservation(reservationId,name, staffId,phone, pickUpDate);
            finish();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String sPickupDate = year + "-" + month + "-" + dayOfMonth;
        tietPickDate.setText(sPickupDate);
    }
}