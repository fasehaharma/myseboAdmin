package com.example.myseboadminstaff.reservation;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myseboadminstaff.FirebaseHelper;
import com.example.myseboadminstaff.R;
import com.example.myseboadminstaff.asset.AssetAdapter;
import com.example.myseboadminstaff.databinding.ActivityEditAssetBinding;
import com.example.myseboadminstaff.databinding.ActivityListReservationBinding;
import com.example.myseboadminstaff.reservation.Reservation;

import java.util.List;

public class ListReservationActivity extends AppCompatActivity {

    private ActivityListReservationBinding activityListReservationBinding;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    private RecyclerView rvReservationList;

    private ReservationAdapter reservationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityListReservationBinding = DataBindingUtil.setContentView(this, R.layout.activity_list_reservation);

        rvReservationList = activityListReservationBinding.rvListReservation;

        reservationAdapter = new ReservationAdapter();

        rvReservationList.setAdapter(reservationAdapter);
        rvReservationList.setLayoutManager(new LinearLayoutManager(this));

        firebaseHelper.readReservationList();

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