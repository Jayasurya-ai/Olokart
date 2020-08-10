package com.viba.olokartusers;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class BagAvtivity extends AppCompatActivity implements PaymentResultListener {

    ArrayList<GetSet> getSets;
    BagAdapter adapter;
    DatabaseReference sellerRef, userRef, databaseReference, ordersRef, source, destn, userorderRef;
    RecyclerView recyclerView;
    String suid;
    TextView BagPrice, Total, price;
    TextView stoname, stocity;
    ImageView image;
    Button home, pick;
    TextView loc_change;
    String miniOrder;
    AlertDialog alertDialog;
    TextView uaddress;
    String optionMode="";
    private String inString;
    TextInputEditText housenum, landmark;
    TextView tects;
    String selleraddress;
    String useraddress, currentAddress;
    Button check_out;
   // String dateid, timeid;
    int total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        BagPrice = findViewById(R.id.Bag_price);
        stoname = findViewById(R.id.sleer_name);
        stocity = findViewById(R.id.sleer_city);
        image = findViewById(R.id.vendorstore_image);
        uaddress = findViewById(R.id.addressui);
        Total = findViewById(R.id.totalpri);
        price = findViewById(R.id.toopri);
        loc_change = findViewById(R.id.loc_change);
        home = findViewById(R.id.home_delivery);
        pick = findViewById(R.id.pick_up_from);
        tects = findViewById(R.id.texts);
        check_out = findViewById(R.id.check_out);

        recyclerView = findViewById(R.id.bagrecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        getSets = new ArrayList<GetSet>();

        userorderRef = FirebaseDatabase.getInstance().getReference("User Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        source = FirebaseDatabase.getInstance().getReference("Cart Items");
        // destn = FirebaseDatabase.getInstance().getReference("Seller Orders").child(suid);
        //ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(suid);
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart Items").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        loc_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Places.class));
            }
        });

        BagTotal();

        recycler();

        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionMode.equals("")) {

                    Toast.makeText(BagAvtivity.this, "Choose any one option", Toast.LENGTH_SHORT).show();
                }
                else if (optionMode.equals("Home Delivery")&& total<Integer.parseInt(miniOrder)) {
                        alertMiniorder();
                }
                else {
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(BagAvtivity.this).inflate(R.layout.payments_layout, viewGroup, false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(BagAvtivity.this);

                    builder.setView(dialogView);

                    AlertDialog alertDialog = builder.create();
                    Window window = alertDialog.getWindow();
                    window.setGravity(Gravity.BOTTOM);

                    window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                    alertDialog.show();

                    Button payOn, cashOn;
                    housenum = dialogView.findViewById(R.id.edithousenum);
                    landmark = dialogView.findViewById(R.id.editlandnum);

                    payOn = dialogView.findViewById(R.id.payon);
                    cashOn = dialogView.findViewById(R.id.cashon);

                    payOn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startPayment();

                        }
                    });
                    cashOn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(housenum.getText().toString())) {
                                Toast.makeText(BagAvtivity.this, "Please enter house number", Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(landmark.getText().toString())) {
                                Toast.makeText(BagAvtivity.this, "Please enter land mark", Toast.LENGTH_SHORT).show();
                            } else {
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat currentDate =  new SimpleDateFormat("yyyy-MM-dd");
                                String saveCurrentDate = currentDate.format(calendar.getTime());
                                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm");
                                String saveCurrentTime = currentTime.format(calendar.getTime());

                                DateFormat dateFormat = new SimpleDateFormat("ddmmyyyy");
                                String dateid = dateFormat.format(calendar.getTime());
                                SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmss");
                                String timeid = timeFormat.format(calendar.getTime());

                                moveFirebaseRecord(source.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(suid), destn.child(optionMode).child("OLORD"+timeid+dateid));

                                HashMap<String, Object> karthikmap = new HashMap<>();
                                karthikmap.put("addresss", uaddress.getText().toString());
                                karthikmap.put("date", saveCurrentDate);
                                karthikmap.put("time", saveCurrentTime);
                                karthikmap.put("orderid", "OLORD"+timeid+dateid);
                                karthikmap.put("landmark", housenum.getText().toString());
                                karthikmap.put("housenum", landmark.getText().toString());
                                karthikmap.put("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                karthikmap.put("bagprice", String.valueOf(total));
                                karthikmap.put("optionmode", optionMode);
                                karthikmap.put("state","new");
                                karthikmap.put("suid",suid);

                                ordersRef.child(optionMode).child("OLORD"+timeid+dateid).updateChildren(karthikmap);
                                userorderRef.child("OLORD"+timeid + dateid).updateChildren(karthikmap);
                                databaseReference.removeValue();
                              //  source.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                Toast.makeText(BagAvtivity.this, "Order placed!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), OrdersAvtivity.class));
                                finish();


                            }
                        }
                    });
                }


            }
        });


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                useraddress = dataSnapshot.child("uaddress").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
            currentAddress = getIntent().getStringExtra("location");
            if (currentAddress == null) {
                uaddress.setText(useraddress);
            }
            else {
                uaddress.setText(currentAddress);
            }


    }

    public void recycler() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    destn = FirebaseDatabase.getInstance().getReference("Seller Orders").child(suid);
//                    ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(suid);
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                        GetSet h = dataSnapshot2.getValue(GetSet.class);
                        getSets.add(h);

                        suid = dataSnapshot1.getKey();
                       // Toast.makeText(BagAvtivity.this, ""+suid, Toast.LENGTH_SHORT).show();
                        sellerRef = FirebaseDatabase.getInstance().getReference("Sellers").child(suid);
                        destn = FirebaseDatabase.getInstance().getReference("Seller Orders").child(suid);
                        ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(suid);

                        sellerRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String storeName = dataSnapshot.child("sname").getValue().toString();
                                String storecity = dataSnapshot.child("scity").getValue().toString();
                                String simage = dataSnapshot.child("simage").getValue().toString();
                                miniOrder = dataSnapshot.child("minorder").getValue().toString();
                                stoname.setText(storeName);
                                stocity.setText(storecity);
                                Picasso.get().load(simage).into(image);
                                selleraddress = dataSnapshot.child("saddress").getValue().toString();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
//
                    adapter = new BagAdapter(BagAvtivity.this, getSets);
                    recyclerView.setAdapter(adapter);
                }


                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    public void BagTotal() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total = 0;
                getSets.clear();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {

                        GetSet getSet = ds1.getValue(GetSet.class);
//                    String Bag=ds.child("product_price").getValue().toString();
//                    Toast.makeText(BagAvtivity.this, ""+Bag, Toast.LENGTH_SHORT).show();
                        Integer product_price = Integer.parseInt((getSet.getProduct_price()));
                        total = total + product_price;

                    }

                }
                adapter = new BagAdapter(BagAvtivity.this, getSets);
                recyclerView.setAdapter(adapter);
                BagPrice.setText(String.valueOf(total) + "₹");
                Total.setText(String.valueOf(total) + "₹");
                price.setText(String.valueOf(total) + "₹");

                home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (total >= Integer.parseInt(miniOrder)) {
                            home.setBackgroundColor(getResources().getColor(R.color.lightblack));
                            pick.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            tects.setText("Delivery to");
                            optionMode= "Home Delivery";
                            uaddress.setText(useraddress);
                          //  Toast.makeText(BagAvtivity.this, "Error", Toast.LENGTH_SHORT).show();
                            loc_change.setVisibility(View.VISIBLE);

                        }
                        else {
                            alertMiniorder();
                        }
                    }
                });

                pick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        home.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        pick.setBackgroundColor(getResources().getColor(R.color.lightblack));
                        tects.setText("Pick Up from");
                        optionMode = "Pickup";
                        uaddress.setText(selleraddress);
                        loc_change.setVisibility(View.GONE);


                    }
                });

                //  Log.d("TAG", total + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void startPayment() {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_89DugIVu4nu623");
        checkout.setImage(R.drawable.olologo);
        Activity activity = this;
        try {
            JSONObject options = new JSONObject();
            options.put("description", "Olokart");
            options.put("currency", "INR");
            options.put("amount", total*100);
            checkout.open(activity, options);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        if (TextUtils.isEmpty(housenum.getText().toString())) {
            Toast.makeText(BagAvtivity.this, "Please enter house number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(landmark.getText().toString())) {
            Toast.makeText(BagAvtivity.this, "Please enter land mark", Toast.LENGTH_SHORT).show();
        }
        else {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
            String saveCurrentDate = currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss:ms");
            String saveCurrentTime = currentTime.format(calendar.getTime());

            DateFormat dateFormat = new SimpleDateFormat("ddmmyyyy");
            String dateid = dateFormat.format(calendar.getTime());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmss");
            String timeid = timeFormat.format(calendar.getTime());

            HashMap<String, Object> karthikmap = new HashMap<>();
            karthikmap.put("addresss", uaddress.getText().toString());
            karthikmap.put("date", saveCurrentDate);
            karthikmap.put("time", saveCurrentTime);
            karthikmap.put("orderid", "OLORD"+timeid+dateid);
            karthikmap.put("landmark", housenum.getText().toString());
            karthikmap.put("housenum", landmark.getText().toString());
            karthikmap.put("uuid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            karthikmap.put("bagprice", total+"");
            karthikmap.put("optionmode", optionMode);
            karthikmap.put("state","new");
            karthikmap.put("suid",suid);


            ordersRef.child(optionMode).child("OLORD"+timeid+dateid).updateChildren(karthikmap);
            userorderRef.child("OLORD"+timeid+dateid).updateChildren(karthikmap);
            databaseReference.removeValue();
            Toast.makeText(BagAvtivity.this, "Order placed!", Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();

    }
    public void moveFirebaseRecord(DatabaseReference fromPath, final DatabaseReference toPath)
    {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(),
                        new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    System.out.println("Copy failed");
                                } else {
                                    System.out.println("Success");
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void alertMiniorder() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(BagAvtivity.this).inflate(R.layout.miniorder_alert, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(BagAvtivity.this);

        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);

        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        alertDialog.show();

         TextView orderValue, orderbtn;
        orderValue = dialogView.findViewById(R.id.miniOrderValue);
        orderbtn = dialogView.findViewById(R.id.orderOk);
        orderValue.setText(miniOrder);
        orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}