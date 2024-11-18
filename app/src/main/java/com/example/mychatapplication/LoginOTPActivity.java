package com.example.mychatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.ViewSpline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychatapplication.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOTPActivity extends AppCompatActivity {

    Long timeoutseconds = 60L;
    String verificationcode;
    PhoneAuthProvider.ForceResendingToken ResendingToken;
    String phonenumber;
    EditText otpinput;
    Button nextbtn;
    ProgressBar progressBar;
    TextView resendOTPtextview;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otpactivity);


        otpinput = findViewById(R.id.login_otp);
        nextbtn = findViewById(R.id.next_btn);
        progressBar = findViewById(R.id.login_progressbar);
        resendOTPtextview= findViewById(R.id.resend_otp_text);

        //Getting phone number
        phonenumber = getIntent().getExtras().getString("phone");
        sendOTP(phonenumber, false);

        nextbtn.setOnClickListener(view ->  {
            String enteredOtp = otpinput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationcode, enteredOtp);
            signIn(credential);

        });

        resendOTPtextview.setOnClickListener((view -> {
            sendOTP(phonenumber,true);
        }));

    }

    void sendOTP(String phonenumber, boolean isResend){

        startResendTimer();

        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth).
                        setPhoneNumber(phonenumber).
                        setTimeout(timeoutseconds, TimeUnit.SECONDS).
                        setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtil.showToast(getApplicationContext(),"OTP verification is failed");
                                 setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationcode = s;
                                ResendingToken = forceResendingToken;
                                AndroidUtil.showToast(getApplicationContext(),"OTP sent successfully!");
                                setInProgress(false);
                            }
                        });

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }


    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            nextbtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            nextbtn.setVisibility(View.VISIBLE);
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential) {
        //login and go

        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful ()) {
                    Intent intent= new Intent( LoginOTPActivity.this, UserNameActivity.class);
                    intent.putExtra("phone",phonenumber);
                    startActivity (intent);
                }else{
                    AndroidUtil.showToast(getApplicationContext(), "OTP verification failed");
                }

            }
        });
    }

    void startResendTimer(){
        resendOTPtextview.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutseconds--;
                resendOTPtextview.setText("Resend OTP in"+timeoutseconds +"seconds");
                if(timeoutseconds<=0){
                    timeoutseconds = 60L;
                    timer.cancel();
                    runOnUiThread(() ->{
                        resendOTPtextview.setEnabled(true);
                    });
                }
            }
        },0,1000);

    }
}