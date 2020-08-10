package com.viba.olokartusers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    TextView userName, userNum;
    ImageView editProfile;
    DatabaseReference databaseReference;
    TextView about, feedback, desclaimer, help, terms, logout, share;
    ImageView editImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.userName);
        userNum = findViewById(R.id.user_mobile);
        editProfile = findViewById(R.id.editProfile);


        about = findViewById(R.id.cardAbout);
        feedback = findViewById(R.id.cardFeedback);
        desclaimer = findViewById(R.id.cardDesclaimer);
        help = findViewById(R.id.cardHelp);
        terms = findViewById(R.id.cardTerms);
        logout = findViewById(R.id.cardLogout);
        share = findViewById(R.id.cardShare);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.editprofilelayout, viewGroup, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();
                Window window = alertDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);

                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                alertDialog.show();
                Button update,cancel;
                update=dialogView.findViewById(R.id.update);
                cancel=dialogView.findViewById(R.id.cancel);
                final TextInputEditText editname,editemail;
                editname=dialogView.findViewById(R.id.editname);
                editemail=dialogView.findViewById(R.id.editemail);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(editemail.getText().toString())||TextUtils.isEmpty(editname.getText().toString())){
                            Toast.makeText(ProfileActivity.this, "Please provide all details..", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            DatabaseReference updateref;
                            updateref=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            HashMap<String,Object>updatemap=new HashMap<>();
                            updatemap.put("uname",editname.getText().toString());
                            updatemap.put("uemail",editemail.getText().toString());
                            updateref.updateChildren(updatemap);
                            Toast.makeText(ProfileActivity.this, "Data Updated Succesfully...", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        desclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DesclaimerActivity.class));
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TermsConditions.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                ProfileActivity.this.finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_profile);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_orders:
                        startActivity(new Intent(getApplicationContext(), OrdersAvtivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String usernamestr= dataSnapshot.child("uname").getValue().toString();
                String userNumberstr = dataSnapshot.child("uphone").getValue().toString();

                userName.setText(usernamestr);
                userNum.setText(userNumberstr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}