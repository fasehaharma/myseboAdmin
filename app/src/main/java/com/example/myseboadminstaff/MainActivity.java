package com.example.myseboadminstaff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.myseboadminstaff.asset.AssetActivity;
import com.example.myseboadminstaff.databinding.ActivityMainBinding;
import com.example.myseboadminstaff.reservation.ListReservationActivity;
import com.example.myseboadminstaff.usermanagement.ListUserActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ActivityMainBinding activityMainBinding;

    private Button btnListReservation;
    private Button btnReport;
    private Button btnLogout;
    private Button btnAsset;
    private Button btnUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        btnListReservation = activityMainBinding.btnListReservation;
        btnReport = activityMainBinding.btnViewReport;
        btnLogout = activityMainBinding.btnLogout;
        btnAsset = activityMainBinding.btnAsset;
        btnUser = activityMainBinding.btnUser;


        btnListReservation.setOnClickListener(this);
        btnReport.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnAsset.setOnClickListener(this);
        btnUser.setOnClickListener(this);

    }

    public void logout() {
        FirebaseAuth.getInstance().signOut(); //logout

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogout) {
            logout();
        } else if (v == btnListReservation) {
            Intent intent = new Intent(getApplicationContext(), ListReservationActivity.class);
            startActivity(intent);
        } else if (v == btnReport) {
            Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
            startActivity(intent);
        } else if (v == btnAsset) {
            Intent intent = new Intent(getApplicationContext(), AssetActivity.class);
            startActivity(intent);
        } else if (v == btnUser){
            Intent intent = new Intent(getApplicationContext(), ListUserActivity.class);
            startActivity(intent);
        }

    }
}
