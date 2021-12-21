package com.example.myseboadminstaff.reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myseboadminstaff.FirebaseHelper;
import com.example.myseboadminstaff.R;
import com.example.myseboadminstaff.asset.AssetAdapter;
import com.example.myseboadminstaff.databinding.ActivityAssetBinding;
import com.example.myseboadminstaff.databinding.ActivityReservationDetailBinding;
import com.google.firebase.Timestamp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservationDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAccept;
    private Button btnReject;

    private TextView tvEvent;
    private TextView tvOrganization;
    private TextView tvPhone;
    private TextView tvStatus;
    private TextView tvBorrowingDate;
    private TextView tvReturnDate;
    private TextView tvPDFLetter;
    private TextView tvPDFID;
    private RecyclerView rvListItem;


    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private ActivityReservationDetailBinding activityReservationDetailBinding;
    private AssetAdapter assetAdapter;
    private String reservationId;
    private Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReservationDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_reservation_detail);


        reservationId = getIntent().getStringExtra("reservationId");

        btnAccept = activityReservationDetailBinding.btnAccept;
        btnReject = activityReservationDetailBinding.btnReject;
        rvListItem = activityReservationDetailBinding.rvListItem;
        tvStatus = activityReservationDetailBinding.tvStatus;
        tvPDFLetter = activityReservationDetailBinding.tvPDFLetter;
        tvPDFID = activityReservationDetailBinding.tvPDFID;


        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        tvPDFLetter.setOnClickListener(this);
        tvPDFID.setOnClickListener(this);

        assetAdapter = new AssetAdapter();
        assetAdapter.setType(AssetAdapter.TYPE_EDIT_RESERVATION);


        firebaseHelper.readReservationDetail(reservationId);
        firebaseHelper.getReservationMutableLiveData().observe(this, new Observer<Reservation>() {
            @Override
            public void onChanged(Reservation reservation) {
                ReservationDetailActivity.this.reservation = reservation;

                assetAdapter.setAssetList(reservation.getEquipment());
                assetAdapter.notifyDataSetChanged();
                assetAdapter.setReservationId(reservationId);

                activityReservationDetailBinding.tvEvent.setText(reservation.getEventName());
                activityReservationDetailBinding.tvOrganization.setText(reservation.getEventOrganization());
                activityReservationDetailBinding.tvPhone.setText(reservation.getPhoneNumber());

                final Timestamp reserveDate = reservation.getDateStart();
                final Date date = reserveDate.toDate();
                String dateString = new SimpleDateFormat("dd/MM/yy").format(date);

                final Timestamp returnDate = reservation.getDateEnd();
                final Date rdate = returnDate.toDate();
                String rdateString = new SimpleDateFormat("dd/MM/yy").format(rdate);

                activityReservationDetailBinding.tvReturnDate.setText(rdateString);
                activityReservationDetailBinding.tvBorrowingDate.setText(dateString);

                if (reservation.getStatus() == Reservation.STATUS_REJECT){
                    tvStatus.setText("REJECT");
                } else if (reservation.getStatus() == Reservation.STATUS_ACCEPT){
                    tvStatus.setText("ACCEPT");
                } else if (reservation.getStatus() == Reservation.STATUS_PENDING){
                    tvStatus.setText("PENDING");
                } else if (reservation.getStatus() == Reservation.STATUS_PICKUP){
                    tvStatus.setText("PICK-UP");
                } else if (reservation.getStatus() == Reservation.STATUS_RETURN){
                    tvStatus.setText("RETURN");
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
        if (v == btnAccept) {

            firebaseHelper.acceptReservation (reservationId);
            finish();

        } else if (v == btnReject){

            firebaseHelper.rejectReservation (reservationId);
            finish();

        } else if (v == tvPDFLetter) {

            try {
                if (reservation != null) {
                    firebaseHelper.viewPDFLetter(reservation, this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (v == tvPDFID){

            try {
                if (reservation != null) {
                    firebaseHelper.viewPDFID(reservation, this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}