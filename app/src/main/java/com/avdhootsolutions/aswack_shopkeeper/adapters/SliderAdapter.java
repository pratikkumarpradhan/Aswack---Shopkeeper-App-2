package com.avdhootsolutions.aswack_shopkeeper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avdhootsolutions.aswack_shopkeeper.R;
import com.avdhootsolutions.aswack_shopkeeper.models.Sliders;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ViewHolder viewHolder;
    private ArrayList<Sliders> sliderArray;

    public SliderAdapter(Context mContext, ArrayList<Sliders> sliderArray) {
        this.mContext = mContext;
        this.sliderArray = sliderArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_slider, parent, false);
        // recyclerView is your passed view.
//        DisplayMetrics displayMetrics = new DisplayMetrics();
////        Resources.getSystem().getDisplayMetrics().widthPixels
////        int height = displayMetrics.heightPixels;
//        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
//        //  int width = 500;
//        ViewGroup.LayoutParams params = view.getLayoutParams();
//        params.width = (int) (width * 0.8);
//        view.setLayoutParams(params);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        viewHolder = (ViewHolder) holder;

        Glide.with(mContext).load(sliderArray.get(position).getImage()).placeholder(R.drawable.placeholder).into(viewHolder.ivCategory);

    }

    @Override
    public int getItemCount() {
        return sliderArray.size();
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
            // tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }

    }
}
