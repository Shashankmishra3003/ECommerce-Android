package com.shashank.ecommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.ecommerce.adapter.ProductViewHolder;
import com.shashank.ecommerce.model.Product;
import com.shashank.ecommerce.utilities.Common;
import com.shashank.ecommerce.utilities.ItemClickListner;

public class ProductListActivity extends AppCompatActivity {
    ActionBar actionBar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView productRecyclerView;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter;
    String productId;

    @Override
    protected void onStart() {
        super.onStart();
        if (Common.checkConnectivity(ProductListActivity.this))
        {
            loadProductRecyclerView();
        }
        else
            Common.showLogInAlertDialog(ProductListActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Product List");

        productId = getIntent().getStringExtra("id");

        if(Common.checkConnectivity(ProductListActivity.this))
        {
            inItProductRecyclerView();
        }
        else
            Common.showLogInAlertDialog(ProductListActivity.this);
    }

    private void inItProductRecyclerView() {
        productRecyclerView = findViewById(R.id.product_recycler_view);
        productRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        productRecyclerView.setLayoutManager(layoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");
    }


    private void loadProductRecyclerView() {
        FirebaseRecyclerOptions<Product> options =
            new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(databaseReference.orderByChild("id").equalTo(productId),Product.class)
                .build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, final int position, @NonNull final Product model) {
                        holder.setProduct(getApplicationContext(),model.getName(),model.getDescription(), model.getImageUrl(), model.getPrice());
                        holder.setItemClickListner(new ItemClickListner() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Bundle bundle = new Bundle();
                                bundle.putString("id",productId);
                                bundle.putString("productId",firebaseRecyclerAdapter.getRef(position).getKey());
                                bundle.putString("productName",model.getName());
                                bundle.putString("productPrice",model.getPrice());
                                bundle.putString("productDescription",model.getDescription());
                                bundle.putString("imageUrl",model.getImageUrl());
                                bundle.putString("shippingPrice",model.getShippingPrice());
                                Intent productDetail = new Intent(ProductListActivity.this,ProductDetailActivity.class);
                                productDetail.putExtras(bundle);
                                startActivityForResult(productDetail,1);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.card_view_productlist,viewGroup,false);
                        return new ProductViewHolder(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        productRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                productId = data.getStringExtra("prevId");
            }
        }
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
