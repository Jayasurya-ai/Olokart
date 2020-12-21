package com.olokart.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellerProducts extends AppCompatActivity {
    ArrayList<GetSet> getSets;
    ProductsAdapter adapter;
    DatabaseReference databaseReference, storeReference, allCatreference, subCatreference, cartRef;
    RecyclerView recyclerView;
     String getCat;
     ImageView back;
     EditText searchEdit;
    String suid, suba=" ", subb=" ", subc=" ", subd=" ", sube=" ";
    TextView catName, catSubname, sname, scity, noProducts ;
    int total;
    TextView bagPrice;
    Button category, subcatbtn;
    ProgressBar progressBar;

//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products);

        Intent intent = getIntent();
        suid = intent.getStringExtra("suid");

        back = findViewById(R.id.backSproducts);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(SellerProducts.this, HomeActivity.class));
                finish();
            }
        });
        progressBar = findViewById(R.id.spin_kitProducts);

        bagPrice = findViewById(R.id.txtTotalprice);
        Sprite foldingCube = new DoubleBounce();
        progressBar.setIndeterminateDrawable(foldingCube);

        noProducts = findViewById(R.id.emptyProductstxt);
        searchEdit =findViewById(R.id.search_product);
        category = findViewById(R.id.sow_categories);
        subcatbtn = findViewById(R.id.sow_subcategories);
        sname = findViewById(R.id.store_name);
        scity = findViewById(R.id.store_city);



        databaseReference = FirebaseDatabase.getInstance().getReference("Seller Products").child(suid);
        storeReference = FirebaseDatabase.getInstance().getReference("Sellers").child(suid);
        allCatreference = FirebaseDatabase.getInstance().getReference("All Categories");

        progressBar.setVisibility(View.VISIBLE);

        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String snamestr = dataSnapshot.child("sname").getValue().toString();
                String scitystr = dataSnapshot.child("scity").getValue().toString();

                sname.setText(snamestr);
                scity.setText(scitystr);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView = findViewById(R.id.all_recycler);
        catName = findViewById(R.id.cat_name);
        catSubname = findViewById(R.id.subcat_name);

        getSets = new ArrayList<GetSet>();

            recycler();

            BagTotal();
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showCustomDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });


    }






    public void recycler() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                getSets.clear();
                progressBar.setVisibility(View.VISIBLE);

                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                try {
                                    String state = dataSnapshot3.child("product_state").getValue().toString();
                                    if (state.equals("activated")) {
                                        GetSet h = dataSnapshot3.getValue(GetSet.class);
                                        getSets.add(h);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                    }
                    adapter = new ProductsAdapter(SellerProducts.this, getSets);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.GONE);
                if(getSets.isEmpty()){
                    noProducts.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    noProducts.setVisibility(View.GONE);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


    }
    private void filter(String text) {
        try {
            ArrayList<GetSet> filteredList = new ArrayList<>();
            for (GetSet item : getSets) {
                if (item.getProduct_name().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            adapter.filterList(filteredList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCustomDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.alert_cat, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);

        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;

        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        alertDialog.show();
        final TextView all = dialogView.findViewById(R.id.allcat);
        final TextView ricecat = dialogView.findViewById(R.id.ricecat);
        final TextView dalscat = dialogView.findViewById(R.id.dalscat);
        final TextView ediblecat = dialogView.findViewById(R.id.edibleoilcat);
        final TextView householdcat = dialogView.findViewById(R.id.householdcat);
        final TextView brandedproductcat = dialogView.findViewById(R.id.brandcat);
        final ImageView cross = dialogView.findViewById(R.id.crossdailog);

        final TextView masalacat = dialogView.findViewById(R.id.masalalcat);
        final TextView personalcarecat = dialogView.findViewById(R.id.perscarecat);
        final TextView sugarsaltcat = dialogView.findViewById(R.id.sugarsaltcat);
        final TextView bevcat = dialogView.findViewById(R.id.beveragecat);
        final TextView dairy = dialogView.findViewById(R.id.dairycat);
        final TextView vegecat = dialogView.findViewById(R.id.vegecat);
        final TextView other = dialogView.findViewById(R.id.othercat);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                getSets.clear();
             getCat = all.getText().toString();
             catName.setVisibility(View.GONE);
             catSubname.setVisibility(View.GONE);
             recycler();
             subcatbtn.setVisibility(View.GONE);

            }
        });

        ricecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = ricecat.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
              catSubname.setVisibility(View.GONE);
            }
        });

        dalscat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = dalscat.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
               catSubname.setVisibility(View.GONE);
            }
        });

        ediblecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = ediblecat.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
                catSubname.setVisibility(View.GONE);
            }
        });

        householdcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = householdcat.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
                catSubname.setVisibility(View.GONE);
            }
        });
        brandedproductcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = brandedproductcat.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
                catSubname.setVisibility(View.GONE);
            }
        });
        masalacat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = masalacat.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
                catSubname.setVisibility(View.GONE);
            }
        });
        personalcarecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = personalcarecat.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
                catSubname.setVisibility(View.GONE);
            }
        });
        sugarsaltcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = sugarsaltcat.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
                catSubname.setVisibility(View.GONE);
            }
        });
        bevcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = bevcat.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
                catSubname.setVisibility(View.GONE);
            }
        });
        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = dairy.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
                catSubname.setVisibility(View.GONE);
            }
        });
        vegecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = vegecat.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
                catSubname.setVisibility(View.GONE);
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCat = other.getText().toString();
                catName.setText(getCat);
                alertDialog.dismiss();
                catRecycler();
                subcatbtn.setVisibility(View.VISIBLE);
                catName.setVisibility(View.VISIBLE);
                catSubname.setVisibility(View.GONE);
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        subcatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSets.clear();
                allCatreference.child(getCat).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        try {
                            suba = dataSnapshot.child("suba").getValue().toString();
                            subb = dataSnapshot.child("subb").getValue().toString();
                            subc = dataSnapshot.child("subc").getValue().toString();
                            subd = dataSnapshot.child("subd").getValue().toString();
                            sube = dataSnapshot.child("sube").getValue().toString();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        CharSequence options[]=new CharSequence[]{
                                suba, subb, subc, subd, sube
                        };
                        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(SellerProducts.this);
                        builder.setTitle("Choose Sub-Category");


                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                switch (i){
                                    case  0:
                                        if (suba.equals(" ")) {
                                            break;
                                        } else {
                                            getSets.clear();
                                            catSubname.setVisibility(View.VISIBLE);
                                            catSubname.setText(" > " +suba);
                                            subCatreference = FirebaseDatabase.getInstance().getReference("Seller Products").child(suid).child(getCat).child(suba);
                                            subCatRecycler();
                                            break;
                                        }

                                    case 1:
                                        if (subb.equals(" ")) {
                                            break;
                                        } else {
                                            getSets.clear();
                                            catSubname.setVisibility(View.VISIBLE);
                                            catSubname.setText(" > " +subb);
                                            subCatreference = FirebaseDatabase.getInstance().getReference("Seller Products").child(suid).child(getCat).child(subb);
                                            subCatRecycler();
                                            break;
                                        }
                                    case 2:
                                        if (subc.equals(" ")) {
                                            break;
                                        } else {
                                            getSets.clear();
                                            catSubname.setVisibility(View.VISIBLE);
                                            catSubname.setText( " > " +subc);
                                            subCatreference = FirebaseDatabase.getInstance().getReference("Seller Products").child(suid).child(getCat).child(subc);
                                            subCatRecycler();
                                            break;
                                        }
                                    case 3:
                                        if (subd.equals(" ")) {
                                            break;
                                        } else {
                                            getSets.clear();
                                            catSubname.setVisibility(View.VISIBLE);
                                            catSubname.setText(" > " +subd);
                                            subCatreference = FirebaseDatabase.getInstance().getReference("Seller Products").child(suid).child(getCat).child(subd);
                                            subCatRecycler();
                                            break;
                                        }
                                    case 4:
                                        if (sube.equals(" ")) {
                                            break;
                                        } else {
                                            getSets.clear();
                                            catSubname.setVisibility(View.VISIBLE);
                                            catSubname.setText(" > " +sube);
                                            subCatreference = FirebaseDatabase.getInstance().getReference("Seller Products").child(suid).child(getCat).child(sube);
                                            subCatRecycler();
                                            break;
                                        }
                                }

                            }
                        });
                        builder.show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }


    public void catRecycler() {

        getSets.clear();
        //subCatreference = FirebaseDatabase.getInstance().getReference("Seller Products").child(suid).child(getCat);
        databaseReference.child(getCat).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            try {
                                GetSet h = dataSnapshot2.getValue(GetSet.class);
                                getSets.add(h);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    adapter = new ProductsAdapter(SellerProducts.this, getSets);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(getSets.isEmpty()){
                    noProducts.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    noProducts.setVisibility(View.GONE);
                }
                    progressBar.setVisibility(View.GONE);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    public void subCatRecycler() {

        getSets.clear();
        subCatreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.VISIBLE);

                try {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        GetSet h = dataSnapshot1.getValue(GetSet.class);
                        getSets.add(h);
                    }

                    adapter = new ProductsAdapter(SellerProducts.this, getSets);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(getSets.isEmpty()){
                    noProducts.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    noProducts.setVisibility(View.GONE);
                }
                    progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);



    }

    public void openBag(View view) {
//

        try {

            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(suid).exists()) {
                        startActivity(new Intent(SellerProducts.this,BagAvtivity.class));

                    }
                    else {
                        Toast.makeText(SellerProducts.this, "Oops! Bag is empty...", Toast.LENGTH_SHORT).show();
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
    public void BagTotal() {
        try {


            cartRef = FirebaseDatabase.getInstance().getReference("Cart Items").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    total = 0;



                        for (DataSnapshot ds1 : dataSnapshot.child(suid).getChildren()) {

                            GetSet getSet = ds1.getValue(GetSet.class);
                            Integer product_price = Integer.parseInt((getSet.getProduct_price()));
                            total = total + product_price;


                        }

                        bagPrice.setText("₹" + total);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

