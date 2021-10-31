package com.example.myseboadminstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myseboadminstaff.databinding.ActivityPickUpBinding;
import com.example.myseboadminstaff.databinding.ActivityReturnBinding;
import com.google.android.material.textfield.TextInputEditText;

public class ReturnActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityReturnBinding activityReturnBinding;

    private static final String TAG = "Report";

    private TextInputEditText tietReturnName;
    private TextInputEditText tietReturnDate;
    private TextInputEditText tietReturnStaffID;
    private TextInputEditText tietReturnPhone;
    private TextInputEditText tietReturnNote;

    private Button btnSubmit;
    private String reservationId;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityReturnBinding = DataBindingUtil.setContentView(this, R.layout.activity_return);

        reservationId = getIntent().getStringExtra("reservationId");
        tietReturnName = activityReturnBinding.tietReturnName;
        tietReturnDate = activityReturnBinding.tietReturnDate;
        tietReturnStaffID = activityReturnBinding.tietStaffId;
        tietReturnPhone = activityReturnBinding.tietPhone;
        tietReturnNote = activityReturnBinding.tietReturnNote;

        btnSubmit = activityReturnBinding.btnSubmit;

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            String name =  tietReturnName.getText().toString();
            String returnDate = tietReturnDate.getText().toString();
            String staffId = tietReturnStaffID.getText().toString();
            String phone = tietReturnPhone.getText().toString();
            String note = tietReturnNote.getText().toString();

            firebaseHelper.pickupReservation(reservationId,name, staffId,phone);

        }
    }
}