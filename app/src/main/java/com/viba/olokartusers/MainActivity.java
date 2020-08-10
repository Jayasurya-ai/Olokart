package com.viba.olokartusers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.app.BottomSheetDialog;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.annotations.Nullable;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout linearLayout;
    TextView[] dots;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 550;
    final long PERIOD_MS = 2200;
    int NUM_PAGES = 4;
    Button signInButton;
    DatabaseReference databaseReference;
    GoogleSignInClient mgoogleSignInClient;
    private static final String TAg = "MainActivity";
    private FirebaseAuth mAuth;
    int RC_SIGn_In = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton = findViewById(R.id.register_google);
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == NUM_PAGES - 1) {
//                    currentPage = 0;
//                }
//                viewPager.setCurrentItem(currentPage++, true);
//            }
//        };
//
//        timer = new
//
//                Timer(); // This will create a new Thread
//        timer.schedule(new
//
//                               TimerTask() { // task to be scheduled
//                                   @Override
//                                   public void run() {
//                                       handler.post(Update);
//                                   }
//                               }, DELAY_MS, PERIOD_MS);
//        viewPager = findViewById(R.id.viewPager);
//        final SliderAdapter sliderAdapter = new SliderAdapter(this);
//        linearLayout = findViewById(R.id.lin);
//        viewPager.setAdapter(sliderAdapter);
//        addDotsIndicator(0);

//        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    private void signIn() {
        Intent SignInIntent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(SignInIntent, RC_SIGn_In);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGn_In) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                FireBaseGoogleOuth(acc);

            } catch (ApiException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
//            FireBaseGoogleOuth(null);
            }
        }
    }

    private void FireBaseGoogleOuth(GoogleSignInAccount acc) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail().build();

                    mgoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

                    GoogleSignInAccount account1 = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
                    if (account1 != null) {
                        Intent i = new Intent(MainActivity.this, OtpRegister.class);
                        HashMap<String, Object> jayasurya = new HashMap<>();
                        jayasurya.put("uname", account1.getDisplayName());
                        jayasurya.put("uemail", account1.getEmail());
                        jayasurya.put("upass", "");
                        jayasurya.put("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        jayasurya.put("uaddress", "");
                        jayasurya.put("ulat", "0");
                        jayasurya.put("ulong", "0");
                        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(jayasurya);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public void addDotsIndicator(int position) {
//
//        dots = new TextView[4];
//        linearLayout.removeAllViews();
//
//        for (int i = 0; i < dots.length; i++) {
//
//            dots[i] = new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226"));
//            dots[i].setTextSize(35);
//            dots[i].setTextColor(getResources().getColor(R.color.colorAccent));
//
//            linearLayout.addView(dots[i]);
//
//        }
//        if (dots.length > 0) {
//            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
//        }
//    }
//
//
//
//    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int i) {
//
//            addDotsIndicator(i);
//
//            currentPage = i;
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    };

    public void EmailPassword(View view) {

        startActivity(new Intent(MainActivity.this, OtpRegister.class));
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

