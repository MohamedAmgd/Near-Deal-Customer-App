package com.mohamed_amgd.ayzeh.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohamed_amgd.ayzeh.Models.Offer;
import com.mohamed_amgd.ayzeh.R;

import java.util.ArrayList;

public class OffersRecyclerAdapter extends RecyclerView.Adapter<OffersRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Offer> mOffers;
    private OnClickListener mOfferOnClickListener;

    public OffersRecyclerAdapter(Context mContext, ArrayList<Offer> mOffers) {
        this.mContext = mContext;
        this.mOffers = mOffers;
    }

    public void setOfferOnClickListener(OnClickListener mOfferOnClickListener) {
        this.mOfferOnClickListener = mOfferOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offers_list_item,parent,false);
        return new ViewHolder(view, mOfferOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersRecyclerAdapter.ViewHolder holder, int position) {
        Offer offer = mOffers.get(position);

        Glide.with(mContext).load(offer.getShopImageUrl()).into(holder.getOfferImage());
        holder.getOfferName().setText(offer.getShopName());
        holder.getOfferPrice().setText(offer.getPrice());
    }

    @Override
    public int getItemCount() {
        return mOffers.size();
    }

    public interface OnClickListener{
        void onClick(int position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mOfferImage;
        TextView mOfferName;
        TextView mOfferPrice;
        public ViewHolder(@NonNull View itemView, OnClickListener listener) {
            super(itemView);
            mOfferImage = itemView.findViewById(R.id.offer_image);
            mOfferName = itemView.findViewById(R.id.offer_name);
            mOfferPrice = itemView.findViewById(R.id.offer_price);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position);
                    }
                }
            });
        }

        public ImageView getOfferImage() {
            return mOfferImage;
        }

        public TextView getOfferName() {
            return mOfferName;
        }

        public TextView getOfferPrice() {
            return mOfferPrice;
        }
    }
}
