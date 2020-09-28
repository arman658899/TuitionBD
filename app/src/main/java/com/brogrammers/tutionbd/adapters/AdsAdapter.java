package com.brogrammers.tutionbd.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.beans.AdInfo;
import com.brogrammers.tutionbd.listeners.OnRecyclerViewItemClickListener;
import com.brogrammers.tutionbd.viewholders.AdViewHolder;

import java.util.Calendar;
import java.util.List;

public class AdsAdapter extends RecyclerView.Adapter<AdViewHolder> {
    private Context context;
    private List<AdInfo> ads;
    private OnRecyclerViewItemClickListener<AdInfo> listener;
    public AdsAdapter(Context context, List<AdInfo> ads, OnRecyclerViewItemClickListener<AdInfo> listener) {
        this.context = context;
        this.ads = ads;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdViewHolder(LayoutInflater.from(context).inflate(R.layout.postshowitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        holder.tvLocation.setText(ads.get(position).getLocation());
        holder.tvTittle.setText(ads.get(position).getTittle());
        holder.tvSalary.setText(ads.get(position).getSalary());
        holder.tvSubject.setText(ads.get(position).getSubject());
        holder.tvPostedDate.setText(getFormatedDate(ads.get(position).getCreatedTime()));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemSelected(ads.get(position));
            }
        });
    }

    private String getFormatedDate(long createdTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createdTime);

        return DateFormat.format("dd-MM-yyyy",calendar).toString();
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }
}
