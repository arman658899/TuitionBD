package com.brogrammers.tuitionapp.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brogrammers.tuitionapp.R;

public class InformationItemViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTittle,tvDesc;
    public Button buttonAdd;
    public InformationItemViewHolder(@NonNull View itemView) {
        super(itemView);

        tvTittle = itemView.findViewById(R.id.textview_tittle);
        tvDesc = itemView.findViewById(R.id.textview_details);
        buttonAdd = itemView.findViewById(R.id.button_add);
    }
}
