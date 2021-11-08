package com.example.myseboadminstaff.usermanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.CircularArray;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myseboadminstaff.databinding.ItemReservationBinding;
import com.example.myseboadminstaff.databinding.ItemUserBinding;
import com.example.myseboadminstaff.reservation.ReservationAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private OnUserAdapterCallBack onUserAdapterCallBack;
    private List<User> userList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemUserBinding itemUserBinding = ItemUserBinding.inflate(layoutInflater, parent, false);
        return new UserAdapter.ViewHolder(itemUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(userList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void setOnUserAdapterCallBack(OnUserAdapterCallBack onUserAdapterCallBack) {
        this.onUserAdapterCallBack = onUserAdapterCallBack;
    }

    public interface OnUserAdapterCallBack{
        void onUserAdapterCallBack(User user);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding binding;

        public ViewHolder(@NonNull ItemUserBinding binding) {

            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserAdapterCallBack.onUserAdapterCallBack(userList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(User user) {
            binding.setUser(user);
        }
    }
}
