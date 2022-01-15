package com.example.otp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Verification extends AppCompatActivity {
    TextView resend;

    EditText otp_text;
    String otp;

    LinearLayout confirm_button;

    TextView confirm_txt_button;

    ProgressBar confirm_progressbar;

    TextInputLayout error_email_txt;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        resend = findViewById(R.id.resend);
        otp_text = findViewById(R.id.otp_text);
        confirm_button = findViewById(R.id.confirm_button);
        confirm_txt_button = findViewById(R.id.confirm_txt_button);
        confirm_progressbar = findViewById(R.id.confirm_progressbar);
        error_email_txt = findViewById(R.id.error_email_txt);


        Intent intent = getIntent();
        String mob = intent.getStringExtra("mobile");


        initiate_otp(mob);


        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                getting inputdata from textbox
                String otp_txt = otp_text.getText().toString();

                if (otp_txt.isEmpty()) {
                    error_email_txt.setError("Enter OTP");
                } else {

//                hiding progresses bar and  change textview ,disable button
                    confirm_progressbar.setVisibility(View.VISIBLE);
                    confirm_txt_button.setText("Please Wait");
                    confirm_button.setEnabled(false);


                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otp,otp_txt);

                    signInWithPhoneAuthCredential(credential);



                }
            }
        });


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Verification.this, mob + "OTP Sended Successfully", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void initiate_otp(String mobile) {


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+91 " + mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                          @Override
                                          public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                              super.onCodeSent(s, forceResendingToken);
                                              otp=s;
                                          }

                                          @Override
                                          public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                              signInWithPhoneAuthCredential(phoneAuthCredential);
                                          }

                                          @Override
                                          public void onVerificationFailed(@NonNull FirebaseException e) {
                                              Toast.makeText(getApplicationContext(), mobile+e.getMessage(), Toast.LENGTH_SHORT).show();
                                          }
                                      }
                        )          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Verification.this, Dash_board.class));
                            finish();
                            Toast.makeText(Verification.this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                            confirm_button.setEnabled(true);
                            confirm_progressbar.setVisibility(View.GONE);
                            confirm_txt_button.setText("Verify");
                        }
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        confirm_button.setEnabled(true);
        confirm_progressbar.setVisibility(View.GONE);
        confirm_txt_button.setText("Verify");
    }
}