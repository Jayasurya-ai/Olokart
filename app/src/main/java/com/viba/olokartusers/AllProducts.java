package com.viba.olokartusers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AllProducts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference,userref, orderRef;
    ArrayList<GetSet> getSets;
    OrdersPlacedAdapter adapter;
    TextView bagprice;
    ProgressBar progressBar;
   // Button accept,decline;
    String userid;
    String uphone;
    String datetime,optionmode, suid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        bagprice = findViewById(R.id.bagrs);


        Intent i = getIntent();
        datetime = i.getStringExtra("orderid");
        suid = i.getStringExtra("suid");
        optionmode = i.getStringExtra("optionmode");
        orderRef = FirebaseDatabase.getInstance().getReference("User Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(datetime);
        reference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(suid).child(optionmode);

        progressBar = findViewById(R.id.progressAllproducts);

        recyclerView = findViewById(R.id.ordersplacedRecycler);
        recyclerView.setHasFixedSize(true);


        getSets = new ArrayList<GetSet>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllProducts.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        reference.child(datetime).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    progressBar.setVisibility(View.VISIBLE);
                    GetSet h = dataSnapshot1.getValue(GetSet.class);
                    getSets.add(h);
                }
                // Toast.makeText(AllProducts.this, ""+datetime, Toast.LENGTH_SHORT).show();

                orderRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String Bagprice = dataSnapshot.child("bagprice").getValue().toString();
                        bagprice.setText(Bagprice + "₹");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                progressBar.setVisibility(View.GONE);
                //      }
                adapter = new OrdersPlacedAdapter(AllProducts.this, getSets);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
