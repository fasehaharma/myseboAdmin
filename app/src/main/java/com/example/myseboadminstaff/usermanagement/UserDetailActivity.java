package com.example.myseboadminstaff.usermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myseboadminstaff.FirebaseHelper;
import com.example.myseboadminstaff.R;
import com.example.myseboadminstaff.databinding.ActivityUserDetailBinding;
import com.example.myseboadminstaff.usermanagement.UserAdapter;

import java.util.List;

public class UserDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAccept;
    private Button btnReject;

    private TextView tvName;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvStaffId;
    private TextView tvId;
    private TextView tvStatus;


    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private ActivityUserDetailBinding activityUserDetailBinding;
    private UserAdapter userAdapter;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityUserDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail);


        userId = getIntent().getStringExtra("userId");
        firebaseHelper.getUserDetail(userId);
        firebaseHelper.getUserDetailMutableLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                activityUserDetailBinding.tvName.setText(user.getName());
                activityUserDetailBinding.tvEmail.setText(user.getEmail());
                activityUserDetailBinding.tvPhone.setText(user.getPhone());
                activityUserDetailBinding.tvStaffID.setText(user.getStaffId());
                activityUserDetailBinding.tvID.setText(user.getId());
                activityUserDetailBinding.tvStatus.setText(String.valueOf(user.getVerify()));

            }
        });

        btnAccept = activityUserDetailBinding.btnAccept;
        btnReject = activityUserDetailBinding.btnReject;



        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnAccept){
            firebaseHelper.acceptAdmin(userId);
            finish();
        }else if (v == btnReject){
            firebaseHelper.rejectAdmin(userId);
            finish();
        }
    }
}