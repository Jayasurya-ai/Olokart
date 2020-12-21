package com.olokart.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersAvtivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    private DatabaseReference reference;
    ArrayList<GetSet> getSets;
    TextView noOrders;
    OrderAdapter adapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_avtivity);

        recyclerView = findViewById(R.id.newOrderRecycler);
        noOrders = findViewById(R.id.noOrderstxt);
        recyclerView.setHasFixedSize(true);
        back = findViewById(R.id.backImage);
        getSets = new ArrayList<GetSet>();

        progressBar = findViewById(R.id.spin_kitOrders);
        Sprite foldingCube = new DoubleBounce();
        progressBar.setIndeterminateDrawable(foldingCube);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(OrdersAvtivity.this, HomeActivity.class));
                finish();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("User Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getSets.clear();
                progressBar.setVisibility(View.VISIBLE);
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                        GetSet h = dataSnapshot1.getValue(GetSet.class);
                        getSets.add(h);
                    }


                progressBar.setVisibility(View.GONE);
                adapter = new OrderAdapter(OrdersAvtivity.this, getSets);
                recyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(getSets.isEmpty()){
                    noOrders.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    noOrders.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_orders);
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);

//        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#47a626")));
//        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#47a626")));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(OrdersAvtivity.this,HomeActivity.class));
//        finish();
//    }
}