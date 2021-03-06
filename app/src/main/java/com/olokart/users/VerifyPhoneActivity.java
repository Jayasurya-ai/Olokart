package com.olokart.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String verificationId;
    String editAddr = "!", lat = "0", longi = "0", phoneNumber;
    ProgressBar progressBar;
    EditText editText;

    Button buttonSignIn;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.spin_kitVerify);

        Sprite foldingCube = new DoubleBounce();
        progressBar.setIndeterminateDrawable(foldingCube);
        buttonSignIn = findViewById(R.id.buttonSignIn);


        editText = findViewById(R.id.editTextCode);


        phoneNumber = getIntent().getStringExtra("phoneNumber");
         username = getIntent().getStringExtra("uname");
        sendVerificationCode(phoneNumber);

        // save phone number
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("USER_PREF",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phoneNumber", phoneNumber);
        editor.apply();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                try {
                    verifyCode(code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            progressBar.setVisibility(View.VISIBLE);
                            DatabaseReference rootRef;
                            rootRef = FirebaseDatabase.getInstance().getReference("Users");
                            HashMap<String, Object> locMap = new HashMap<>();
                            locMap.put("uaddress", editAddr);
                            locMap.put("ulat", lat);
                            locMap.put("ulong", longi);
                            locMap.put("olat", "0");
                            locMap.put("olong", "0");
                            locMap.put("uphone", phoneNumber);
                            locMap.put("uname",username);
                            locMap.put("uemail","");
                            locMap.put("housenum","");
                            locMap.put("landmark","");
                            locMap.put("otherhouse","");
                            locMap.put("otherland","");
                            locMap.put("oaddress","");


                            // userLoc = FirebaseDatabase.getInstance().getReference("Users Location");
                            String uid = mAuth.getCurrentUser().getUid();
                            rootRef.child(uid).updateChildren(locMap);

                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("uuid", uid);

                            progressBar.setVisibility(View.GONE);
                            rootRef.child(uid).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    ringtone();
                                    alertContinue();

                                }
                            });

                        } else {
                           // Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

        progressBar.setVisibility(View.GONE);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Toast.makeText(VerifyPhoneActivity.this, "SMS Code sent", Toast.LENGTH_LONG).show();

            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Toast.makeText(VerifyPhoneActivity.this, "completed", Toast.LENGTH_LONG).show();

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, ""+e, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    };

    public void resendotp(View view) {

        startActivity(new Intent(VerifyPhoneActivity.this,OtpRegister.class));
        finish();
    }
    public void alertContinue() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(VerifyPhoneActivity.this).inflate(R.layout.phone_verified, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyPhoneActivity.this, R.style.DialogTheme);

        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();


        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
       // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
       // window.setGravity(Gravity.BOTTOM);
        alertDialog.show();

        Button verified = dialogView.findViewById(R.id.btnContinue);

        verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(VerifyPhoneActivity.this, movemap.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    VerifyPhoneActivity.this.finish();

            }
        });

    }
    public void ringtone(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}