package com.example.myseboadminstaff.asset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myseboadminstaff.FirebaseHelper;
import com.example.myseboadminstaff.R;
import com.example.myseboadminstaff.databinding.ActivityAddAssetBinding;
import com.example.myseboadminstaff.databinding.ActivityEditAssetBinding;

public class EditAssetActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnEdit;
    private EditText etItem;
    private EditText etQuantity;

    private String sItem;
    private String sId;
    private int iQuantity;
    private String reservationId;
    private int type;

    private ActivityEditAssetBinding activityEditAssetBinding;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEditAssetBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_asset);

        reservationId = getIntent().getStringExtra("reservationId");
        type = getIntent().getIntExtra("type", 0);

        btnEdit = activityEditAssetBinding.btnEdit;
        etItem = activityEditAssetBinding.etItem;
        etQuantity = activityEditAssetBinding.etQuantity;

        btnEdit.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();

        sItem = extras.getString("item");
        sId = extras.getString("id");
        iQuantity = extras.getInt("quantity");

        etItem.setText(sItem);
        etQuantity.setText(String.valueOf(iQuantity));


    }


    @Override
    public void onClick(View v) {
        if (v == btnEdit){

            if (type == AssetAdapter.TYPE_EDIT_ASSET){
                firebaseHelper.editAsset(sId, etItem.getText().toString(), Integer.parseInt(etQuantity.getText().toString()));
                finish();
            } else if ( type == AssetAdapter.TYPE_EDIT_RESERVATION){
                firebaseHelper.editAssetReservation(sId, etItem.getText().toString(), Integer.parseInt(etQuantity.getText().toString()), reservationId, iQuantity);
                finish();
            }

        }
    }
}