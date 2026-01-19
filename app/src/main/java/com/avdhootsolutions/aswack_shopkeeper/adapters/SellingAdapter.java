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
import com.avdhootsolutions.aswack_shopkeeper.interfaces.ServiceClickListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class SellingAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ViewHolder viewHolder;
    private int pos;
    private ServiceClickListener serviceClickListener;
    private ArrayList<String> serviceArray;
    int[] backgrounds = new int[]{
            R.drawable.ic_car,
            R.drawable.ic_bike,
            R.drawable.ic_commercial,
            R.drawable.ic_emergency_vehical,
            R.drawable.ic_heavy_vehical
    };

    public SellingAdapter(Context mContext, int pos, ArrayList<String> serviceArray, ServiceClickListener serviceClickListener) {
        this.mContext = mContext;
        this.pos = pos;
        this.serviceArray = serviceArray;
        this.serviceClickListener = serviceClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_selling, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        viewHolder = (ViewHolder) holder;
        viewHolder.tvCategoryName.setText(serviceArray.get(position));
        viewHolder.ivCategory.setImageResource(backgrounds[position]);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceClickListener.onServiceCLick(pos);
//                if (pos == 0) {
//                    mContext.startActivity(new Intent(mContext, SellVehicalActivity.class));
//                } else if (pos == 1) {
//                    mContext.startActivity(new Intent(mContext, BuyVehicalActivity.class));
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceArray.size();
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

