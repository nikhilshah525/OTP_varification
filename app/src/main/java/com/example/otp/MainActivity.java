package com.example.otp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText mobile_no;

    LinearLayout send_button;

    TextView send_txt_button;

    TextInputLayout error_mobile_txt;

    ProgressBar send_progressbar;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

            mobile_no = findViewById(R.id.mobile_no);
            error_mobile_txt = findViewById(R.id.error_mobile_txt);
            send_button = findViewById(R.id.send_button);
            send_txt_button = findViewById(R.id.send_txt_button);
            send_progressbar = findViewById(R.id.send_progressbar);


            send_button.setOnClickListener(view -> {


                try {

//                getting inputdata from textbox
                    String mobile = mobile_no.getText().toString().trim();

                    if (mobile.isEmpty()) {
                        error_mobile_txt.setError("Please Enter Mobile Number.");
                    } else {

//                      hiding progresses bar and  change textview ,disable button
                        send_progressbar.setVisibility(View.VISIBLE);
                        send_txt_button.setText("Please Wait");
                        send_button.setEnabled(false);

                        Intent intent = new Intent(MainActivity.this, Verification.class);
                        intent.putExtra("mobile", mobile);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });


        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        send_button.setEnabled(true);
        send_progressbar.setVisibility(View.GONE);
        send_txt_button.setText("Send");
    }
}