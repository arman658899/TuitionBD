package com.brogrammers.tuitionapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.brogrammers.tuitionapp.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }
    int image[]={
            R.drawable.picture_1,
            R.drawable.picture_2,
            R.drawable.picture_3
    };
    int title[]={
            R.string.intro_1,
            R.string.intro_2,
            R.string.intro_3
    };
    int description[]={
            R.string.desc_1,
            R.string.desc_2,
            R.string.desc_3
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
