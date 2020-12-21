package com.olokart.users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HelpActivity extends AppCompatActivity {

    TextView call, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        call = findViewById(R.id.callSupport);
        email = findViewById(R.id.emailSupprt);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEMail();
            }
        });
    }
    public  void Call()
    {
        // Find the EditText by its unique ID
        // EditText e = (EditText)findViewById(R.id.editText);

        // show() method display the toast with message
        // "clicked"
        // Toast.makeText(this, "clicked", Toast.LENGTH_LONG)
        //.show();

        // Use format with "tel:" and phoneNumber created is
        // stored in u."
        Uri u = Uri.parse("tel:" +"9346850529" );

        // Create the intent and set the data for the
        // intent as the phone number.
        Intent i = new Intent(Intent.ACTION_DIAL, u);

        try
        {
            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            startActivity(i);
        }
        catch (SecurityException s)
        {
            // show() method display the toast with
            // exception message.
            Toast.makeText(this, s.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void sendEMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "Order@olokart.in");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Name of the issue");
        intent.putExtra(Intent.EXTRA_TEXT, "Enter your queries here");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

}