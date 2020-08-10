package com.viba.olokartusers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private Integer[] images = { R.drawable.sliderddd, R.drawable.sliderbbb,R.drawable.sliderccc,R.drawable.slideraaa};

    // public String[] slidertext = {"Non-profit organization", "Grab Opportunities", "Improve and sharp your skills", "Explore Yourself by joining our Tech or Non-Tech community"};

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // return super.instantiateItem(container, position);

        View view = layoutInflater.inflate(R.layout.activity_slider_adapter, container, false);

        ImageView imageView = view.findViewById(R.id.sliderImage);
        //TextView textView = (TextView) view.findViewById(R.id.sliderText);

        // textView.setText(slidertext[position]);
        imageView.setImageResource(images[position]);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}