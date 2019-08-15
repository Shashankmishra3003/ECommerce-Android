package com.shashank.ecommerce.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shashank.ecommerce.R;
import com.shashank.ecommerce.utilities.ItemClickListner;



public class OrderStatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    View mView;
    ItemClickListner itemClickListner;

    public OrderStatusViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(this);

    }

    public void setOrderStatus(String Id, String date, String total)
    {
        TextView orderId, orderDate, orderTotal;

        orderId = mView.findViewById(R.id.order_id);
        orderDate = mView.findViewById(R.id.order_date);
        orderTotal = mView.findViewById(R.id.order_total);

        orderId.setText(Id);
        orderDate.setText(date);
        orderTotal.setText(total);
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

