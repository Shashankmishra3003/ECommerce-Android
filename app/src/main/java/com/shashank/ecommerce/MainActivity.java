package com.shashank.ecommerce;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.ecommerce.adapter.CategoryRecyclerViewAdapter;
import com.shashank.ecommerce.adapter.ImageSliderAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
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
    private ArrayList<String> categoryImageUrls = new ArrayList<>();
    private ArrayList<String> categoryNames = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        //ViewPager
        imageSlider = findViewById(R.id.image_slider);
        //dot layout
        dotLayout = findViewById(R.id.dot_container);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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

        //Setting the Image Slider
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

        initCategoryData();
    }

    private void initCategoryData() {
        Log.d(TAG, "initcategoryData: Getting category");
        categoryImageUrls.add("https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg");
        categoryNames.add("Category One");
        categoryImageUrls.add("https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg");
        categoryNames.add("Category Two");
        categoryImageUrls.add("https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg");
        categoryNames.add("Category Three");
        categoryImageUrls.add("https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg");
        categoryNames.add("Category Four");
        categoryImageUrls.add("https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg");
        categoryNames.add("Category Five");

        initCategoryRecyclerView();
    }

    private void initCategoryRecyclerView() {
        Log.d(TAG, "initCategoryRecyclerView: Initializing recycler view");
        RecyclerView categoryRecyclerView = findViewById(R.id.recycler_view_category);
        CategoryRecyclerViewAdapter categoryRecyclerViewAdapter =
                new CategoryRecyclerViewAdapter(this,categoryNames,categoryImageUrls);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS,
                LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        categoryRecyclerView.setAdapter(categoryRecyclerViewAdapter);
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

        ImageView dotImage[] = new ImageView[imageUrls.length];

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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
