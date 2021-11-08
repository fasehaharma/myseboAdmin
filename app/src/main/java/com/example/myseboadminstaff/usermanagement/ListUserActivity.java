package com.example.myseboadminstaff.usermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.myseboadminstaff.FirebaseHelper;
import com.example.myseboadminstaff.R;
import com.example.myseboadminstaff.databinding.ActivityListUserBinding;
import com.example.myseboadminstaff.usermanagement.UserAdapter;

import java.util.List;

public class ListUserActivity extends AppCompatActivity {

    private ActivityListUserBinding activityListUserBinding;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    private RecyclerView rvListUser;

    private UserAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityListUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_list_user);

        rvListUser = activityListUserBinding.rvListUser;

        userAdapter = new UserAdapter();

        userAdapter.setOnUserAdapterCallBack(new UserAdapter.OnUserAdapterCallBack() {
            @Override
            public void onUserAdapterCallBack(User user) {
                Intent intent = new Intent(ListUserActivity.this,UserDetailActivity.class);
                intent.putExtra("userId",user.getId());

                startActivity(intent);
            }
        });

        firebaseHelper.getUserList();
        firebaseHelper.getUserListMutableLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userAdapter.setUserList(users);
                userAdapter.notifyDataSetChanged();
            }
        });

        rvListUser.setAdapter(userAdapter);
        rvListUser.setLayoutManager(new LinearLayoutManager(this));


    }
}