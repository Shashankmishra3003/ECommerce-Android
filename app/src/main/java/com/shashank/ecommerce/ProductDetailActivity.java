package com.shashank.ecommerce;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {
    ActionBar actionBar;
    TextView productName,productDesc,productPrice,productShipping;
    ImageView productImage;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Product Detail");

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        String name = bundle.getString("productName");
        String desc = bundle.getString("productDescription");
        String price = bundle.getString("productPrice");
        String shipping = bundle.getString("shippingPrice");
        String image = bundle.getString("imageUrl");

        productName = findViewById(R.id.product_name);
        productDesc = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        productShipping = findViewById(R.id.product_shipping);
        productImage = findViewById(R.id.product_image);

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
