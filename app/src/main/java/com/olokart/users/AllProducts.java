package com.olokart.users;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllProducts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference, orderRef, sellerOrderRef;
    ArrayList<GetSet> getSets;
    OrdersPlacedAdapter adapter;
    TextView bagprice;
    ProgressBar progressBar;
    ImageView back;
    TextView cancel, paymentMode;
    String datetime,optionmode, suid,order, state;
    TextView orderId, sellerAddr, userAddr, status, totalPrice;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        bagprice = findViewById(R.id.bagrs);

        progressBar = findViewById(R.id.spin_kitOrderedProducts);

        back = findViewById(R.id.backallProducts);

        paymentMode = findViewById(R.id.paymentMode);
        cancel = findViewById(R.id.cancelOrder);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllProducts.this, OrdersAvtivity.class));
                finish();
            }
        });

        Sprite foldingCube = new DoubleBounce();
        progressBar.setIndeterminateDrawable(foldingCube);
        Intent i = getIntent();
        datetime = i.getStringExtra("orderid");
        suid = i.getStringExtra("suid");
        optionmode = i.getStringExtra("optionmode");
        orderRef = FirebaseDatabase.getInstance().getReference("User Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(datetime);
        reference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(suid).child(optionmode);

        recyclerView = findViewById(R.id.ordersplacedRecycler);
        recyclerView.setHasFixedSize(true);

        orderId = findViewById(R.id.orderid);
        sellerAddr = findViewById(R.id.vendorlocation);
        userAddr = findViewById(R.id.userlocation);
        status = findViewById(R.id.orderstatus);
        totalPrice = findViewById(R.id.totalpri);

        getSets = new ArrayList<GetSet>();

        sellerOrderRef = FirebaseDatabase.getInstance().getReference("Orders").child(suid).child(optionmode).child(datetime);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllProducts.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        reference.child(datetime).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        progressBar.setVisibility(View.VISIBLE);
                        GetSet h = dataSnapshot1.getValue(GetSet.class);
                        getSets.add(h);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                orderRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        try {
                            String Bagprice = dataSnapshot.child("bagprice").getValue().toString();
                            state = dataSnapshot.child("state").getValue().toString();
                            String suid = dataSnapshot.child("suid").getValue().toString();
                            String uuid = dataSnapshot.child("uuid").getValue().toString();
                            String orrid = dataSnapshot.child("orderid").getValue().toString();
                            String mode = dataSnapshot.child("pmode").getValue().toString();
                            order = dataSnapshot.child("order").getValue().toString();
                            orderId.setText(orrid);
                            bagprice.setText("₹" + Bagprice);
                            paymentMode.setText(mode);

                            totalPrice.setText("₹" + Bagprice);
                            if (state.equals("new") && order.equals("placed")) {
                                status.setText("Order Placed");
                            } else if (state.equals("ongoing") && order.equals("placed")) {
                                status.setText("Delivery Started");
                            } else if (state.equals("past") && order.equals("placed")) {
                                status.setText("Delivered");
                            }
                            else if (state.equals("new") && order.equals("cancelled")) {
                                status.setText("Order cancelled");
                            }
//                            }
//                            else if (state.equals("ongoing") && order.equals("cancelled")) {
//                                status.setText("Order cancelled");
//                            } else if (state.equals("past") && order.equals("cancelled")) {
//                                status.setText("Order cancelled");
//                            }
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Sellers").child(suid);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String vendorLoc = dataSnapshot.child("saddress").getValue().toString();
                                    sellerAddr.setText(vendorLoc);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uuid);
                            userRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String userloc = dataSnapshot.child("uaddress").getValue().toString();
                                    userAddr.setText(userloc);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (state.equals("ongoing") || state.equals("past")) {
                                        ViewGroup viewGroup = findViewById(android.R.id.content);
                                        View dialogView = LayoutInflater.from(AllProducts.this).inflate(R.layout.alert_cancel, viewGroup, false);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(AllProducts.this);

                                        builder.setView(dialogView);

                                        final AlertDialog alertDialog = builder.create();
                                        Window window = alertDialog.getWindow();
                                        window.setGravity(Gravity.CENTER);

                                        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        alertDialog.show();

                                        TextView dismiss = dialogView.findViewById(R.id.dismissNum);
//                                        dial.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//
//                                                Uri u = Uri.parse("tel:" +"9346850529" );
//                                                Intent i = new Intent(Intent.ACTION_DIAL, u);
//                                                try
//                                                {
//                                                    startActivity(i);
//                                                }
//                                                catch (SecurityException s)
//                                                {
//                                                    Toast.makeText(AllProducts.this, s.getMessage(), Toast.LENGTH_LONG)
//                                                            .show();
//                                                }
//                                            }
//                                        });
                                        dismiss.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertDialog.dismiss();
                                            }
                                        });

                                    }

                                    else if (order.equals("placed") && state.equals("new")) {
                                        ViewGroup viewGroup = findViewById(android.R.id.content);
                                        View dialogView = LayoutInflater.from(AllProducts.this).inflate(R.layout.alert_bag, viewGroup, false);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(AllProducts.this);

                                        builder.setView(dialogView);

                                        final AlertDialog alertDialog = builder.create();
                                        Window window = alertDialog.getWindow();
                                        window.setGravity(Gravity.CENTER);

                                        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        alertDialog.show();

                                        TextView ok, dismiss;
                                        final EditText cancelEdit = dialogView.findViewById(R.id.canceltxt);
                                        ok = dialogView.findViewById(R.id.cancelConfirm);
                                        dismiss = dialogView.findViewById(R.id.dismissOrder);

                                        dismiss.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertDialog.dismiss();
                                            }
                                        });
                                        ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (TextUtils.isEmpty(cancelEdit.getText().toString())) {
                                                    Toast.makeText(AllProducts.this, "Please enter your reason for cancellation!", Toast.LENGTH_SHORT).show();
                                                }

                                                else {
                                                    HashMap<String, Object> orderMap = new HashMap<>();
                                                    orderMap.put("order", "cancelled");
                                                    orderMap.put("reason", cancelEdit.getText().toString());
                                                    sellerOrderRef.updateChildren(orderMap);
                                                    orderRef.updateChildren(orderMap);
                                                    Toast.makeText(AllProducts.this, "Your order is cancelled sucessfully!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(AllProducts.this, OrdersAvtivity.class));
                                                    finish();
                                                    alertDialog.dismiss();
                                                }

                                            }
                                        });
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                adapter = new OrdersPlacedAdapter(AllProducts.this, getSets);
                recyclerView.setAdapter(adapter);

                if(getSets.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendNotification() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+suid);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Order Cancelled");
            notificationObj.put("body", "Customer Orderid is "+"OLORD"+datetime);
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
}
