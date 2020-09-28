package com.brogrammers.tutionbd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.viewholders.InformationItemViewHolder;

public class InformationHighlightAdapter extends RecyclerView.Adapter<InformationItemViewHolder> {
    private Context context;
    public InformationHighlightAdapter(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public InformationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InformationItemViewHolder(LayoutInflater.from(context).inflate(R.layout.samplveiew_highlighted_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InformationItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
