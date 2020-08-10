package com.viba.olokartusers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.security.Provider;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class OtpRegister extends AppCompatActivity {
    EditText editTextCountryCode, editTextPhone;
    Button buttonContinue;
    TextInputEditText username;
    FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    Provider provider;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_register);
      //  requestLocationPermission();
       // checkLocationPermission();
        username=findViewById(R.id.userNameReg);


        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextCountryCode.setText("+91");
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.buttonContinue);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  getLastLocation();
                String code = editTextCountryCode.getText().toString().trim();
                String number = editTextPhone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10 || TextUtils.isEmpty(username.getText().toString())) {
                    Toast.makeText(OtpRegister.this, "Please Enter all details...", Toast.LENGTH_SHORT).show();
                    return;
                }

                String phoneNumber = code + number;

                Intent intent = new Intent(OtpRegister.this, VerifyPhoneActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("uname",username.getText().toString());
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
    }
}