package com.shashank.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shashank.ecommerce.R;
import com.shashank.ecommerce.utilities.ItemClickListner;
import com.squareup.picasso.Picasso;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    View mView;
    ItemClickListner itemClickListner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(this);
    }

    public void setProduct(Context context, String name, String description, String image, String price)
    {
        TextView ProductName, ProductDescription, ProductPrice;
        ImageView ProductImage;

        ProductName = mView.findViewById(R.id.product_name);
        ProductDescription = mView.findViewById(R.id.product_description);
        ProductPrice = mView.findViewById(R.id.product_price);
        ProductImage = mView.findViewById(R.id.product_image);

        ProductName.setText(name);
        ProductDescription.setText(description);
        ProductPrice.setText(price);
        Picasso.get()
                .load(image)
                .into(ProductImage);
    }
    @Override
    public void onClick(View v) {
        this.itemClickListner.onItemClick(v,getLayoutPosition());
    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }
}
