package com.brogrammers.tuitionapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brogrammers.tuitionapp.R;
import com.brogrammers.tuitionapp.beans.HighlightItem;
import com.brogrammers.tuitionapp.viewholders.InformationItemViewHolder;
import com.brogrammers.tuitionapp.views.findtuitionortutoractivity.FindTuitionOrTutorActivityVP;

import java.util.List;

public class InformationHighlightAdapter extends RecyclerView.Adapter<InformationItemViewHolder> {
    private Context context;
    private List<HighlightItem> highlightItems;
    private FindTuitionOrTutorActivityVP.Presenter presenter;
    public InformationHighlightAdapter(Context context, List<HighlightItem> highlightItems,FindTuitionOrTutorActivityVP.Presenter presenter){
        this.context = context;
        this.highlightItems = highlightItems;
        this.presenter = presenter;
    }
    @NonNull
    @Override
    public InformationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InformationItemViewHolder(LayoutInflater.from(context).inflate(R.layout.samplveiew_highlighted_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InformationItemViewHolder holder, int position) {
        holder.tvTittle.setText(highlightItems.get(position).getTittle());
        holder.tvDesc.setText(highlightItems.get(position).getDes());
        holder.buttonAdd.setText(highlightItems.get(position).getButtonName());
        holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return highlightItems.size();
    }
}
