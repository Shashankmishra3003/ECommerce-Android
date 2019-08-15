package com.shashank.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shashank.ecommerce.adapter.OrderStatusViewHolder;
import com.shashank.ecommerce.model.Request;
import com.shashank.ecommerce.utilities.Common;
import com.shashank.ecommerce.utilities.ItemClickListner;

public class OrderStatusActivity extends AppCompatActivity {
    ActionBar actionBar;
    RecyclerView orderStatusRecyclerView;
    GoogleSignInAccount acct;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<Request, OrderStatusViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Order History");

        if(Common.checkConnectivity(OrderStatusActivity.this))
        {
            inItRecyclerView();
            checkIfOrderExist(this);
        }
        else
            Common.showLogInAlertDialog(OrderStatusActivity.this);

    }

    private void checkIfOrderExist(final Context context) {
        Query query = databaseReference.orderByChild("userEmail").equalTo(acct.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    loadOrderHistory();
                }
                else{
                    Common.showEmptyCartAlertDialog(context);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadOrderHistory() {
        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(databaseReference.orderByChild("userEmail").equalTo(acct.getEmail()),Request.class)
                .build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Request, OrderStatusViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final OrderStatusViewHolder holder, final int position, @NonNull final Request model) {
                                holder.setOrderStatus(firebaseRecyclerAdapter.getRef(position).getKey(),model.getOrderDate(),model.getOrderTotal());
                                holder.setItemClickListner(new ItemClickListner() {
                                    @Override
                                    public void onItemClick(View v, int pos) {
                                        Intent orderStatus = new Intent(OrderStatusActivity.this,OrderDetailActivity.class);

                                        //assigning the model to current request object
                                        Common.currentRequest = model;
                                        startActivity(orderStatus);
                                    }
                                });
                            }
                    @NonNull
                    @Override
                    public OrderStatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.card_view_order_status,viewGroup,false);
                        return new OrderStatusViewHolder(view);
                    }
                };

            firebaseRecyclerAdapter.startListening();
            orderStatusRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public void inItRecyclerView()
    {
        orderStatusRecyclerView = findViewById(R.id.order_status_recycler_view);
        orderStatusRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        orderStatusRecyclerView.setLayoutManager(layoutManager);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Request");
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
