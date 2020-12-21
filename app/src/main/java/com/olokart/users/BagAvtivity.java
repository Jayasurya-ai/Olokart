package com.olokart.users;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
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
    RadioButton radioButton;
    String miniOrder, phone, name;
    ProgressBar progressBar;
    TextView uaddress;
    String optionMode="";
    TextView tects;
    String selleraddress, oaddress;
    String useraddress, currentAddress, userHouse, userLand, currentHouse, currentLand;
    Button check_out;
    int total=0;
    String timeid, delivery;
    RadioGroup radioGroup;
    RadioButton pickup,homedelivery;
    CardView cococ;

    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        radioGroup = (RadioGroup) findViewById(R.id.payRadio);
        BagPrice = findViewById(R.id.Bag_price);
        stoname = findViewById(R.id.sleer_name);
        stocity = findViewById(R.id.sleer_city);
        image = findViewById(R.id.vendorstore_image);
        uaddress = findViewById(R.id.addressui);
//        house = findViewById(R.id.setHousenum);
//        land = findViewById(R.id.setLandmark);
        Total = findViewById(R.id.totalpri);
        price = findViewById(R.id.toopri);
        loc_change = findViewById(R.id.editLoc);
        pickup=findViewById(R.id.pickup);
        homedelivery=findViewById(R.id.homeDel);
        cococ = findViewById(R.id.cocococ);
        cococ.setBackgroundResource(R.drawable.cardviewradius);
        tects = findViewById(R.id.texts);
        check_out = findViewById(R.id.check_out);

        recyclerView = findViewById(R.id.bagrecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        getSets = new ArrayList<GetSet>();

        userorderRef = FirebaseDatabase.getInstance().getReference("User Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        source = FirebaseDatabase.getInstance().getReference("Cart Items");
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart Items").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        progressBar = findViewById(R.id.spin_kitCart);

        Sprite foldingCube = new DoubleBounce();
        progressBar.setIndeterminateDrawable(foldingCube);

        loc_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(BagAvtivity.this).inflate(R.layout.editlocationlayout, viewGroup, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(BagAvtivity.this);

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

                try {
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String housenum = dataSnapshot.child("housenum").getValue().toString();
                            final String landmark = dataSnapshot.child("landmark").getValue().toString();
                            final String uaddressstr = dataSnapshot.child("uaddress").getValue().toString();
                            final String oaddressstr = dataSnapshot.child("oaddress").getValue().toString();
                            final String otherland = dataSnapshot.child("otherland").getValue().toString();
                            final String otherhouse =dataSnapshot.child("otherhouse").getValue().toString();
                            homeAddr.setText(housenum +", "+landmark+",  "+uaddressstr);
                            other1.setText(otherhouse+", "+otherland+",  "+oaddressstr);
                            RelativeLayout relativeLayout = dialogView.findViewById(R.id.defjdfj);
                            if (!oaddressstr.equals("")) {
                                other.setVisibility(View.VISIBLE);
                                other1.setVisibility(View.VISIBLE);
                                relativeLayout.setVisibility(View.VISIBLE);
                            }
                            homeAddr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
//                                    house.setText(housenum);
//                                    land.setText(landmark);
                                    alertDialog.dismiss();
                                    uaddress.setText(housenum+", "+landmark+", "+uaddressstr);
                                }
                            });

                            other1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
//                                    house.setText(otherhouse);
//                                    land.setText(otherland);
                                    alertDialog.dismiss();
                                    uaddress.setText(otherhouse+", "+otherland+", "+oaddressstr);
                                }
                            });                           }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Button addAddr = dialogView.findViewById(R.id.loaction_set);
                   addAddr.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // startActivity(new Intent(getApplicationContext(), Places.class));
            }
        });


        mRequestQue = Volley.newRequestQueue(this);

       BagTotal();

        recycler();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phone = dataSnapshot.child("uphone").getValue().toString();
                name = dataSnapshot.child("uname").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tects.setText("Pick Up from");
                uaddress.setText(selleraddress);
                loc_change.setVisibility(View.GONE);
                optionMode = "Pickup";

            }
        });
        homedelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionMode = "";
                tects.setText("Delivery to");
                uaddress.setText(useraddress);
               // loc_change.setVisibility(View.VISIBLE);
                if (delivery.equals("Pick up")) {
                    Toast.makeText(BagAvtivity.this, "This store dosen't provide Home Delivery", Toast.LENGTH_SHORT).show();
                }

                else if (total < Integer.parseInt(miniOrder)) {
                    alertMiniorder();
                } else if (total >= Integer.parseInt(miniOrder)) {

                    optionMode = "Home Delivery";
                    loc_change.setVisibility(View.VISIBLE);


                }

            }
        });






        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (total==0) {
                    optionMode = "";
                   // Toast.makeText(BagAvtivity.this, "Can't place order, Bag value is 0", Toast.LENGTH_SHORT).show();
                }
                else if (phone.equals("")) {

//                    Toast.makeText(BagAvtivity.this, "Please update  your mobile number in profile section....", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(BagAvtivity.this,ProfileActivity.class));
//                    finish();

                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(BagAvtivity.this).inflate(R.layout.editmoblayout, viewGroup, false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(BagAvtivity.this);

                    builder.setView(dialogView);

                    final AlertDialog alertDialog = builder.create();
                    Window window = alertDialog.getWindow();
                    window.setGravity(Gravity.BOTTOM);

                    window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                    alertDialog.show();

                    final TextInputEditText mob = dialogView.findViewById(R.id.editphonemob);
                    Button updt = dialogView.findViewById(R.id.updatemob);
                    Button cancl = dialogView.findViewById(R.id.cancelmob);

                    updt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(mob.getText().toString())) {
                                Toast.makeText(BagAvtivity.this, "Please enter your mobile numeber!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                userRef.child("uphone").setValue(mob.getText().toString());
                                Toast.makeText(BagAvtivity.this, "Mobile number updated!", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }
                    });

                    cancl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }

//
                    if (optionMode.equals("")) {

                         Toast.makeText(BagAvtivity.this, "Choose any one option", Toast.LENGTH_SHORT).show();
//                    } else if (BagPrice.getText().toString().equals("₹0")) {
//                        startActivity(new Intent(BagAvtivity.this,SellerProducts.class));
//
//                       Toast.makeText(BagAvtivity.this, "Can't order, Bag is empty!", Toast.LENGTH_SHORT).show();
                        //  }
                    }
                        else if (optionMode.equals("Home Delivery")){
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(BagAvtivity.this).inflate(R.layout.payments_layout, viewGroup, false);

                        AlertDialog.Builder builder = new AlertDialog.Builder(BagAvtivity.this);

                        builder.setView(dialogView);

                        AlertDialog alertDialog = builder.create();
                        Window window = alertDialog.getWindow();
                        window.setGravity(Gravity.BOTTOM);

                        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                        alertDialog.show();

                        Button payOn, cashOn;

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
                                if (phone.equals("")){
                                    Toast.makeText(BagAvtivity.this, "Complete your profile (required mob no.)", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                                    String saveCurrentDate = currentDate.format(calendar.getTime());
                                    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm");
                                    String saveCurrentTime = currentTime.format(calendar.getTime());

                                    SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmssms");
                                    timeid = timeFormat.format(calendar.getTime());

                                    moveFirebaseRecord(source.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(suid), destn.child(optionMode).child("OLORD" + timeid));

                                    HashMap<String, Object> karthikmap = new HashMap<>();
                                    karthikmap.put("address", uaddress.getText().toString());
                                    karthikmap.put("uname", name);
                                    karthikmap.put("uphone",phone);
                                    karthikmap.put("date", saveCurrentDate);
                                    karthikmap.put("time", saveCurrentTime);
                                    karthikmap.put("order", "placed");
                                    karthikmap.put("orderid", "OLORD" + timeid);
                                    karthikmap.put("reason", "");
                                    karthikmap.put("pmode", "Cash On Delivery");
                                    karthikmap.put("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    karthikmap.put("bagprice", String.valueOf(total));
                                    karthikmap.put("optionmode", optionMode);
                                    karthikmap.put("state", "new");
                                    karthikmap.put("suid", suid);

                                    ordersRef.child(optionMode).child("OLORD" + timeid).updateChildren(karthikmap);
                                    userorderRef.child("OLORD" + timeid).updateChildren(karthikmap);
                                    databaseReference.removeValue();
                                    sendNotification();
                                    //  source.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(BagAvtivity.this, "Order placed!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), OrdersAvtivity.class));
                                    finish();


                                }
                            }
                        });
                    }

                    else if (optionMode.equals("Pickup")){
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(BagAvtivity.this).inflate(R.layout.payments_layout_pickup, viewGroup, false);

                        AlertDialog.Builder builder = new AlertDialog.Builder(BagAvtivity.this);

                        builder.setView(dialogView);

                        AlertDialog alertDialog = builder.create();
                        Window window = alertDialog.getWindow();
                        window.setGravity(Gravity.BOTTOM);

                        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                        alertDialog.show();

                        Button payOn, cashOn;
//                        housenum = dialogView.findViewById(R.id.edithousenum);
//                        landmark = dialogView.findViewById(R.id.editlandnum);

                        payOn = dialogView.findViewById(R.id.payonPickup);
                        cashOn = dialogView.findViewById(R.id.cashonIpckup);

                        payOn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startPayment();

                            }
                        });
                        cashOn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                progressBar.setVisibility(View.VISIBLE);
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                                String saveCurrentDate = currentDate.format(calendar.getTime());
                                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm");
                                String saveCurrentTime = currentTime.format(calendar.getTime());

                                SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmssms");
                                timeid = timeFormat.format(calendar.getTime());

                                moveFirebaseRecord(source.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(suid), destn.child(optionMode).child("OLORD" + timeid));

                                HashMap<String, Object> karthikmap = new HashMap<>();
                                karthikmap.put("address", uaddress.getText().toString());
                                karthikmap.put("date", saveCurrentDate);
                                karthikmap.put("time", saveCurrentTime);
                                karthikmap.put("uname", name);
                                karthikmap.put("uphone", phone);
                                karthikmap.put("order", "placed");
                                karthikmap.put("reason", "");
                                karthikmap.put("pmode", "Cash On delivery");
                                karthikmap.put("orderid", "OLORD" + timeid);
                                karthikmap.put("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                karthikmap.put("bagprice", String.valueOf(total));
                                karthikmap.put("optionmode", optionMode);
                                karthikmap.put("state", "new");
                                karthikmap.put("suid", suid);

                                ordersRef.child(optionMode).child("OLORD" + timeid).updateChildren(karthikmap);
                                userorderRef.child("OLORD" + timeid).updateChildren(karthikmap);
                                databaseReference.removeValue();
                                sendNotification();
                                //  source.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(BagAvtivity.this, "Order placed!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), OrdersAvtivity.class));
                                finish();
                            }
                        });
                    }



            }
        });


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                useraddress = dataSnapshot.child("uaddress").getValue().toString();
                userHouse = dataSnapshot.child("housenum").getValue().toString();
                userLand = dataSnapshot.child("landmark").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
            currentAddress = getIntent().getStringExtra("location");
            currentHouse = getIntent().getStringExtra("housenum");
            currentLand = getIntent().getStringExtra("landmark");
            if (currentAddress == null && optionMode.equals("Home Delivery")) {
                uaddress.setText(userHouse+", "+userLand+useraddress);
//                house.setText(userHouse);
//                land.setText(userLand);
            }
            else if (optionMode.equals("Home Delivery")){
                uaddress.setText(currentHouse+", "+currentLand+currentAddress);
//                house.setText(currentHouse);
//                land.setText(currentLand);
            }

    }

    public void recycler() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                getSets.clear();

                progressBar.setVisibility(View.VISIBLE);
                try {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                            GetSet h = dataSnapshot2.getValue(GetSet.class);
                            getSets.add(h);

                            suid = dataSnapshot1.getKey();
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
                                    delivery = dataSnapshot.child("delivery").getValue().toString();
                                    stoname.setText(storeName);
                                    stocity.setText(storecity);
                                    Picasso.get().load(simage).placeholder(R.drawable.open)
                                            .error(R.drawable.open).into(image);
                                    selleraddress = dataSnapshot.child("saddress").getValue().toString();


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
//

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter = new BagAdapter(BagAvtivity.this, getSets);
                recyclerView.setAdapter(adapter);


                if (getSets.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
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


                try {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        for (DataSnapshot ds1 : ds.getChildren()) {

                            GetSet getSet = ds1.getValue(GetSet.class);
//                    String Bag=ds.child("product_price").getValue().toString();
//                    Toast.makeText(BagAvtivity.this, ""+Bag, Toast.LENGTH_SHORT).show();
                            Integer product_price = Integer.parseInt((getSet.getProduct_price()));
                            total = total + product_price;


                        }


                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                try {

                    adapter = new BagAdapter(BagAvtivity.this, getSets);
                    recyclerView.setAdapter(adapter);

                    BagPrice.setText(String.valueOf("₹" + total));
                    Total.setText(String.valueOf("₹" + total));
                    price.setText(String.valueOf("₹" + total));
                } catch (Exception e) {
                    e.printStackTrace();
                }


//                home.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (delivery.equals("Pick up")) {
//                            Toast.makeText(BagAvtivity.this, "This store dosen't provide Home Delivery", Toast.LENGTH_SHORT).show();
//                        }
//                        else if (total >= Integer.parseInt(miniOrder)) {
//                            home.setBackgroundColor(getResources().getColor(R.color.lightblack));
//                            pick.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                            tects.setText("Delivery to");
//                            optionMode= "Home Delivery";
//                            uaddress.setText(useraddress);
//                          //  Toast.makeText(BagAvtivity.this, "Error", Toast.LENGTH_SHORT).show();
//                            loc_change.setVisibility(View.VISIBLE);
//
//                        }
//                        else {
//                            alertMiniorder();
//                        }
//                    }
//                });
//
//                pick.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        home.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                        pick.setBackgroundColor(getResources().getColor(R.color.lightblack));
//                        tects.setText("Pick Up from");
//                        optionMode = "Pickup";
//                        uaddress.setText(selleraddress);
//                        loc_change.setVisibility(View.GONE);
//
//
//                    }
//                });

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
            options.put("name", "Olokart");
            options.put("currency", "INR");
            options.put("amount", total*100);
            options.put("theme.color", "#47a626");
            checkout.open(activity, options);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        if (phone.equals("")) {
            Toast.makeText(this, "Complete your profile (required mob. no)", Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
            String saveCurrentDate = currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss:ms");
            String saveCurrentTime = currentTime.format(calendar.getTime());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmssms");
             timeid = timeFormat.format(calendar.getTime());

            moveFirebaseRecord(source.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(suid), destn.child(optionMode).child("OLORD"+timeid));

            HashMap<String, Object> karthikmap = new HashMap<>();
            karthikmap.put("address", uaddress.getText().toString());
            karthikmap.put("date", saveCurrentDate);
            karthikmap.put("time", saveCurrentTime);
            karthikmap.put("orderid", "OLORD"+timeid);
            karthikmap.put("uname", name);
            karthikmap.put("order", "placed");
            karthikmap.put("reason", "");
            karthikmap.put("uphone", phone);
            karthikmap.put("uuid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            karthikmap.put("bagprice", total+"");
            karthikmap.put("optionmode", optionMode);
            karthikmap.put("pmode", "Paid Online");
            karthikmap.put("state","new");
            karthikmap.put("suid",suid);



            ordersRef.child(optionMode).child("OLORD"+timeid).updateChildren(karthikmap);
            userorderRef.child("OLORD"+timeid).updateChildren(karthikmap);
            databaseReference.removeValue();
            sendNotification();
            Toast.makeText(BagAvtivity.this, "Order placed!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(BagAvtivity.this, OrdersAvtivity.class));
            finish();

        }
    }

    private void sendNotification() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+suid);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","New Order Placed");
            notificationObj.put("body", "Orderid is "+"OLORD"+timeid);
         //   notificationObj.put("icon",R.drawable.olologo);
            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAA2vX6iik:APA91bFj3SPKHBN8dmzXpuBOcCv3flSGLWpV17OlDdsix56fiu4UWwbhX4fnvef9HkU3-A90Ts1wsDtFG1QweW_kVvDLPxb_P5jwORDyX5zLeGHZYibabo8Qsdzfcht3N4GjEELsvX9b");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
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
        orderValue.setText("₹"+miniOrder);
        orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BagAvtivity.this, SellerProducts.class).putExtra("suid", suid));
    }
}