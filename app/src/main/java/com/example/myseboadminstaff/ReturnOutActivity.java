package com.example.myseboadminstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

                activityReturnOutBinding.tvPickUpBy.setText(reservation.getPickUpName());
                activityReturnOutBinding.tvPickUpPhoneNumber.setText(reservation.getPickUpPhone());
                activityReturnOutBinding.tvID.setText(reservation.getPickUpId());

                activityReturnOutBinding.tvReturnBy.setText(reservation.getReturnName());
                activityReturnOutBinding.tvReturnPhoneNumber.setText(reservation.getReturnPhone());
                activityReturnOutBinding.tvReturnIDStaff.setText(reservation.getReturnIdStaff());
                activityReturnOutBinding.tvReturnNote.setText(reservation.getNote());

                

                final Timestamp reserveDate = reservation.getDateStart();
                final Date date = reserveDate.toDate();
                String dateString = new SimpleDateFormat("dd/MM/yy").format(date);

                final Timestamp returnDate = reservation.getDateEnd();
                final Date rdate = returnDate.toDate();
                String rdateString = new SimpleDateFormat("dd/MM/yy").format(rdate);

                activityReturnOutBinding.tvReturnDate.setText(rdateString);
                activityReturnOutBinding.tvBorrowingDate.setText(dateString);

                if (reservation.getStatus() == Reservation.STATUS_REJECT){
                    tvStatus.setText("REJECT");
                } else if (reservation.getStatus() == Reservation.STATUS_ACCEPT){
                    tvStatus.setText("ACCEPT");
                } else if(reservation.getStatus() == Reservation.STATUS_PENDING){
                    tvStatus.setText("PENDING");
                } else if(reservation.getStatus() == Reservation.STATUS_PICKUP){
                    tvStatus.setText("PICKUP");
                    activityReturnOutBinding.lnPickupDetailContainer.setVisibility(View.VISIBLE);
                } else if (reservation.getStatus() == Reservation.STATUS_RETURN){
                    tvStatus.setText("RETURN");
                    activityReturnOutBinding.lnReturnDetailContainer.setVisibility(View.VISIBLE);
                }

            }
        });


        btnPickUp.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

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

            Intent intent = new Intent(getApplicationContext(), PickUpActivity.class);
            intent.putExtra("reservationId",reservationId);
            startActivity(intent);

        } else if (v == btnReturn){

            Intent intent = new Intent(getApplicationContext(), ReturnActivity.class);
            intent.putExtra("reservationId",reservationId);
            startActivity(intent);
        }

    }
}