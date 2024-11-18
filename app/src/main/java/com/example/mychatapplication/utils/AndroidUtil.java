package com.example.mychatapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mychatapplication.model.UserModel;
import com.google.firebase.firestore.auth.User;

public class AndroidUtil {
    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username",model.getUsername());
        intent.putExtra("phone",model.getPhone());
        intent.putExtra("userID", model.getUserID());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserID(intent.getStringExtra("userID"));
        return userModel;
     }

}
