package com.example.mychatapplication;

import static com.example.mychatapplication.R.id.send_otp_btn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class LoginPhoneNumberActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button sendOtpBtn;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);

        countryCodePicker = findViewById(R.id.login_countrycode);
        phoneInput = findViewById(R.id.login_phonenumber);
        sendOtpBtn = findViewById(send_otp_btn);
        progressBar = findViewById(R.id.login_progressbar);

        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);

        sendOtpBtn.setOnClickListener(view -> {

                if(!countryCodePicker.isValidFullNumber()){
                    phoneInput.setError("Enter valid phone numebr");
                    return;
                }
                Intent intent = new Intent(LoginPhoneNumberActivity.this, LoginOTPActivity.class);
                intent.putExtra("phone", countryCodePicker.getFullNumberWithPlus());
                startActivity(intent);

        });


    }

}
