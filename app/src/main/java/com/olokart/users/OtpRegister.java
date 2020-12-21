package com.olokart.users;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class OtpRegister extends AppCompatActivity {
    EditText editTextCountryCode, editTextPhone;
    Button buttonContinue;
    TextInputEditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_register);

        statusCheck();
      //  requestLocationPermission();
       // checkLocationPermission();
        username=findViewById(R.id.usernameReg);


        editTextCountryCode = findViewById(R.id.editCode);
        editTextCountryCode.setText("+91");
        editTextPhone = findViewById(R.id.numer);
        buttonContinue = findViewById(R.id.continueReg);

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
                finish();

            }
        });


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

    public void loginBack(View view) {
        startActivity(new Intent(OtpRegister.this,MainActivity.class));
        finish();
    }
}