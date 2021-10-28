package com.example.myseboadminstaff.asset;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myseboadminstaff.FirebaseHelper;
import com.example.myseboadminstaff.databinding.ItemAssetBinding;

import java.util.ArrayList;
import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {
    private List<Asset> assetList = new ArrayList<>();

    private int type = TYPE_EDIT_ASSET;

    public static final int TYPE_EDIT_ASSET = 1001;
    public static final int TYPE_EDIT_RESERVATION = 1002;
    private String reservationId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemAssetBinding itemBinding = ItemAssetBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(assetList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    public void setAssetList(List<Asset> assetList) {
        this.assetList = assetList;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private FirebaseHelper firebaseHelper = new FirebaseHelper();

        private ItemAssetBinding binding;
        private TextView tvEdit;
        private TextView tvDelete;



        public ViewHolder(ItemAssetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.tvDelete = binding.tvDelete;
            this.tvEdit = binding.tvEdit;


            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    Asset asset = assetList.get(adapterPosition);


                    Intent intent = new Intent(v.getContext(), EditAssetActivity.class);
                    intent.putExtra("id", asset.getId());
                    intent.putExtra("item", asset.getItem());
                    intent.putExtra("quantity", asset.getQuantity());
                    intent.putExtra("date", asset.getDate());
                    intent.putExtra("type",type);
                    intent.putExtra("reservationId", reservationId);

                    v.getContext().startActivity(intent);
                }
            });

            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    Asset asset = assetList.get(adapterPosition);

                    if (type == AssetAdapter.TYPE_EDIT_RESERVATION){

                        firebaseHelper.deleteReservationAsset(reservationId, asset);
                        assetList.remove(asset);
                        notifyDataSetChanged();

                    } else if (type == AssetAdapter.TYPE_EDIT_ASSET){

                        firebaseHelper.deleteAsset(asset.getId());

                    }

                }
            });

        }

        public void bind(Asset asset) {

            binding.tvItemName.setText(asset.getItem());
            binding.tvQuantity.setText(String.valueOf(asset.getQuantity()));



        }

    }
}
