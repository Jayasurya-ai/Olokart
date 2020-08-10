package com.viba.olokartusers;

//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.animation.IntEvaluator;
//import android.animation.ValueAnimator;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.drawable.GradientDrawable;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.animation.AccelerateDecelerateInterpolator;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.GroundOverlay;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//public class movemap extends AppCompatActivity implements OnMapReadyCallback {
//    SupportMapFragment supportMapFragment;
//    FusedLocationProviderClient fusedLocationProviderClient;
//    GoogleMap mMap;
//    ;
//    double currLat, currLong;
//    List<Address> addresses;
//    FusedLocationProviderClient mFusedLocationClient;
//    int PERMISSION_ID = 44;
//    LatLng loc;
//    Button saveCurrentloc, savelocationmanually;
//    TextView textcurrent;
//    String address;
//    DatabaseReference databaseReference;
//    Geocoder geocoder;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_movemap);
//
//
//        saveCurrentloc = findViewById(R.id.set_location);
//        savelocationmanually = findViewById(R.id.set_location_manually);
//        textcurrent = findViewById(R.id.text_current_location);
//        geocoder = new Geocoder(movemap.this, Locale.getDefault());
//        addresses = null;

//
//        saveCurrentloc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(textcurrent.getText().toString())) {
//                    Snackbar.make(v, "Please Wait while we are loading your Location!", Snackbar.LENGTH_SHORT).show();
//
//                } else {
//                    saveData();
//                    startActivity(new Intent(movemap.this, HomeActivity.class));
//                    finish();
//                }
//            }
//        });
//        savelocationmanually.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(movemap.this, SearchLocationActivity.class));
//            }
//
//
//        });
//        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        supportMapFragment.getMapAsync(this);
//
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//
//    }
//
//    @Override
//    public void onMapReady(final GoogleMap googleMap) {
//        mMap = googleMap;
//
//        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//            @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();
//            task.addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(final Location location) {
//                    if (location != null) {
//                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//                            @Override
//                            public void onMapReady(GoogleMap googleMap) {
//                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                                Toast.makeText(movemap.this, "" + latLng, Toast.LENGTH_SHORT).show();
//
//                                //  MarkerOptions options = new MarkerOptions().position(latLng).title("Current Location");
//                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
//                                // googleMap.addMarker(options);
//                            }
//                        });
//                    }
//                }
//            });
//
//        }
//
//        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
//            @Override
//            public void onCameraMove() {
//                mMap.clear();
//            }
//        });
//
//
//        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
//
//            @Override
//            public void onCameraMove() {
//
//                mMap.clear();
//
//
//            }
//        });
//
//
//
//
//        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//
//
//
//                LatLng latLng = mMap.getCameraPosition().target;
//
//
//
//                currLat = (double) latLng.latitude;
//                currLong = (double) latLng.longitude;
//                try {
//                    addresses = geocoder.getFromLocation(currLat, currLong, 1);
//                    address = addresses.get(0).getAddressLine(0);
//                    textcurrent.setText(address);
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                Toast.makeText(movemap.this, "" + currLat, Toast.LENGTH_SHORT).show();
//                Toast.makeText(movemap.this, "" + currLong, Toast.LENGTH_SHORT).show();
//
//
//                mMap.clear();
//                circleAnimation();
//
//
//            }
//        });
//    }
//

//    // [END_EXCLUDE]
//
//    private void circleAnimation() {
//        GradientDrawable d = new GradientDrawable();
//        d.setShape(GradientDrawable.OVAL);
//        d.setSize(500, 500);
//        d.setColor(0x55dddddd);
//        d.setStroke(5, Color.BLACK);
//
//        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth()
//                , d.getIntrinsicHeight()
//                , Bitmap.Config.ARGB_8888);
//
//        // Convert the drawable to bitmap
//        Canvas canvas = new Canvas(bitmap);
//        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        d.draw(canvas);
//
//        // Radius of the circle
//        final int radius = 20;
//
//        // Add the circle to the map
//        //   circle = mMap.addGroundOverlay(new GroundOverlayOptions()
//        //     .position(mMap.getCameraPosition().target, 2 * radius).image(BitmapDescriptorFactory.fromBitmap(bitmap)));
//
//        ValueAnimator valueAnimator = new ValueAnimator();
//        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
//        valueAnimator.setIntValues(0, radius);
//        valueAnimator.setDuration(2000);
//        valueAnimator.setEvaluator(new IntEvaluator());
//        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                //  float animatedFraction = valueAnimator.getAnimatedFraction();
//                //  circle.setDimensions(animatedFraction * radius * 2);
//            }
//        });
//
//        valueAnimator.start();
//    }
//
//
//
//}

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Places extends FragmentActivity implements OnMapReadyCallback {

    LatLng latLng;
    GoogleMap mMap;
    Double currLat, currLong;
    LocationManager locationManager;
    Geocoder geocoder;
    TextView textView;
    Button savecurrent,savemaually;
    FusedLocationProviderClient fusedLocationProviderClient;
    Address address;
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());





        textView = findViewById(R.id.text_current);


        savecurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(textView.getText().toString())) {
                    Snackbar.make(v, "Please Wait while we are loading your Location!", Snackbar.LENGTH_SHORT).show();

                } else {
                   // saveData();
                    startActivity(new Intent(Places.this, BagAvtivity.class).putExtra("location", textView.getText().toString()));
                    finish();
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
                            Toast.makeText(Places.this, "" + latLng, Toast.LENGTH_SHORT).show();

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

                Toast.makeText(Places.this, "" + currLat + currLong, Toast.LENGTH_SHORT).show();

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
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
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





//        mMap = googleMap;
//
////        if (ActivityCompat.checkSelfPermission(movemap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(movemap.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
////            return;
////        }
////        mMap.setMyLocationEnabled(true);
//
//
//
//
//
//        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
//            @Override
//            public void onCameraMove() {
//                mMap.clear();
//            }
//        });
//
//

//    }
//    private void circleAnimation() {
//        GradientDrawable d = new GradientDrawable();
//        d.setShape(GradientDrawable.OVAL);
//        d.setSize(500, 500);
//        d.setColor(0x55dddddd);
//        d.setStroke(5, Color.BLACK);
//
//        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth()
//                , d.getIntrinsicHeight()
//                , Bitmap.Config.ARGB_8888);
//
//        // Convert the drawable to bitmap
//        Canvas canvas = new Canvas(bitmap);
//        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        d.draw(canvas);
//
//        // Radius of the circle
//        final int radius = 20;
//
//        // Add the circle to the map
////        final GroundOverlay circle = mMap.addGroundOverlay(new GroundOverlayOptions()
////                .position(mMap.getCameraPosition().target, 2 * radius).image(BitmapDescriptorFactory.fromBitmap(bitmap)));
//
//        ValueAnimator valueAnimator = new ValueAnimator();
//        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
//        valueAnimator.setIntValues(0, radius);
//        valueAnimator.setDuration(2000);
//        valueAnimator.setEvaluator(new IntEvaluator());
//        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
////                float animatedFraction = valueAnimator.getAnimatedFraction();
////                circle.setDimensions(animatedFraction * radius * 2);
//            }
//        });
//
//        valueAnimator.start();
//    }

}