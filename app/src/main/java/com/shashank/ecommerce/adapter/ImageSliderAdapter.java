package com.shashank.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageSliderAdapter extends PagerAdapter {

    private Context context;
    private String[] imageUrl;
    private int customPosition;

    public ImageSliderAdapter(Context context, String[] imageUrl)
    {
        this.context = context;
        this.imageUrl = imageUrl;
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE; //smooth flow from last to first slide
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;  //here view and object are returned from instantiateItem method down below

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        if (customPosition == imageUrl.length)
        {
            customPosition = 0;
        }
        Picasso.get()
                .load(imageUrl[customPosition])
                .fit()
                .centerCrop()
                .into(imageView);
        container.addView(imageView);
        customPosition++;
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
