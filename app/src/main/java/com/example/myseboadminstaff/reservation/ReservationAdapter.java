package com.example.myseboadminstaff.reservation;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myseboadminstaff.asset.AssetAdapter;
import com.example.myseboadminstaff.databinding.ItemAssetBinding;
import com.example.myseboadminstaff.databinding.ItemReservationBinding;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder>  {
    private List<Reservation> reservations = new ArrayList<>();
    private OnReservationAdapterCallBack onReservationAdapterCallBack;

    @NonNull
    @Override
    public ReservationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemReservationBinding itemReservationBinding = ItemReservationBinding.inflate(layoutInflater, parent, false);
        return new ReservationAdapter.ViewHolder(itemReservationBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationAdapter.ViewHolder holder, int position) {
        holder.bind(reservations.get(holder.getAdapterPosition()));
    }

    public void setOnReservationAdapterCallBack(OnReservationAdapterCallBack onReservationAdapterCallBack) {
        this.onReservationAdapterCallBack = onReservationAdapterCallBack;
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public interface  OnReservationAdapterCallBack{
        void onReservationCallBack(Reservation reservation);
    }

    public void setReservationList(List<Reservation> reservations) {
        this.reservations = reservations;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemReservationBinding binding;

        public ViewHolder(@NonNull ItemReservationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  onReservationAdapterCallBack.onReservationCallBack(reservations.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Reservation reservation) {
            binding.setEquipment(reservation);

            if (reservation.getStatus() == Reservation.STATUS_REJECT){
                binding.tvId.setTextColor(Color.RED);
                binding.tvEmail.setTextColor(Color.RED);
            } else if (reservation.getStatus() == Reservation.STATUS_ACCEPT){
                binding.tvId.setTextColor(Color.GREEN);
                binding.tvEmail.setTextColor(Color.GREEN);
            } else {
                binding.tvId.setTextColor(Color.BLACK);
                binding.tvEmail.setTextColor(Color.BLACK);
            }
        }
    }
}
