package com.brogrammers.tuitionapp.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brogrammers.tuitionapp.AppPreferences;
import com.brogrammers.tuitionapp.Constants;
import com.brogrammers.tuitionapp.R;
import com.brogrammers.tuitionapp.beans.AdInfo;
import com.brogrammers.tuitionapp.listeners.OnRecyclerViewItemClickListener;
import com.brogrammers.tuitionapp.viewholders.AdViewHolder;

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
        return new AdViewHolder(LayoutInflater.from(context).inflate(R.layout.sampleview_ad_info,parent,false));
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
        holder.tvClass.setText(ads.get(position).getStudentClass());
        holder.tvSchedule.setText(ads.get(position).getSchedule());
        holder.tvLanguage.setText(ads.get(position).getLanguage());

        if (AppPreferences.getProfileType(context)== Constants.PROFILE_FIND_TUITION_TEACHER){
            //post for tuition
            holder.tvPostType.setText("Need Tutor");
        }else if (AppPreferences.getProfileType(context)==Constants.PROFILE_FIND_TUTOR_GUARDIAN){
            //post for tutor
            holder.tvPostType.setText("Need Tuition");
        }

    }

    private String getFormatedDate(long createdTime) {
        if (createdTime <= 0 ) return "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createdTime);

        return DateFormat.format("dd-MM-yyyy",calendar).toString();
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }
}
