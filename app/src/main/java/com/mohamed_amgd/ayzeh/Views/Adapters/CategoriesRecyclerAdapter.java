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
import com.mohamed_amgd.ayzeh.Models.Category;
import com.mohamed_amgd.ayzeh.R;

import java.util.ArrayList;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoriesRecyclerViewHolder> {

    private final ArrayList<Category> categories;
    private Context mContext;
    private OnClickListener mCategoryOnClickListener;

    public CategoriesRecyclerAdapter(Context mContext, ArrayList<Category> categories) {
        this.categories = categories;
        this.mContext = mContext;
    }

    public void setCategoryOnClickListener(OnClickListener mCategoryOnClickListener) {
        this.mCategoryOnClickListener = mCategoryOnClickListener;
    }

    @NonNull
    @Override
    public CategoriesRecyclerAdapter.CategoriesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_list_item,parent,false);
        return new CategoriesRecyclerViewHolder(view,mCategoryOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesRecyclerAdapter.CategoriesRecyclerViewHolder holder, int position) {
        Category category = categories.get(position);

        ImageView categoryImage = holder.getCategoryImage();
        Glide.with(mContext).load(category.getImageDrawable())
                .override(450)
                .into(categoryImage);

        TextView categoryTitle = holder.getCategoryTitle();
        categoryTitle.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface OnClickListener{
        void onClick(int position);
    }

    public static class CategoriesRecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mCategoryImage;
        private final TextView mCategoryTitle;
        public CategoriesRecyclerViewHolder(@NonNull View itemView, OnClickListener listener) {
            super(itemView);
            mCategoryImage = itemView.findViewById(R.id.category_image);
            mCategoryTitle = itemView.findViewById(R.id.category_title);

            mCategoryImage.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position);
                    }
                }
            });
        }

        public ImageView getCategoryImage() {
            return mCategoryImage;
        }

        public TextView getCategoryTitle() {
            return mCategoryTitle;
        }
    }
}
