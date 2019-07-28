package com.shashank.ecommerce;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.balysv.materialripple.MaterialRippleLayout;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.shashank.ecommerce.Database.Database;
import com.shashank.ecommerce.adapter.ShoppingCartAdapter;
import com.shashank.ecommerce.model.Product;
import com.shashank.ecommerce.utilities.Common;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {
    ActionBar actionBar;
    TextView productName,productDesc,productPrice,productShipping;
    ImageView productImage;
    MaterialRippleLayout addToCart;
    ElegantNumberButton elegantNumberButton;
    CounterFab fabButton;

    String id,productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Product Detail");

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        productId = bundle.getString("productId");
        final String name = bundle.getString("productName");
        String desc = bundle.getString("productDescription");
        final String price = bundle.getString("productPrice");
        final String shipping = bundle.getString("shippingPrice");
        final String image = bundle.getString("imageUrl");

        productName = findViewById(R.id.product_name);
        productDesc = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        productShipping = findViewById(R.id.product_shipping);
        productImage = findViewById(R.id.product_image);
        addToCart = findViewById(R.id.button_add_to_cart);
        elegantNumberButton = findViewById(R.id.elegantNumberButton);
        fabButton = findViewById(R.id.fab_button);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinner_item,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        productName.setText(name);
        productDesc.setText(desc);
        productPrice.setText(price);
        productShipping.setText(shipping);
        Picasso.get()
                .load(image)
                .into(productImage);


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.checkConnectivity(ProductDetailActivity.this))
                {
                    new Database(getBaseContext()).addToCart(new Product(
                            acct.getEmail(),
                            productId,
                            name,
                            price,
                            image,
                            elegantNumberButton.getNumber(),
                            shipping
                    ));
                    fabButton.setCount(new Database(ProductDetailActivity.this).getCartCount(acct.getEmail()));
                    Common common;
                    ConstraintLayout constraintLayout = findViewById(R.id.product_detail_main);
                    common = new Common(ProductDetailActivity.this);
                    common.addedToCartSnackBar(constraintLayout);
                }
                else
                    Common.showLogInAlertDialog(ProductDetailActivity.this);
            }
        });
       fabButton.setCount(new Database(ProductDetailActivity.this).getCartCount(acct.getEmail()));

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("prevId",id);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
