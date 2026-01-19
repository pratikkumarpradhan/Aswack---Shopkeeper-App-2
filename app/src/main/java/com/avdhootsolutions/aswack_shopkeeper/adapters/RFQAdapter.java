package com.avdhootsolutions.aswack_shopkeeper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avdhootsolutions.aswack_shopkeeper.R;
import com.avdhootsolutions.aswack_shopkeeper.activities.RFQDetailActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class RFQAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private RFQAdapter.ViewHolder viewHolder;
    private ArrayList<String> ourServiceArray;

    public RFQAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RFQAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rfq, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        viewHolder = (RFQAdapter.ViewHolder) holder;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, RFQDetailActivity.class));
            }
        });
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

