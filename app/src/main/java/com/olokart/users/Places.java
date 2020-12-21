package com.olokart.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Places extends FragmentActivity implements OnMapReadyCallback {

    LatLng latLng;
    GoogleMap mMap;
    Double currLat, currLong;
    Geocoder geocoder;
    TextInputEditText textView;
    Button savecurrent,savemaually;
   // TextInputEditText house, land;
    FusedLocationProviderClient fusedLocationProviderClient;
    Address address;
    String ulat, ulong, uaddress, uhouse, uland;
    List<Address> addressList;
    DatabaseReference databaseReference;
    SupportMapFragment mapFragment;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        statusCheck();
        savecurrent=findViewById(R.id.change_location);
        savemaually=findViewById(R.id.change_location_manually);
//        house = findViewById(R.id.changeHousenum);
//        land = findViewById(R.id.changeLandmark);

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


        textView = findViewById(R.id.text_current);


        savecurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (TextUtils.isEmpty(textView.getText().toString())) {
                    Snackbar.make(v, "Please move to your current location!", Snackbar.LENGTH_SHORT).show();

                } else {
                   // saveData();
                    HashMap<String, Object> saveMap = new HashMap<>();
                    saveMap.put("oaddress", textView.getText().toString());
                   saveMap.put("olat", currLat);
                   saveMap.put("olong",currLong);
                   saveMap.put("otherhouse","");
                   saveMap.put("otherland","");
                    databaseReference.updateChildren(saveMap);

                    otherchangeHouseDialog();

                }
            }
        });
        savemaually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startActivity(new Intent(Places.this, ChangeLocationActivity.class));
            }


        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.searchLocmap);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                            Toast.makeText(Places.this, "" + latLng, Toast.LENGTH_SHORT).show();

                            //  MarkerOptions options = new MarkerOptions().position(latLng).title("Current Location");
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            // googleMap.addMarker(options);
                        }
                    });
                }
            }
        });


        //  arrayPoints = new ArrayList<LatLng>();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {




                latLng = mMap.getCameraPosition().target;

                currLat = latLng.latitude;
                currLong = latLng.longitude;

           //     Toast.makeText(Places.this, "" + currLat + currLong, Toast.LENGTH_SHORT).show();

                try {
                    addressList = geocoder.getFromLocation(
                            currLat, currLong, 1);
                    if (addressList != null && addressList.size() > 0) {
                        address = addressList.get(0);
                        textView.setText(address.getAddressLine(0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void showDefaultLocation() {
//
        LatLng redmond = new LatLng(47.6739881, -122.121512);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }

        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(15);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    mMap.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };

    private void otherchangeHouseDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(Places.this).inflate(R.layout.change_otherhouseland,viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(Places.this);

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
                     tempaddress = textView.getText().toString();
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
                    startActivity(new Intent(Places.this, HomeActivity.class));
                    finish();

                }

            }
        });
    }
}