package com.avdhootsolutions.aswack_shopkeeper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avdhootsolutions.aswack_shopkeeper.R;

public class NotificationAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ViewHolder viewHolder;

    public NotificationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        viewHolder = (ViewHolder) holder;
    }

    @Override
    public int getItemCount() {
        return 15;
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

//        ConstraintLayout clSenderMsg, clReceiverMsg;
//        CardView clReceiverImages;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            clReceiverMsg = itemView.findViewById(R.id.clReciverMsg);
//            clSenderMsg = itemView.findViewById(R.id.clSenderMsg);
//            clReceiverImages = itemView.findViewById(R.id.clReceiverImages);
        }

    }
}

