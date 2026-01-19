package com.avdhootsolutions.aswack_shopkeeper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avdhootsolutions.aswack_shopkeeper.R;
import com.avdhootsolutions.aswack_shopkeeper.models.SliderData;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class SliderImageAdapter extends SliderViewAdapter<SliderImageAdapter.SliderAdapterViewHolder> {
    // list for storing urls of images.
    private final List<SliderData> mSliderItems;
    public ICustomListListener iCustomListListener;

    // Constructor
    public SliderImageAdapter(Context context, ArrayList<SliderData> sliderDataArrayList, ICustomListListener iCustomListListener) {
        this.mSliderItems = sliderDataArrayList;
        this.iCustomListListener = iCustomListListener;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {
        final SliderData sliderItem = mSliderItems.get(position);

        Picasso.get()
                .load(sliderItem.getImgUrl())
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iCustomListListener.imageSelect(sliderItem.getImgUrl());
            }
        });
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }
    }

    public interface ICustomListListener {
        void imageSelect(String path);


    }
}
