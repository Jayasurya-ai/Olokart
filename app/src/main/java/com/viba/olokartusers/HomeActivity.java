package com.viba.olokartusers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;

public class HomeActivity extends AppCompatActivity {

    AppBarConfiguration mAppBarConfiguration;
    String suid;
    DatabaseReference databaseReference;
    TextView sname, szip, scity;
    ViewPager viewPager;
    LinearLayout linearLayout;
    TextView[] dots;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 550;
    final long PERIOD_MS = 2200;
    int NUM_PAGES = 4;
    RecyclerView recyclerView;
    DatabaseReference reference;
    String category, pname;
    FirebaseAuth firebaseAuth;
    ArrayList<GetSet> getSets;
    FloatingActionButton floatingActionButton;

    private DatabaseReference UserRef, sellerRef, usercurrentRef, userCartRef;
    SwipeRefreshLayout refreshLayout;
    userAdapter userAdapter;
    Double nearbyDis = 6.00, selLat, selLong;
    EditText editText;
    Button next, prev, finish;
    private Query query;
    ImageView locImage;
    ProgressBar progressBar;
    TextView emptySellerstxt;
    AlertDialog alertDialog;
    // ProgressBar progressBar;
    TextView Novendors;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        checkConnection();
        getSets = new ArrayList<GetSet>();
        sellerRef = FirebaseDatabase.getInstance().getReference("Sellers");
        userCartRef = FirebaseDatabase.getInstance().getReference("Cart Items").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        recyclerView = findViewById(R.id.homeRecycler);
        progressBar = findViewById(R.id.progressHome);
        emptySellerstxt = findViewById(R.id.noSellersNearbytxt);

        floatingActionButton = findViewById(R.id.bagFloat);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                userCartRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            startActivity(new Intent(getApplicationContext(), BagAvtivity.class));
                        }
                        else {
                            Snackbar.make(v, "Bag is Empty!", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        usercurrentRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        recycler();

        usercurrentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lat=dataSnapshot.child("ulat").getValue().toString();
                String lon=dataSnapshot.child("ulong").getValue().toString();
                if(lat.equals("0")&&lon.equals("0")){
                    showCustomDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_home);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_orders:
                        startActivity(new Intent(getApplicationContext(), OrdersAvtivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
        Toolbar toolbar = findViewById(R.id.toolHome);


        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new

                Timer(); // This will create a new Thread
        timer.schedule(new

                               TimerTask() { // task to be scheduled
                                   @Override
                                   public void run() {
                                       handler.post(Update);
                                   }
                               }, DELAY_MS, PERIOD_MS);
        viewPager =

                findViewById(R.id.homeViewpager);

        final SliderAdapter sliderAdapter = new SliderAdapter(this);
        linearLayout =

                findViewById(R.id.homeLin);
        viewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        viewPager.addOnPageChangeListener(pageChangeListener);
        //

    }

    public void recycler() {
        try {

            usercurrentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userLoc) {
                    final String userLat = userLoc.child("ulat").getValue().toString();
                    final String userLong = userLoc.child("ulong").getValue().toString();

                    final Double lat1 = Double.parseDouble(userLat);
                    final Double lon1 = Double.parseDouble(userLong);

                    sellerRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot sellerLoc) {

                            getSets.clear();
                            progressBar.setVisibility(View.VISIBLE);
                            for (DataSnapshot dataSnapshot : sellerLoc.getChildren()) {

                                if (dataSnapshot.exists()) {

                                    String latstr1 = dataSnapshot.child("slat").getValue().toString();
                                    String lonstr1 = dataSnapshot.child("slong").getValue().toString();

                                    final Double lat2 = Double.parseDouble(latstr1);
                                    final Double lon2 = Double.parseDouble(lonstr1);

                                    Double rlon1, rlon2, rlat1, rlat2;
                                    rlon1 = Math.toRadians(lon1);
                                    rlon2 = Math.toRadians(lon2);
                                    rlat1 = Math.toRadians(lat1);
                                    rlat2 = Math.toRadians(lat2);
                                    Double dlon = rlon2 - rlon1;
                                    Double dlat = rlat2 - rlat1;
                                    Double a = Math.pow(Math.sin(dlat / 2), 2)
                                            + Math.cos(rlat1) * Math.cos(rlat2)
                                            * Math.pow(Math.sin(dlon / 2), 2);

                                    Double c = 2 * Math.asin(Math.sqrt(a));
                                    Integer r = 8000;

                                    // calculate the result
                                    double result = c * r;

                                    if (result <= nearbyDis) {

                                        GetSet h = dataSnapshot.getValue(GetSet.class);
                                        //  overridePendingTransition(0,0);
                                        getSets.add(h);

                                    }
                                }


                                progressBar.setVisibility(View.GONE);
                                userAdapter = new userAdapter(HomeActivity.this, getSets);

                                recyclerView.setAdapter(userAdapter);

                                if(getSets.isEmpty()){
                                    emptySellerstxt.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                }
                                else{
                                    recyclerView.setVisibility(View.VISIBLE);
                                    emptySellerstxt.setVisibility(View.GONE);
                                }

                                // progressBar.setVisibility(View.GONE);

                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAdapter);


    }

    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (null == activeNetwork) {
            Toast.makeText(HomeActivity.this, "No Internet Connection!",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void nearbySellers(View view) {
        CharSequence options[] = new CharSequence[]{
                "(0-6) Km", "(0-4) Km", "(0-2) Km", "Change Your Location"
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Distance");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                switch (i) {
                    case 0:
                        nearbyDis = 6.00;
                        getSets.clear();
                        recycler();
                        break;
                    case 1:
                        nearbyDis = 4.00;
                        getSets.clear();
                        recycler();
                        break;
                    case 2:
                        nearbyDis = 2.00;
                        getSets.clear();
                        recycler();
                        break;
                    case 3:
                        startActivity(new Intent(HomeActivity.this, movemap.class));
                        break;

                }
            }
        });
        builder.show();

    }

    public void addDotsIndicator(int position) {

        dots = new TextView[4];
        linearLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorAccent));

            linearLayout.addView(dots[i]);

        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }


    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);

            currentPage = i;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void showCustomDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.errorlocation, viewGroup, false);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView ok = dialogView.findViewById(R.id.buttonloc);
        alertDialog.setCanceledOnTouchOutside(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, movemap.class));
                HomeActivity.this.finish();
            }
        });
    }
}