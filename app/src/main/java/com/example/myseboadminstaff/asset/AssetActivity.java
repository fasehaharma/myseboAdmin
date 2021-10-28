package com.example.myseboadminstaff.asset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myseboadminstaff.FirebaseHelper;
import com.example.myseboadminstaff.R;
import com.example.myseboadminstaff.databinding.ActivityAssetBinding;

import java.util.List;

public class AssetActivity extends AppCompatActivity  implements View.OnClickListener{

    private Button btnAdd;

    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private ActivityAssetBinding activityAssetBinding;
    private AssetAdapter assetAdapter;
    private RecyclerView rvItemAsset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAssetBinding = DataBindingUtil.setContentView(this, R.layout.activity_asset);

        rvItemAsset = activityAssetBinding.rvItemAsset;

        btnAdd = activityAssetBinding.btnAdd;

        btnAdd.setOnClickListener(this);

        assetAdapter = new AssetAdapter();

        rvItemAsset.setAdapter(assetAdapter);
        rvItemAsset.setLayoutManager(new LinearLayoutManager(this));


            firebaseHelper.getAssetListMutableLiveData().observe(this, new Observer<List<Asset>>() {
            @Override
            public void onChanged(List<Asset> assets) {
                Log.d("asset", "onChanged: " + assets.size());
                assetAdapter.setAssetList(assets);
                assetAdapter.notifyDataSetChanged();

            }
        });
       firebaseHelper.readAssetList();

        }

    public void onClick(View v) {
        if (v == btnAdd) {
            Intent intent = new Intent(getApplicationContext(), AddAssetActivity.class);
            startActivity(intent);
        }
    }
}