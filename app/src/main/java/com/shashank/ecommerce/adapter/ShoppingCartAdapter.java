package com.shashank.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.shashank.ecommerce.Database.Database;
import com.shashank.ecommerce.R;
import com.shashank.ecommerce.ShoppingCartActivity;
import com.shashank.ecommerce.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView productName,productPrice;
    public ImageView productImage;
    public ElegantNumberButton btnQuantity;
    public ImageView btnDelete;

    private List<Product> productList = new ArrayList<>();

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.cart_name);
        productPrice = itemView.findViewById(R.id.cart_price);
        productImage = itemView.findViewById(R.id.cart_image);
        btnQuantity = itemView.findViewById(R.id.elegantNumberButton);
        btnDelete = itemView.findViewById(R.id.button_delete);

    }

    @Override
    public void onClick(View v) {

    }
}

public class ShoppingCartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private List<Product> productList = new ArrayList<>();
    private ShoppingCartActivity shoppingCartActivity;
    private Context applicationContext;

    public ShoppingCartAdapter(List<Product> productList, ShoppingCartActivity shoppingCartActivity, Context context)
    {
        this.productList = productList;
        this.shoppingCartActivity = shoppingCartActivity;
        this.applicationContext = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(shoppingCartActivity);
        View itemView = inflater.inflate(R.layout.card_view_cart,viewGroup,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, final int position) {
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(applicationContext);
        Picasso.get()
                .load(productList.get(position).getImageUrl())
                .into(cartViewHolder.productImage);

        cartViewHolder.btnQuantity.setNumber(productList.get(position).getQuantity());
        cartViewHolder.productName.setText(productList.get(position).getName());

        cartViewHolder.btnQuantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Product product = productList.get(position);
                product.setQuantity(String.valueOf(newValue));
                new Database(shoppingCartActivity).updateCart(product);

                //Calculating the price of product
                int total = 0;
                List<Product> products = new Database(shoppingCartActivity).getCartItems(acct.getEmail());
                for(Product item : products)
                {
                    total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
                }
                TextView price = shoppingCartActivity.findViewById(R.id.cart_total_price);
                price.setText(Integer.toString(total));

                int cartPrice = (Integer.parseInt(productList.get(position).getPrice())) * (Integer.parseInt(productList.get(position).getQuantity()));
                cartViewHolder.productPrice.setText(Integer.toString(cartPrice));
            }
        });

        int cartPrice = (Integer.parseInt(productList.get(position).getPrice())) * (Integer.parseInt(productList.get(position).getQuantity()));
        cartViewHolder.productPrice.setText(Integer.toString(cartPrice));



        cartViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> products = new Database(shoppingCartActivity).getCartItems(acct.getEmail());

                //removing the item from the List at given position
                products.remove(position);

                //Deleting all items from the database
                new Database(shoppingCartActivity).cleanCart(acct.getEmail());

                //Now update new data into the database
                for (Product item : products)
                {
                    new Database(shoppingCartActivity).addToCart(item);
                }
                //refresh the content
                shoppingCartActivity.loadProductList(acct);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
