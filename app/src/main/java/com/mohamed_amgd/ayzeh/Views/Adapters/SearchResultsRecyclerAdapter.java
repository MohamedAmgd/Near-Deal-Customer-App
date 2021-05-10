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

public class SearchResultsRecyclerAdapter extends RecyclerView.Adapter<SearchResultsRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Product> mProducts;
    private OnClickListener mSearchResultOnClickListener;

    public SearchResultsRecyclerAdapter(Context context, ArrayList<Product> products) {
        mContext = context;
        mProducts = products;
    }

    public void setSearchResultOnClickListener(OnClickListener mSearchResultOnClickListener) {
        this.mSearchResultOnClickListener = mSearchResultOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_list_item,parent,false);
        return new ViewHolder(view, mSearchResultOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = mProducts.get(position);

        Glide.with(mContext).load(product.getImageUrl()).into(holder.getProductItemImage());
        holder.getProductItemName().setText(product.getName());
        holder.getProductItemBrand().setText(product.getBrand());
        holder.getProductItemPrice().setText(product.getPrice());
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public interface OnClickListener{
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mProductItemImage;
        TextView mProductItemName;
        TextView mProductItemBrand;
        TextView mProductItemPrice;

        public ViewHolder(@NonNull View itemView, OnClickListener listener) {
            super(itemView);
            mProductItemImage = itemView.findViewById(R.id.product_item_image);
            mProductItemName = itemView.findViewById(R.id.product_item_name);
            mProductItemBrand = itemView.findViewById(R.id.product_item_brand);
            mProductItemPrice = itemView.findViewById(R.id.product_item_price);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position);
                    }
                }
            });
        }

        public ImageView getProductItemImage() {
            return mProductItemImage;
        }

        public TextView getProductItemName() {
            return mProductItemName;
        }

        public TextView getProductItemBrand() {
            return mProductItemBrand;
        }

        public TextView getProductItemPrice() {
            return mProductItemPrice;
        }
    }
}
