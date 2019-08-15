package com.shashank.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.shashank.ecommerce.CheckoutOrderActivity;
import com.shashank.ecommerce.R;
import com.shashank.ecommerce.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class CheckoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView qtyCheckout, priceCheckout, shippingCheckout, totalCheckout,nameCheckout;
    public ImageView imageCheckout;


    public CheckoutViewHolder(@NonNull View itemView) {
        super(itemView);
        qtyCheckout = itemView.findViewById(R.id.qty_checkout);
        priceCheckout = itemView.findViewById(R.id.price_checkout);
        shippingCheckout = itemView.findViewById(R.id.shipping_checkout);
        totalCheckout = itemView.findViewById(R.id.total_checkout);
        nameCheckout = itemView.findViewById(R.id.name_checkout);
        imageCheckout = itemView.findViewById(R.id.image_checkout);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutViewHolder> {

    private List<Product> orderList = new ArrayList<>();
    private CheckoutOrderActivity checkoutOrderActivity;
    private Context applicationContext;


    public CheckoutAdapter(List<Product> orderList, CheckoutOrderActivity checkoutOrderActivity, Context context)
    {
        this.orderList = orderList;
        this.checkoutOrderActivity = checkoutOrderActivity;
        this.applicationContext = context;
    }
    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(checkoutOrderActivity);
        View itemView = inflater.inflate(R.layout.card_view_checkout,viewGroup,false);
        return new CheckoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder checkoutViewHolder, int position) {
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(applicationContext);
        Picasso.get()
                .load(orderList.get(position).getImageUrl())
                .into(checkoutViewHolder.imageCheckout);
        checkoutViewHolder.qtyCheckout.setText(orderList.get(position).getQuantity());
        checkoutViewHolder.nameCheckout.setText(orderList.get(position).getName());
        checkoutViewHolder.shippingCheckout.setText(orderList.get(position).getShippingPrice());
        checkoutViewHolder.priceCheckout.setText(orderList.get(position).getPrice());

        int itemPrice = (Integer.parseInt(orderList.get(position).getPrice())) * (Integer.parseInt(orderList.get(position).getQuantity()));
        checkoutViewHolder.totalCheckout.setText(Integer.toString(itemPrice));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
