package com.olokart.users;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChangeLocationActivity extends AppCompatActivity{

    EditText seach;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    String currenAddr, currLat, currLong, address;
    String ulat, ulong, uaddress, uhouse, uland;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);
        seach=findViewById(R.id.place_change);

        Intent intent = getIntent();
        currenAddr = intent.getStringExtra("location");
//        currentHouse = intent.getStringExtra("housenum");
//        curentLand = intent.getStringExtra("landmark");
        progressBar = findViewById(R.id.spin_kitOrderedChangeLoc);

        Sprite foldingCube = new Circle();
        progressBar.setIndeterminateDrawable(foldingCube);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    ulat = dataSnapshot.child("ulat").getValue().toString();
                    ulong = dataSnapshot.child("ulong").getValue().toString();
                    uaddress = dataSnapshot.child("uaddress").getValue().toString();
                    uhouse = dataSnapshot.child("housenum").getValue().toString();
                    uland = dataSnapshot.child("landmark").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));


        seach.setFocusable(false);
        seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Place.Field> feildlist= Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG,Place.Field.NAME);

                Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,feildlist).build(ChangeLocationActivity.this);
                startActivityForResult(intent,100);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK){
            progressBar.setVisibility(View.VISIBLE);
            Place place=Autocomplete.getPlaceFromIntent(data);

            currLat = String.valueOf(place.getLatLng().latitude);
            currLong = String.valueOf(place.getLatLng().longitude);
            address = String.valueOf(place.getAddress());

            HashMap<String, Object> saveMap = new HashMap<>();
            saveMap.put("oaddress", place.getAddress());
            saveMap.put("olat", place.getLatLng().latitude);
            saveMap.put("olong", place.getLatLng().longitude);
            saveMap.put("otherhouse","");
            saveMap.put("otherland","");
            databaseReference.updateChildren(saveMap);
           otherchangeHouseDialog();
            Toast.makeText(this, "Location Saved", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else if(resultCode== AutocompleteActivity.RESULT_ERROR){
            progressBar.setVisibility(View.GONE);
            Status status=Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(), Toast.LENGTH_SHORT).show();


        }
    }
    private void otherchangeHouseDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(ChangeLocationActivity.this).inflate(R.layout.change_otherhouseland,viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeLocationActivity.this);

        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);

        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        alertDialog.show();

        final TextInputEditText house = dialogView.findViewById(R.id.otherhousenum);
        final TextInputEditText land = dialogView.findViewById(R.id.otherlandnum);
        Button ok = dialogView.findViewById(R.id.otherchangeHouseLand);
       // othrAddrtxt.setText("Enter house no. and landmark for "+textView.getText().toString());

        alertDialog.setCanceledOnTouchOutside(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(house.getText().toString())) {
                    Snackbar.make(v, " Please Enter your house number!", Snackbar.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(land.getText().toString())) {
                    Snackbar.make(v, " Please give your current landmark!", Snackbar.LENGTH_SHORT).show();

                }
                else {
                    String templat , templong, tempaddress, tempHouse, tenpLand;
                    templat = String.valueOf(currLat);
                    templong = String.valueOf(currLong);
                    tempaddress = address;
                    tempHouse =  house.getText().toString();
                    tenpLand = land.getText().toString();

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("otherhouse", uhouse);
                    userMap.put("otherland", uland);
                    userMap.put("ulat", templat);
                    userMap.put("ulong", templong);
                    userMap.put("uaddress", tempaddress);
                    userMap.put("housenum", tempHouse);
                    userMap.put("landmark", tenpLand);
                    userMap.put("olat", ulat);
                    userMap.put("olong", ulong);
                    userMap.put("oaddress", uaddress);
                    databaseReference.updateChildren(userMap);

                    alertDialog.dismiss();

                    startActivity(new Intent(ChangeLocationActivity.this, HomeActivity.class));
                    finish();

                }

            }
        });
    }
}