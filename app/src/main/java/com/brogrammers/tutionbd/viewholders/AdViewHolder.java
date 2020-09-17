package com.brogrammers.tutionbd.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brogrammers.tutionbd.R;

public class AdViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTittle,tvAdId,tvPostedDate,tvSalary,tvSubject,tvLocation;
    public AdViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTittle = itemView.findViewById(R.id.tv_tittle);
        tvAdId = itemView.findViewById(R.id.tv_post_id);
        tvPostedDate = itemView.findViewById(R.id.tv_posted_time);
        tvSalary = itemView.findViewById(R.id.tv_salary);
        tvSubject = itemView.findViewById(R.id.tv_subject);
        tvLocation = itemView.findViewById(R.id.tv_location);
    }
}
