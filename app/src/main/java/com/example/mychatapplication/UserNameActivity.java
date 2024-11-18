package com.example.mychatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.mychatapplication.model.UserModel;
import com.example.mychatapplication.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserNameActivity extends AppCompatActivity {

    EditText usernameinput;
    Button letmein;
    ProgressBar progressBar;
    String phonenumber;

    UserModel userModel;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        usernameinput = findViewById(R.id.login_username);
        letmein = findViewById(R.id.login_letmein_btn);
        progressBar = findViewById(R.id.login_progressbar);

        phonenumber = getIntent().getExtras().getString("phone");
        getUsername();

        letmein.setOnClickListener(view -> {
            setUsername();
        });
    }



    void setUsername(){

        String username = usernameinput.getText().toString();
        if(username.isEmpty() || username.length() < 3){
            usernameinput.setError("Username length should be at least 3");
            return;
        }
        setInProgress(true);

        if(userModel!=null){
            userModel.setUsername(username);
        }else{
            userModel = new UserModel(phonenumber, username, Timestamp.now(),FirebaseUtils.currentUserID());
        }

        FirebaseUtils.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(UserNameActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    void getUsername(){
        setInProgress(true);
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                   userModel = task.getResult().toObject(UserModel.class);
                    if(userModel != null){
                        usernameinput.setText(userModel.getUsername());
                    }
                }

            }
        });

    }


    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            letmein.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            letmein.setVisibility(View.VISIBLE);
        }
    }
}