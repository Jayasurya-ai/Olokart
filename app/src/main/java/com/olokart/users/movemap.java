package com.olokart.users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class movemap extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    LatLng latLng;
    GoogleMap mMap;
    double currLat, currLong;
    TextInputEditText textView;
    Button savecurrent,savemaually;
    FusedLocationProviderClient fusedLocationProviderClient;
    Address address;
    List<Address> addressList;
    DatabaseReference databaseReference;
    SupportMapFragment mapFragment;
   // TextInputEditText houseNum, landMark;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movemap);
        statusCheck();
        savecurrent=findViewById(R.id.set_location);
        savemaually=findViewById(R.id.set_location_manually);


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());





        textView = findViewById(R.id.text_cur);


        savecurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(textView.getText().toString())) {
                    Snackbar.make(v, "Please move to your current location!", Snackbar.LENGTH_SHORT).show();
                } else {
                    saveData();

                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(movemap.this).inflate(R.layout.change_houseland, viewGroup, false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(movemap.this);

                    builder.setView(dialogView);

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    Window window = alertDialog.getWindow();
                    window.setGravity(Gravity.BOTTOM);

                    window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                    alertDialog.show();
                    final TextInputEditText house = dialogView.findViewById(R.id.housenum);
                    final TextInputEditText land = dialogView.findViewById(R.id.landnum);

                    Button ok = dialogView.findViewById(R.id.changeHouseLand);


                    alertDialog.setCanceledOnTouchOutside(false);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(house.getText().toString())) {
                                Snackbar.make(v, " Please Enter your house number!", Snackbar.LENGTH_SHORT).show();

                            } else if (TextUtils.isEmpty(land.getText().toString())) {
                                Snackbar.make(v, " Please give your current landmark!", Snackbar.LENGTH_SHORT).show();

                            } else {
                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("housenum", house.getText().toString());
                                userMap.put("landmark", land.getText().toString());
                                databaseReference.updateChildren(userMap);
                                alertDialog.dismiss();

                                startActivity(new Intent(movemap.this, HomeActivity.class));
                                finish();

                            }

                        }
                    });
                }
            }
        });
        savemaually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    ViewGroup viewGroup = findViewById(android.R.id.content);
//                    View dialogView = LayoutInflater.from(movemap.this).inflate(R.layout.activity_places_autocomplete, viewGroup, false);
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(movemap.this, R.style.DialogTheme);
//
//                    builder.setView(dialogView);
//
//                    final AlertDialog alertDialog = builder.create();
//                    Window window = alertDialog.getWindow();
//                    //  window.setGravity(Gravity.TOP);
//
//                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_search;
//                    alertDialog.show();
//
//                    EditText seach;
//                    seach = dialogView.findViewById(R.id.search_location_bar);
//
//
//                    ImageView imageView = dialogView.findViewById(R.id.crossImage);
//
//                    imageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            alertDialog.dismiss();
//                        }
//                    });
//
//                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//
//                    Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
//
//
//                    seach.setFocusable(false);
//                    seach.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//
//                            List<Place.Field> feildlist = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
//
//                            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, feildlist).build(movemap.this);
//                            startActivityForResult(intent, 100);
//
//                        }
//                    });
//
//
//                }
                startActivity(new Intent(movemap.this, SearchActivity.class));
            }
            });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmap);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);




        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                            mMap.animateCamera(cameraUpdate);
                          //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
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



                Geocoder geocoder;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                latLng = mMap.getCameraPosition().target;



                currLat = latLng.latitude;
                currLong = latLng.longitude;




                try {


                    addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                  //  Toast.makeText(movemap.this, "" + currLat + currLong, Toast.LENGTH_SHORT).show();
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






    public void saveData() {

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("uaddress", textView.getText().toString());
        userMap.put("ulat", String.valueOf(currLat));
        userMap.put("ulong", String.valueOf(currLong));

        databaseReference.updateChildren(userMap);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // progressBar.setVisibility(View.VISIBLE);
            Place place = Autocomplete.getPlaceFromIntent(data);


            HashMap<String, Object> locmap = new HashMap<>();
            locmap.put("ulat", String.valueOf(place.getLatLng().latitude));
            locmap.put("ulong", String.valueOf(place.getLatLng().longitude));
            locmap.put("uaddress", String.valueOf(place.getAddress()));
            databaseReference.updateChildren(locmap);
            startActivity(new Intent(movemap.this, HomeActivity.class));
            finish();
            Toast.makeText(this, "Location Saved", Toast.LENGTH_SHORT).show();
            //  progressBar.setVisibility(View.GONE);
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            //  progressBar.setVisibility(View.GONE);
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();


        }
    }


    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}