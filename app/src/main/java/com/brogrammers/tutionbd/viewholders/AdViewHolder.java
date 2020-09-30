package com.brogrammers.tutionbd.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brogrammers.tutionbd.R;

public class AdViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTittle,tvClass,tvPostedDate,tvSalary,tvSubject,tvLocation,tvSchedule,tvLanguage,tvPostType;
    public LinearLayout container;
    public AdViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTittle = itemView.findViewById(R.id.tv_tittle);
        tvPostedDate = itemView.findViewById(R.id.tv_posted_time);
        tvSalary = itemView.findViewById(R.id.tv_salary);
        tvSubject = itemView.findViewById(R.id.tv_subject);
        tvLocation = itemView.findViewById(R.id.tv_location);
        tvPostType = itemView.findViewById(R.id.post_type);
        tvLanguage = itemView.findViewById(R.id.textview_language);
        tvSchedule = itemView.findViewById(R.id.textview_weekly_schedule);
        tvClass = itemView.findViewById(R.id.textview_class);

        container = itemView.findViewById(R.id.linearlayout_container);
    }
}
