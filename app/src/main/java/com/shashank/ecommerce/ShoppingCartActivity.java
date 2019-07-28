package com.shashank.ecommerce;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.shashank.ecommerce.Database.Database;
import com.shashank.ecommerce.adapter.ShoppingCartAdapter;
import com.shashank.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {
    ActionBar actionBar;
    RecyclerView cartRecyclerView;
    ShoppingCartAdapter shoppingCartAdapter;
    TextView cartTotal,productCount,itemText;

    List<Product> cartItem = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Shopping Cart");

        cartTotal = findViewById(R.id.cart_total_price);
        productCount = findViewById(R.id.product_count);
        itemText = findViewById(R.id.item_text);

        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        cartRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        cartRecyclerView.setLayoutManager(layoutManager);

        loadProductList(acct);

    }

    public void loadProductList(GoogleSignInAccount acct) {
        cartItem = new Database(this).getCartItems(acct.getEmail());
        shoppingCartAdapter = new ShoppingCartAdapter(cartItem,this,getApplicationContext());
        shoppingCartAdapter.notifyDataSetChanged();

        cartRecyclerView.setAdapter(shoppingCartAdapter);

        int total = 0;
        for (Product product: cartItem)
        {
            total += (Integer.parseInt(product.getPrice())) * (Integer.parseInt(product.getQuantity()));
        }
        cartTotal.setText(Integer.toString(total));
        productCount.setText(Integer.toString(cartItem.size()));
        if(cartItem.size()>1)
        {
            itemText.setText(getString(R.string.items));
        }
        else
            itemText.setText(getString(R.string.item));
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
