package com.shashank.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shashank.ecommerce.R;
import com.squareup.picasso.Picasso;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    View mView;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setCategory(Context context, String title, String image)
    {
        TextView categoryName = mView.findViewById(R.id.category_name);
        ImageView categoryImage = mView.findViewById(R.id.category_image) ;

        categoryName.setText(title);
        Picasso.get()
                .load(image)
                .into(categoryImage);
    }
}
