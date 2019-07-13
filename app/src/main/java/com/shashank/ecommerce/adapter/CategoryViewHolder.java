package com.shashank.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shashank.ecommerce.R;
import com.shashank.ecommerce.utilities.ItemClickListner;
import com.squareup.picasso.Picasso;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    View mView;
    ItemClickListner itemClickListner;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(this);
    }

    public void setCategory(Context context, String title, String image,ProgressBar progressBar,RelativeLayout sliderLayout,RelativeLayout recyclerLayout)
    {
        TextView categoryName = mView.findViewById(R.id.category_name);
        ImageView categoryImage = mView.findViewById(R.id.category_image) ;

        categoryName.setText(title);
        Picasso.get()
                .load(image)
                .into(categoryImage);
        sliderLayout.setVisibility(View.VISIBLE);
        recyclerLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        this.itemClickListner.onItemClick(v, getLayoutPosition ());
    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }
}
