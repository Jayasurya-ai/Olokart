package com.viba.olokartusers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    DataSnapshot dataSnapsh;
    Button deliveryStatusbtn;
    String anvesh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_avtivity);

        recyclerView = findViewById(R.id.newOrderRecycler);
        noOrders = findViewById(R.id.noOrderstxt);
        recyclerView.setHasFixedSize(true);
        getSets = new ArrayList<GetSet>();

        progressBar = findViewById(R.id.progressOrders);
        reference = FirebaseDatabase.getInstance().getReference("User Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        GetSet h = dataSnapshot1.getValue(GetSet.class);
                        getSets.add(h);
                }
                progressBar.setVisibility(View.GONE);
                adapter = new OrderAdapter(OrdersAvtivity.this, getSets);
                recyclerView.setAdapter(adapter);

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
}