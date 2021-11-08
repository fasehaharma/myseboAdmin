package com.example.myseboadminstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.myseboadminstaff.databinding.ActivityPickUpBinding;
import com.example.myseboadminstaff.databinding.ActivityReturnBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class ReturnActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private ActivityReturnBinding activityReturnBinding;

    private static final String TAG = "Report";

    private TextInputEditText tietReturnName;
    private TextInputEditText tietReturnDate;
    private TextInputEditText tietReturnStaffID;
    private TextInputEditText tietReturnPhone;
    private TextInputEditText tietReturnNote;
    private DatePickerDialog datePickerDialog;

    private Button btnSubmit;
    private String reservationId;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityReturnBinding = DataBindingUtil.setContentView(this, R.layout.activity_return);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(
                this, this, currentYear, currentMonth, currentDay);

        reservationId = getIntent().getStringExtra("reservationId");
        tietReturnName = activityReturnBinding.tietReturnName;
        tietReturnDate = activityReturnBinding.tietReturnDate;
        tietReturnStaffID = activityReturnBinding.tietStaffId;
        tietReturnPhone = activityReturnBinding.tietPhone;
        tietReturnNote = activityReturnBinding.tietReturnNote;

        btnSubmit = activityReturnBinding.btnSubmit;

        btnSubmit.setOnClickListener(this);

        tietReturnDate.setInputType(InputType.TYPE_NULL);
        tietReturnDate.setKeyListener(null);

        tietReturnDate.setOnClickListener(new View.OnClickListener()
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
            String name =  tietReturnName.getText().toString();
            String returnDate = tietReturnDate.getText().toString();
            String staffId = tietReturnStaffID.getText().toString();
            String phone = tietReturnPhone.getText().toString();
            String note = tietReturnNote.getText().toString();

            firebaseHelper.returnReservation(reservationId,name, staffId,phone, note, returnDate);
            finish();

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String sReturnDate = year + "-" + month + "-" + dayOfMonth;
        tietReturnDate.setText(sReturnDate);
    }
}