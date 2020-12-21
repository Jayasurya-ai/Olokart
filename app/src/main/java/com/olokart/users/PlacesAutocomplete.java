package com.olokart.users;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlacesAutocomplete extends AppCompatActivity {
    EditText seach;
    DatabaseReference databaseReference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_autocomplete);
        seach=findViewById(R.id.search_location_bar);

        progressBar = findViewById(R.id.spin_kitOrderedSetLoc);

        Sprite foldingCube = new CubeGrid();
        progressBar.setIndeterminateDrawable(foldingCube);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));


        seach.setFocusable(false);
        seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Place.Field> feildlist= Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG,Place.Field.NAME);

                    Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,feildlist).build(PlacesAutocomplete.this);
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


            HashMap<String,Object>locmap=new HashMap<>();
            locmap.put("ulat", String.valueOf(place.getLatLng().latitude));
            locmap.put("ulong", String.valueOf(place.getLatLng().longitude));
            locmap.put("uaddress",String.valueOf(place.getAddress()));
            databaseReference.updateChildren(locmap);
            startActivity(new Intent(PlacesAutocomplete.this,HomeActivity.class));
            finish();
            Toast.makeText(this, "Location Saved", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else if(resultCode== AutocompleteActivity.RESULT_ERROR){
            progressBar.setVisibility(View.GONE);
            Status status=Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(), Toast.LENGTH_SHORT).show();


        }
    }
}