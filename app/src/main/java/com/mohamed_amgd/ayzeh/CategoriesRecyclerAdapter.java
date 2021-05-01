package com.mohamed_amgd.ayzeh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoriesRecyclerViewHolder> {

    private final ArrayList<Category> categories;
    private Context mContext;
    private View.OnClickListener mCategoryOnClickListener;

    public CategoriesRecyclerAdapter(Context mContext, ArrayList<Category> categories) {
        this.categories = categories;
        this.mContext = mContext;
    }

    public void setCategoryOnClickListener(View.OnClickListener mCategoryOnClickListener) {
        this.mCategoryOnClickListener = mCategoryOnClickListener;
    }

    @NonNull
    @Override
    public CategoriesRecyclerAdapter.CategoriesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_list_item,parent,false);
        return new CategoriesRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesRecyclerAdapter.CategoriesRecyclerViewHolder holder, int position) {
        Category category = categories.get(position);

        ImageView categoryImage = holder.mCategoryImage;
        Glide.with(mContext).load(category.getImageDrawable())
                .override(450)
                .into(categoryImage);

        categoryImage.setOnClickListener(mCategoryOnClickListener);
        TextView categoryTitle = holder.mCategoryTitle;
        categoryTitle.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoriesRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView mCategoryImage;
        TextView mCategoryTitle;
        public CategoriesRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            mCategoryImage = itemView.findViewById(R.id.category_image);
            mCategoryTitle = itemView.findViewById(R.id.category_title);
        }
    }
}
