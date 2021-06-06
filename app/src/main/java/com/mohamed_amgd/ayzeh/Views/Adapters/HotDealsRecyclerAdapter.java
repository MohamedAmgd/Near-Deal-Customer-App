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
import com.mohamed_amgd.ayzeh.Models.Product;
import com.mohamed_amgd.ayzeh.R;

import java.util.ArrayList;


public class HotDealsRecyclerAdapter extends RecyclerView.Adapter<HotDealsRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Product> mHotDeals;
    private OnClickListener mHotDealOnClickListener;


    public HotDealsRecyclerAdapter( Context mContext, ArrayList<Product> mHotDeals) {
        this.mHotDeals = mHotDeals;
        this.mContext = mContext;
    }

    public void setHotDealOnClickListener(OnClickListener mHotDealOnClickListener) {
        this.mHotDealOnClickListener = mHotDealOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hot_deals_list_item,parent,false);
        return new ViewHolder(view, mHotDealOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product hotDeal = mHotDeals.get(position);
        Glide.with(mContext)
                .load(hotDeal.getImageUrl())
                .into(holder.getHotDealImageView());

        holder.getHotDealTitleTextView().setText(hotDeal.getName());
        holder.getHotDealBrandTextView().setText(hotDeal.getBrand());
        String price = hotDeal.getPrice() + "";
        holder.getHotDealPriceTextView().setText(price);

    }

    @Override
    public int getItemCount() {
        return mHotDeals.size();
    }

    public interface OnClickListener{
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView mHotDealImageView;
        private final TextView mHotDealTitleTextView;
        private final TextView mHotDealBrandTextView;
        private final TextView mHotDealPriceTextView;

        public ViewHolder(@NonNull View itemView, OnClickListener listener) {
            super(itemView);
            mHotDealImageView = itemView.findViewById(R.id.hot_deal_image);
            mHotDealTitleTextView = itemView.findViewById(R.id.hot_deal_title);
            mHotDealBrandTextView = itemView.findViewById(R.id.hot_deal_brand);
            mHotDealPriceTextView = itemView.findViewById(R.id.hot_deal_price);


            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position);
                    }
                }
            });
        }

        public ImageView getHotDealImageView() {
            return mHotDealImageView;
        }

        public TextView getHotDealTitleTextView() {
            return mHotDealTitleTextView;
        }

        public TextView getHotDealBrandTextView() {
            return mHotDealBrandTextView;
        }

        public TextView getHotDealPriceTextView() {
            return mHotDealPriceTextView;
        }
    }
}
