package com.brogrammers.tutionbd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.beans.HighlightItem;
import com.brogrammers.tutionbd.viewholders.InformationItemViewHolder;
import com.brogrammers.tutionbd.views.findtuitionortutoractivity.FindTuitionOrTutorActivityPresenter;
import com.brogrammers.tutionbd.views.findtuitionortutoractivity.FindTuitionOrTutorActivityVP;

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
                if (presenter!=null) presenter.onHighlightItemClicked(highlightItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return highlightItems.size();
    }
}
