package com.olokart.users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout linearLayout;
    TextView[] dots;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 550;
    final long PERIOD_MS = 2200;
    int NUM_PAGES = 4;
    RecyclerView recyclerView;
    ArrayList<GetSet> getSets;
    FloatingActionButton floatingActionButton;
    String uaddressstr, oaddressstr,setoaddress,setuaddress;
    private DatabaseReference sellerRef, usercurrentRef, userCartRef, updateRef;
    userAdapter userAdapter;
    Double nearbyDis = 20.00;
    ProgressBar progressBar;
    TextView emptySellerstxt;
    int noofcartitems;
    TextView noofcarditems;
    TextView yourloc;
    private  int request=11;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        noofcarditems=findViewById(R.id.noofcartitems);
        yourloc=findViewById(R.id.userloc);

        checkConnection();
        getSets = new ArrayList<GetSet>();
        sellerRef = FirebaseDatabase.getInstance().getReference("Sellers");

        recyclerView = findViewById(R.id.homeRecycler);
        progressBar = findViewById(R.id.spin_kitOrderedHome);
        Sprite foldingCube = new DoubleBounce();
        progressBar.setIndeterminateDrawable(foldingCube);
        emptySellerstxt = findViewById(R.id.noSellersNearbytxt);

        updateRef = FirebaseDatabase.getInstance().getReference("Update");
        userCartRef = FirebaseDatabase.getInstance().getReference("Cart Items").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        floatingActionButton = findViewById(R.id.bagFloat);

        updateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String update=dataSnapshot.child("user").getValue().toString();
                String version;
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = pInfo.versionName;
                    //  Toast.makeText(HomeActivity.this, ""+version, Toast.LENGTH_SHORT).show();

                    if(!version.equals(update)){
                        updateCustomDialog();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    for (DataSnapshot das : dataSnapshot.getChildren()) {
                        noofcartitems = (int) das.getChildrenCount();
                        noofcarditems.setText(String.valueOf(noofcartitems));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                startActivity(new Intent(HomeActivity.this,BagAvtivity.class));

                            }
                            else {
                                Toast.makeText(HomeActivity.this, "Oops! Bag is empty...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        usercurrentRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        recycler();

        usercurrentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                String lat=dataSnapshot.child("ulat").getValue().toString();
                String lon=dataSnapshot.child("ulong").getValue().toString();
                String housenum = dataSnapshot.child("housenum").getValue().toString();
                String land = dataSnapshot.child("landmark").getValue().toString();
                 setoaddress = dataSnapshot.child("oaddress").getValue().toString();
                 setuaddress = dataSnapshot.child("uaddress").getValue().toString();

                if(lat.equals("0")&&lon.equals("0")|| housenum.equals("") && land.equals("")) {
//                    showCustomDialog();
                    dialog d = new dialog();
                    d.showCustomDialog();
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        usercurrentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    String address = dataSnapshot.child("uaddress").getValue().toString();
                    yourloc.setText(address);
                } catch (Exception e) {
                    e.printStackTrace();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==request){
            Toast.makeText(this, "Start download..", Toast.LENGTH_SHORT).show();
            if(resultCode!=RESULT_OK){
                Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void recycler() {
        try {

            usercurrentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userLoc) {
                  progressBar.setVisibility(View.VISIBLE);
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
                                    String sstatus = dataSnapshot.child("sstatus").getValue().toString();
                                    String permission = dataSnapshot.child("permission").getValue().toString();

                                    final Double lat2 = Double.parseDouble(latstr1);
                                    final Double lon2 = Double.parseDouble(lonstr1);

                                    Double rlon1, rlon2, rlat1, rlat2;

                                    double latDistance = Math.toRadians(lat1 -lat2);
                                    double lngDistance = Math.toRadians(lon1 - lon2);

                                    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                                            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                                            * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

                                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                                    int r= (int) (Math.round(15000.00 * c));
//                                    rlon1 = Math.toRadians(lon1);
//                                    rlon2 = Math.toRadians(lon2);
//                                    rlat1 = Math.toRadians(lat1);
//                                    rlat2 = Math.toRadians(lat2);
//                                    Double dlon = rlon2 - rlon1;
//                                    Double dlat = rlat2 - rlat1;
//                                    Double a = Math.pow(Math.sin(dlat / 2), 2)
//                                            + Math.cos(rlat1) * Math.cos(rlat2)
//                                            * Math.pow(Math.sin(dlon / 2), 2);
//
//                                    Double c = 2 * Math.asin(Math.sqrt(a));
//                                    Integer r = 6371;
//
//                                    // calculate the result
//                                    double result = c * r;

                                    if (sstatus.equals("open") && permission.equals("authorized")) {
                                        if (r <= nearbyDis) {

                                            GetSet h = dataSnapshot.getValue(GetSet.class);
                                            //  overridePendingTransition(0,0);
                                            getSets.add(h);

                                        }
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
                                    progressBar.setVisibility(View.GONE);
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
                "(0-20) Km", "(0-10) Km", "(0-5) Km", "Change Your Location"
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Distance");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int i) {

                switch (i) {
                    case 0:
                        nearbyDis = 20.00;
                        getSets.clear();
                        recycler();
                        break;
                    case 1:
                        nearbyDis = 10.00;
                        getSets.clear();
                        recycler();
                        break;
                    case 2:
                        nearbyDis = 5.00;
                        getSets.clear();
                        recycler();
                        break;
                    case 3:
                                ViewGroup viewGroup = findViewById(android.R.id.content);
                                final View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.editlocationlayout, viewGroup, false);

                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                                builder.setView(dialogView);

                                final AlertDialog alertDialog = builder.create();
                                Window window = alertDialog.getWindow();
                                window.setGravity(Gravity.BOTTOM);

                                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                                alertDialog.show();

                                final TextView homeAddr, other1, other2, other3, other4, other;

                                homeAddr = dialogView.findViewById(R.id.address1);
                                other1 = dialogView.findViewById(R.id.address2);
                                other = dialogView.findViewById(R.id.otherLoc);
                                final RelativeLayout relativeLayouthome = dialogView.findViewById(R.id.deliveryaddd);
                        final RelativeLayout relativeLayoutother = dialogView.findViewById(R.id.defjdfj);

                                try {
                                    usercurrentRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final String housenum = dataSnapshot.child("housenum").getValue().toString();
                                            final String landmark = dataSnapshot.child("landmark").getValue().toString();
                                             uaddressstr = dataSnapshot.child("uaddress").getValue().toString();
                                            oaddressstr = dataSnapshot.child("oaddress").getValue().toString();
                                            final String otherland = dataSnapshot.child("otherland").getValue().toString();
                                            final String otherhouse =dataSnapshot.child("otherhouse").getValue().toString();
                                            final String ulat = dataSnapshot.child("ulat").getValue().toString();
                                            final String ulong = dataSnapshot.child("ulong").getValue().toString();
                                            final String otherlat = dataSnapshot.child("olat").getValue().toString();
                                            final String otherlong = dataSnapshot.child("olong").getValue().toString();
                                            final String uaddress = dataSnapshot.child("uaddress").getValue().toString();
                                            final String oaddress = dataSnapshot.child("oaddress").getValue().toString();

                                            homeAddr.setText(housenum +", "+landmark+",  "+uaddressstr);
                                            other1.setText(otherhouse+", "+otherland+",  "+oaddressstr);
                                            RelativeLayout relativeLayout = dialogView.findViewById(R.id.defjdfj);
                                            if (!oaddressstr.equals("")) {
                                                other.setVisibility(View.VISIBLE);
                                                other1.setVisibility(View.VISIBLE);
                                                relativeLayout.setVisibility(View.VISIBLE);
                                            }
                                            relativeLayouthome.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

//
                                                    HashMap<String, Object> locMap = new HashMap<>();
                                                    locMap.put("ulat", ulat);
                                                    locMap.put("ulong", ulong);
                                                    locMap.put("uaddress", uaddress);

                                                    usercurrentRef.updateChildren(locMap);
                                                    alertDialog.dismiss();
                                                }
                                            });

                                            relativeLayoutother.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

//
                                                    String templat = otherlat;
                                                    String templong = otherlong;
                                                    String tempAddress = oaddress;

                                                    HashMap<String, Object> locMap = new HashMap<>();
                                                    locMap.put("ulat", templat);
                                                    locMap.put("ulong", templong);
                                                    locMap.put("olat", ulat);
                                                    locMap.put("olong", ulong);
                                                    locMap.put("uaddress", tempAddress);
                                                    locMap.put("oaddress", uaddress);

                                                    usercurrentRef.updateChildren(locMap);

                                                    alertDialog.dismiss();
                                                }
                                            });                           }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    Button addAddr = dialogView.findViewById(R.id.loaction_set);
                                    addAddr.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                            startActivity(new Intent(HomeActivity.this, Places.class));
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // startActivity(new Intent(getApplicationContext(), Places.class));
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

      private void updateCustomDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.update_layout, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView ok = dialogView.findViewById(R.id.updatebtn);
        alertDialog.setCanceledOnTouchOutside(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String link = "market://details?id=";
                    try {
                        // play market available
                        getPackageManager()
                                .getPackageInfo("com.olokart.vendors", 0);
                        // not available
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        // should use browser
                        link = "https://play.google.com/store/apps/details?id=com.olokart.vendors";
                    }
                    // starts external action
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(link + getPackageName())));
            }
        });

//
//    private void showCustomDialog() {
//
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//        View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.errorlocation,viewGroup, false);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//
//        builder.setView(dialogView);
//
//       final AlertDialog alertDialog = builder.create();
//        Window window = alertDialog.getWindow();
//        window.setGravity(Gravity.CENTER);
//
//        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
//        alertDialog.show();
//        TextView ok = dialogView.findViewById(R.id.buttonloc);
//        alertDialog.setCanceledOnTouchOutside(false);
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//                startActivity(new Intent(HomeActivity.this, movemap.class));
//                isFinishing();
//            }
//        });
    }

    public class dialog extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }


        protected void showCustomDialog() {

            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.errorlocation,viewGroup, false);

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

            builder.setView(dialogView);

            final AlertDialog alertDialog = builder.create();
            Window window = alertDialog.getWindow();
            window.setGravity(Gravity.CENTER);

            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            alertDialog.show();
            TextView ok = dialogView.findViewById(R.id.buttonloc);
            alertDialog.setCanceledOnTouchOutside(false);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    startActivity(new Intent(HomeActivity.this, movemap.class));
                    isFinishing();
                }
            });
        }

    }
}