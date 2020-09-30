package com.brogrammers.tutionbd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.brogrammers.tutionbd.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }
    int image[]={
            R.drawable.intro,
            R.drawable.intro,
            R.drawable.intro,
            R.drawable.intro,
    };
    int title[]={
            R.string.findTutor,
            R.string.findTutor,
            R.string.findTutor,
            R.string.findTutor,
    };
    int description[]={
            R.string.description1,
            R.string.description1,
            R.string.description1,
            R.string.description1,
    };

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater= (LayoutInflater) context.getSystemService( context.LAYOUT_INFLATER_SERVICE );
        View view=layoutInflater.inflate( R.layout.introsliderview,container,false );

        //Hooks
        ImageView imageView=view.findViewById( R.id.imageView );
        TextView titleText=view.findViewById( R.id.title );
        TextView details=view.findViewById( R.id.details );

        imageView.setImageResource( image[position] );
        titleText.setText( title [position] );
        details.setText( description[position] );

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (ConstraintLayout) object );
    }
}
