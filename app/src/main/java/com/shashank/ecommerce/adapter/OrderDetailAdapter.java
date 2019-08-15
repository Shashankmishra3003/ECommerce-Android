package com.shashank.ecommerce.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shashank.ecommerce.OrderDetailActivity;
import com.shashank.ecommerce.R;
import com.shashank.ecommerce.model.Product;
import com.shashank.ecommerce.model.Request;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class OrderDetailViewHolder extends RecyclerView.ViewHolder
{
    public TextView odProductName, odQuantity, odShipping, odProductPrice;
    public ImageView odProductImage;
    public OrderDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        odProductName = itemView.findViewById(R.id.od_product_name);
        odQuantity = itemView.findViewById(R.id.od_quantity);
        odShipping = itemView.findViewById(R.id.od_shipping);
        odProductPrice = itemView.findViewById(R.id.od_price);

        odProductImage = itemView.findViewById(R.id.od_product_image);
    }
}
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailViewHolder>{
    private List<Product> myOrders ;
    private OrderDetailActivity orderDetailActivity;

    public OrderDetailAdapter(List<Product> myOrders, OrderDetailActivity orderDetailActivity) {
        this.myOrders = myOrders;
        this.orderDetailActivity = orderDetailActivity;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(orderDetailActivity)
                .inflate(R.layout.card_view_order_detail,viewGroup,false);
        return new OrderDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder orderDetailViewHolder, int position) {
        Product request = myOrders.get(position);
        orderDetailViewHolder.odProductName.setText(request.getName());
        orderDetailViewHolder.odQuantity.setText(request.getQuantity());
        orderDetailViewHolder.odShipping.setText(request.getShippingPrice());
        orderDetailViewHolder.odProductPrice.setText(request.getShippingPrice());

        Picasso.get()
                .load(request.getImageUrl())
                .into(orderDetailViewHolder.odProductImage);
    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
