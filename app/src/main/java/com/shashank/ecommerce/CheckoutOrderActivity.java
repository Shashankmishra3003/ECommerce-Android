package com.shashank.ecommerce;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.ecommerce.Database.Database;
import com.shashank.ecommerce.adapter.CheckoutAdapter;
import com.shashank.ecommerce.model.Product;
import com.shashank.ecommerce.model.Request;
import com.shashank.ecommerce.model.Users;
import com.shashank.ecommerce.utilities.Common;
import com.shashank.ecommerce.utilities.CustomDialog;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutOrderActivity extends AppCompatActivity implements CustomDialog.OnInputListner {
    private static final String TAG = "CheckoutOrderActivity";
    RecyclerView checkoutRecyclerView;
    CheckoutAdapter checkoutAdapter;
    TextView subTotal, standardShipping, orderTotal;
    GoogleSignInAccount acct;
    MaterialRippleLayout materialRippleLayout;
    List<Product> orderItem = new ArrayList<>();
    DatabaseReference reference;
    String currentTime;
    TextView firstName, lastName,orderAddress,orderCity, orderState, orderZipcode, orderCountry, orderMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_order);
        acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        Date date = new Date();
        Locale locale = new Locale("en","US");
        SimpleDateFormat formater = new SimpleDateFormat("MM-dd-yyyy HH:mm",locale);
        currentTime = formater.format(date);

        //ActionBar without creating a custom toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        subTotal = findViewById(R.id.od_subtotal);
        standardShipping = findViewById(R.id.od_standard_shipping);
        orderTotal = findViewById(R.id.od_order_dollar);

        materialRippleLayout = findViewById(R.id.materialRippleLayout_checkout);
        checkoutRecyclerView = findViewById(R.id.checkout_recycler_view);
        checkoutRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        checkoutRecyclerView.setLayoutManager(layoutManager);

        checkForAddress();
        loadOrderList(acct);

        materialRippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("Request");
                Request request = new Request(
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    orderMobile.getText().toString(),
                    orderAddress.getText().toString(),
                    orderCity.getText().toString(),
                    orderState.getText().toString(),
                    orderZipcode.getText().toString(),
                    orderCountry.getText().toString(),
                    orderTotal.getText().toString(),
                    currentTime,
                    acct.getEmail(),
                    subTotal.getText().toString(),
                    standardShipping.getText().toString(),
                    orderItem
                );
                //Sending request to firebase
                String order_number = String.valueOf(System.currentTimeMillis());
                reference.child(order_number).setValue(request);

                //Showing SnackBar and opening the main page
                Common common;
                ScrollView scrollView = findViewById(R.id.checkout_nested_scroll);
                common = new Common(CheckoutOrderActivity.this);
                common.orderPlacedSnackbar(scrollView);

                //Cleaning the cart
                new Database(getBaseContext()).cleanCart(acct.getEmail());

                //Delay for Snack bar
                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            startActivity(new Intent(CheckoutOrderActivity.this, MainActivity.class));
                    }
                },1000L);
            }
        });

    }

    private void checkForAddress() {
        final String userID = acct.getId();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null)
                {
                    CustomDialog dialog = new CustomDialog();
                    dialog.show(getSupportFragmentManager(),"Enter Deliver Address");
                }
                else
                {
                    //Get Address data from Firebase and display here
                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(userID);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Users users = new Users();
                            for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                            {
                                users = userSnapshot.getValue(Users.class);
                            }
                           
                            populateAddress(users);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "Unable to load data from Firebase: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Unable to load data from Firebase: " + databaseError.getMessage());
            }
        });
    }

    private void loadOrderList(GoogleSignInAccount acct) {
        orderItem = new Database(this).getCartItemsWOEmail(acct.getEmail());
        checkoutAdapter = new CheckoutAdapter(orderItem,this,getApplicationContext());
        checkoutAdapter.notifyDataSetChanged();

        checkoutRecyclerView.setAdapter(checkoutAdapter);

        int subtotal =0 , shipping = 0;
        for (Product product : orderItem)
        {
            subtotal += (Integer.parseInt(product.getQuantity())) * (Integer.parseInt(product.getPrice()));
            shipping += Integer.parseInt(product.getShippingPrice());
        }
        subTotal.setText(Integer.toString(subtotal));
        standardShipping.setText(Integer.toString(shipping));

        int total = subtotal + shipping;
        Locale locale = new Locale("en","US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        orderTotal.setText(numberFormat.format(total));
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ShoppingCartActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    //Interface from custom dialog to populate the address entered by the user
    @Override
    public void sendInput(Users users) {
        Log.d(TAG,"Got the data from Dialog");
        populateAddress(users);

    }

    private void populateAddress(Users users) {

        firstName = findViewById(R.id.od_first_name);
        lastName = findViewById(R.id.od_last_name);
        orderAddress = findViewById(R.id.od_order_address);
        orderCity = findViewById(R.id.od_order_city);
        orderState = findViewById(R.id.od_order_state);
        orderZipcode = findViewById(R.id.od_order_zipcode);
        orderCountry = findViewById(R.id.od_order_country);
        orderMobile = findViewById(R.id.od_order_mobile);

        firstName.setText(users.getFirstName());
        lastName.setText(users.getLastName());
        orderAddress.setText(users.getAddress());
        orderCity.setText(users.getCity());
        orderState.setText(users.getState());
        orderZipcode.setText(users.getZipCode());
        orderCountry.setText(users.getCountry());
        orderMobile.setText(users.getMobile());
    }
}
