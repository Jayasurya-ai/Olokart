package com.olokart.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
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
    ImageView editProfile, back;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    TextView about, feedback, desclaimer, help, terms, logout, share;
    DatabaseReference updateref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.userName);
        userNum = findViewById(R.id.user_mobile);
        editProfile = findViewById(R.id.editProfile);

        back = findViewById(R.id.backProfile);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                finish();
            }
        });
        progressBar = findViewById(R.id.spin_kitProfile);

        Sprite foldingCube = new DoubleBounce();
        progressBar.setIndeterminateDrawable(foldingCube);

        about = findViewById(R.id.cardAbout);
        feedback = findViewById(R.id.cardFeedback);
        desclaimer = findViewById(R.id.cardDesclaimer);
        help = findViewById(R.id.cardHelp);
        terms = findViewById(R.id.cardTerms);
        logout = findViewById(R.id.cardLogout);
        share = findViewById(R.id.cardShare);

        updateref=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

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
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                alertDialog.show();


                Button update,cancel;
                update=dialogView.findViewById(R.id.update);
                cancel=dialogView.findViewById(R.id.cancel);
                final TextInputEditText editname,editemail,editphone;
                editname=dialogView.findViewById(R.id.editname);
                editemail=dialogView.findViewById(R.id.editemail);
                editphone=dialogView.findViewById(R.id.editphone);

                updateref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String userName = dataSnapshot.child("uname").getValue().toString();
                        String userEmail = dataSnapshot.child("uemail").getValue().toString();
                        String userPhone=dataSnapshot.child("uphone").getValue().toString();
                        editphone.setText(userPhone);
                        editname.setText(userName);
                        editemail.setText(userEmail);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(editemail.getText().toString())||TextUtils.isEmpty(editname.getText().toString()) ||TextUtils.isEmpty(editphone.getText().toString())){
                            Toast.makeText(ProfileActivity.this, "Please provide all details..", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressBar.setVisibility(View.VISIBLE);

                            HashMap<String,Object>updatemap=new HashMap<>();
                            updatemap.put("uname",editname.getText().toString());
                            updatemap.put("uemail",editemail.getText().toString());
                            updatemap.put("uphone",editphone.getText().toString());
                            updateref.updateChildren(updatemap);
                            progressBar.setVisibility(View.GONE);
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
                showRateDialog(ProfileActivity.this);


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
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finishAffinity();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Olokart");
                    String shareMessage= "\nOLOKART is one of the emerging online market platform provided for Customers and Vendors to deal with Various Products.\nDownload the app Olokart from below link.. \n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose any one"));
                } catch(Exception e) {
                    //e.toString();
                }

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_profile);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

//        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#47a626")));
//        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#47a626")));
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

                try {

                    progressBar.setVisibility(View.VISIBLE);
                    String usernamestr = dataSnapshot.child("uname").getValue().toString();
                    String userNumberstr = dataSnapshot.child("uphone").getValue().toString();

                    userName.setText(usernamestr);
                    userNum.setText(userNumberstr);

                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void showRateDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) context)
                .setTitle("Rate application")
                .setMessage("Please, rate the app at Playstore")
                .setPositiveButton("RATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (context != null) {
                            String link = "market://details?id=";
                            try {
                                // play market available
                                ((Context) context).getPackageManager()
                                        .getPackageInfo("com.olokart.users", 0);
                                // not available
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                                // should use browser
                                link = "https://play.google.com/store/apps/details?id=com.olokart.users";
                            }
                            // starts external action
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(link + ((Context) context).getPackageName())));
                        }
                    }
                })
                .setNegativeButton("CANCEL", null);
        builder.show();
    }
}