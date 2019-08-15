package com.shashank.ecommerce;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.shashank.ecommerce.adapter.OrderDetailAdapter;
import com.shashank.ecommerce.utilities.Common;

public class OrderDetailActivity extends AppCompatActivity {
    TextView odSubTotal, odTotalShipping, odTotal, odFName, odLName, odAddress, odCity,odState, odZipcode,
                odCountry, odMobile;
    RecyclerView orderDetailRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        odSubTotal = findViewById(R.id.od_subtotal);
        odTotalShipping = findViewById(R.id.od_standard_shipping);
        odTotal = findViewById(R.id.od_order_dollar);
        odFName = findViewById(R.id.od_first_name);
        odLName = findViewById(R.id.od_last_name);
        odAddress = findViewById(R.id.od_order_address);
        odCity = findViewById(R.id.od_order_city);
        odState = findViewById(R.id.od_order_state);
        odZipcode = findViewById(R.id.od_order_zipcode);
        odCountry = findViewById(R.id.od_order_country);
        odMobile = findViewById(R.id.od_order_mobile);

        orderDetailRecyclerView = findViewById(R.id.order_detail_recyclerview);
        orderDetailRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        orderDetailRecyclerView.setLayoutManager(layoutManager);

        //String order_id = getIntent().getStringExtra("orderId");
        odSubTotal.setText(Common.currentRequest.getOrderSubtotal());
        odTotalShipping.setText(Common.currentRequest.getOrderTotalShipping());
        odTotal.setText(Common.currentRequest.getOrderTotal());

        odFName.setText(Common.currentRequest.getFname());
        odLName.setText(Common.currentRequest.getLname());
        odAddress.setText(Common.currentRequest.getAddress());
        odCity.setText(Common.currentRequest.getCity());
        odState.setText(Common.currentRequest.getState());
        odCountry.setText(Common.currentRequest.getCountry());
        odMobile.setText(Common.currentRequest.getMobile());

        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentRequest.getOrderList(),this);
        adapter.notifyDataSetChanged();
        orderDetailRecyclerView.setAdapter(adapter);
    }
}
