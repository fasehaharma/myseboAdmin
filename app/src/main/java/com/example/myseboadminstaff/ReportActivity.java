package com.example.myseboadminstaff;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myseboadminstaff.databinding.ActivityListReservationBinding;
import com.example.myseboadminstaff.databinding.ActivityReportBinding;
import com.example.myseboadminstaff.reservation.Reservation;
import com.example.myseboadminstaff.reservation.ReservationAdapter;

import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private ActivityReportBinding activityReportBinding;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    private RecyclerView rvReservationList;

    private ReservationAdapter reservationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityReportBinding = DataBindingUtil.setContentView(this, R.layout.activity_report);

        rvReservationList = activityReportBinding.rvReservation;

        reservationAdapter = new ReservationAdapter();

        rvReservationList.setAdapter(reservationAdapter);
        rvReservationList.setLayoutManager(new LinearLayoutManager(this));

        firebaseHelper.readAcceptedReservationList();

        firebaseHelper.getReservationListMutableLiveData().observe(this, new Observer<List<Reservation>>() {
            @Override
            public void onChanged(List<Reservation> reservations) {
                Log.d("asset", "onChanged: " + reservations.size());
                reservationAdapter.setReservationList(reservations);
                reservationAdapter.notifyDataSetChanged();
            }
        });
    }
}