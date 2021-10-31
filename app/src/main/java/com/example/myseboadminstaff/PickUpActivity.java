package com.example.myseboadminstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myseboadminstaff.databinding.ActivityPickUpBinding;
import com.google.android.material.textfield.TextInputEditText;

public class PickUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityPickUpBinding activityPickUpBinding;

    private static final String TAG = "Report";

    private TextInputEditText tietName;
    private TextInputEditText tietPickDate;
    private TextInputEditText tietStaffID;
    private TextInputEditText tietPhone;

    private Button btnSubmit;
    private String reservationId;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPickUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_up);

        reservationId = getIntent().getStringExtra("reservationId");
        tietName = activityPickUpBinding.tietName;
        tietPickDate = activityPickUpBinding.tietPickDate;
        tietStaffID = activityPickUpBinding.tietStaffId;
        tietPhone = activityPickUpBinding.tietPhone;

        btnSubmit = activityPickUpBinding.btnSubmit;

        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            String name =  tietName.getText().toString();
            String pickUpDate = tietPickDate.getText().toString();
            String staffId = tietStaffID.getText().toString();
            String phone = tietPhone.getText().toString();

            firebaseHelper.pickupReservation(reservationId,name, staffId,phone);

        }
    }

}