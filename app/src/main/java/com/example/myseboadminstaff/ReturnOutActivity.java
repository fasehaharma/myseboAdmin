package com.example.myseboadminstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myseboadminstaff.asset.AssetAdapter;
import com.example.myseboadminstaff.databinding.ActivityReservationDetailBinding;
import com.example.myseboadminstaff.databinding.ActivityReturnOutBinding;
import com.example.myseboadminstaff.reservation.Reservation;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReturnOutActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPickUp;
    private Button btnReturn;

    private TextView tvEvent;
    private TextView tvOrganization;
    private TextView tvPhone;
    private TextView tvStatus;

    private RecyclerView rvListItem;

    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private ActivityReturnOutBinding activityReturnOutBinding;
    private AssetAdapter assetAdapter;
    private String reservationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityReturnOutBinding = DataBindingUtil.setContentView(this, R.layout.activity_return_out);


        reservationId = getIntent().getStringExtra("reservationId");

        btnPickUp = activityReturnOutBinding.btnPickUp;
        btnReturn = activityReturnOutBinding.btnReturn;
        rvListItem = activityReturnOutBinding.rvListItem;
        tvStatus = activityReturnOutBinding.tvStatus;


        btnPickUp.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        assetAdapter = new AssetAdapter();
        assetAdapter.setType(AssetAdapter.TYPE_EDIT_RESERVATION);


        firebaseHelper.readReservationDetail(reservationId);
        firebaseHelper.getReservationMutableLiveData().observe(this, new Observer<Reservation>() {
            @Override
            public void onChanged(Reservation reservation) {
                assetAdapter.setAssetList(reservation.getEquipment());
                assetAdapter.notifyDataSetChanged();
                assetAdapter.setReservationId(reservationId);

                activityReturnOutBinding.tvEvent.setText(reservation.getEventName());
                activityReturnOutBinding.tvOrganization.setText(reservation.getEventOrganization());
                activityReturnOutBinding.tvPhone.setText(reservation.getPhoneNumber());

                final Timestamp reserveDate = reservation.getDateStart();
                final Date date = reserveDate.toDate();
                String dateString = new SimpleDateFormat("dd/MM/yy").format(date);

                final Timestamp returnDate = reservation.getDateEnd();
                final Date rdate = returnDate.toDate();
                String rdateString = new SimpleDateFormat("dd/MM/yy").format(rdate);

                activityReturnOutBinding.tvReturnDate.setText(rdateString);
                activityReturnOutBinding.tvBorrowingDate.setText(dateString);

                if (reservation.getStatus() == 1){
                    tvStatus.setText("REJECT");
                } else if (reservation.getStatus() == 2){
                    tvStatus.setText("ACCEPT");
                } else {
                    tvStatus.setText("PENDING");
                }

            }
        });


        rvListItem.setAdapter(assetAdapter);
        rvListItem.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseHelper.readReservationDetail(reservationId);
    }

    @Override
    public void onClick(View v) {
        if (v == btnPickUp) {

            firebaseHelper.acceptReservation (reservationId);
            finish();

        } else if (v == btnReturn){

            firebaseHelper.rejectReservation (reservationId);
            finish();
        }

    }
}