package com.shashank.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.ecommerce.adapter.CategoryViewHolder;
import com.shashank.ecommerce.adapter.ImageSliderAdapter;
import com.shashank.ecommerce.model.Category;
import com.shashank.ecommerce.utilities.Common;
import com.shashank.ecommerce.utilities.ItemClickListner;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView categoryRecyclerView;
    RelativeLayout sliderLayout,recyclerLayout;
    ProgressBar progressBar;
    DrawerLayout drawer;
    NavigationView navigationView;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> firebaseRecyclerAdapter;
    GoogleSignInAccount acct;

    TextView accountName, accountEmail;
    ImageView accountImage;

    private Timer slideTimer;
    private int current_position = 0;
    private ViewPager imageSlider;
    private LinearLayout dotLayout;
    private int customPosition = 0;

    private String[] imageUrls = new String[]{
            "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
    };

    private static final int NUM_COLUMNS = 2;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
        if(Common.checkConnectivity(MainActivity.this))
        {
            loadRecyclerView();

        }
        else
            Common.showLogInAlertDialog(MainActivity.this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        //ViewPager
        imageSlider = findViewById(R.id.image_slider);
        //dot layout
        dotLayout = findViewById(R.id.dot_container);
        progressBar = findViewById(R.id.main_progress);
        sliderLayout = findViewById(R.id.slider_container);
        recyclerLayout = findViewById(R.id.recycler_layout);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null)
                {
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                }
            }
        };

        if(Common.checkConnectivity(MainActivity.this))
        {
            //Adding data to the navbar
            loadNavigationView();
            //Setting the Image Slider
            loadImageSlider();

            initCategoryRecyclerView();
        }
        else
        {
            Common.showLogInAlertDialog(MainActivity.this);
        }

    }

    private void loadNavigationView() {
        View headerView = navigationView.getHeaderView(0);
        accountName = headerView.findViewById(R.id.account_name);
        accountEmail = headerView.findViewById(R.id.account_email);
        accountImage = headerView.findViewById(R.id.user_image);

        accountName.setText(acct.getDisplayName());
        accountEmail.setText(acct.getEmail());
        Picasso.get()
                .load(acct.getPhotoUrl())
                .into(accountImage);
    }

    private void loadRecyclerView()
    {
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(databaseReference,Category.class)
                        .build();

         firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
                    @NonNull
                    @Override
                    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.card_view_category,viewGroup,false);

                        return new CategoryViewHolder(view);
                    }
                    @Override
                    protected void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position, @NonNull Category model) {
                        holder.setCategory(getApplicationContext(),model.getTitle(),model.getImageUrl(),progressBar,sliderLayout,recyclerLayout);
                        holder.setItemClickListner(new ItemClickListner() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Intent productList = new Intent(MainActivity.this,ProductListActivity.class);
                                productList.putExtra("id",firebaseRecyclerAdapter.getRef(position).getKey());
                                startActivity(productList);
                            }
                        });
                    }

                };
        firebaseRecyclerAdapter.startListening();
        categoryRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }
    private void loadImageSlider()
    {
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(this,imageUrls);
        imageSlider.setAdapter(imageSliderAdapter);

        prepareDots(customPosition++);
        imageSlideShow(); //Automatic slider

        imageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (customPosition == imageUrls.length)
                {
                    customPosition = 0;
                }
                prepareDots(customPosition++);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    private void initCategoryRecyclerView() {
        Log.d(TAG, "initCategoryRecyclerView: Initializing recycler view");
        categoryRecyclerView = findViewById(R.id.recycler_view_category);
        categoryRecyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS,
                LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Category");
    }

    public void imageSlideShow()
    {
        final Handler handler = new Handler();
        final  Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (current_position == Integer.MAX_VALUE)
                {
                    current_position = 0;
                }
                imageSlider.setCurrentItem(current_position++,true);
            }
        };
        slideTimer = new Timer();
        slideTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },250,2500);  //delay for starting the slider, period is the time for the next slide
    }

    public void prepareDots(int currentSlidePosition)
    {
        if (dotLayout.getChildCount() > 0)
            dotLayout.removeAllViews();

        ImageView[] dotImage = new ImageView[imageUrls.length];

        for (int i = 0; i< imageUrls.length;i++)
        {
            dotImage[i] = new ImageView(this);
            if (i == currentSlidePosition)
            {
                dotImage[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dot));
            }
            else
            {
                dotImage[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.in_active_dot));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4,0,4,0);
            dotLayout.addView(dotImage[i],layoutParams);
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finishAffinity();;
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        onBackPressed();
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        }else if (id == R.id.nav_logout) {
            mAuth.signOut();
        }
        else if (id == R.id.nav_shopping_cart)
        {
            Intent intent = new Intent(MainActivity.this,ShoppingCartActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_order_history)
        {
            startActivity(new Intent(MainActivity.this,OrderStatusActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
