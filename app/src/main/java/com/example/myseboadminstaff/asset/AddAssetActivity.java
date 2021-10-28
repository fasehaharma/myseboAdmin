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
import com.example.myseboadminstaff.databinding.ActivityMainBinding;

public class AddAssetActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAdd;
    private EditText etItem;
    private EditText etQuantity;

    private ActivityAddAssetBinding activityAddAssetBinding;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAddAssetBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_asset);

        btnAdd = activityAddAssetBinding.btnAdd;
        etItem = activityAddAssetBinding.etItem;
        etQuantity = activityAddAssetBinding.etQuantity;


        btnAdd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnAdd){
            String itemName = etItem.getText().toString();
            int quantity = Integer.parseInt(etQuantity.getText().toString());
            firebaseHelper.addAsset(itemName, quantity);
            finish();
        }
    }
}