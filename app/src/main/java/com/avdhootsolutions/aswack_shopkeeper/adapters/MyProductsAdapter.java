package com.avdhootsolutions.aswack_shopkeeper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avdhootsolutions.aswack_shopkeeper.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class MyProductsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private MyProductsAdapter.ViewHolder viewHolder;
    private ArrayList<String> ourServiceArray;

    public MyProductsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyProductsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_products, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        viewHolder = (MyProductsAdapter.ViewHolder) holder;

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView ivCategory;
        TextView tvCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategory = itemView.findViewById(R.id.ivCategory);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }

    }
}
