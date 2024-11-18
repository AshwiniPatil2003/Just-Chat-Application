package com.example.mychatapplication.utils;

import android.content.Intent;

import com.example.mychatapplication.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtils {
    public static String currentUserID(){

        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        if(currentUserID()!=null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("Users").document(currentUserID());
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("Users");
    }
    public static DocumentReference getChatroomReference(String chatroomID){
        return FirebaseFirestore.getInstance().collection("Chatrooms").document(chatroomID);
    }
    public static String getChatroomID(String UserID1, String UserID2){
        if(UserID1.hashCode()<UserID2.hashCode()){
            return UserID1+"_"+UserID2;
        }else{
            return UserID2+"_"+UserID1;
        }
    }

    public static CollectionReference getChatroomMessageReference(String chatroomID){
        return getChatroomReference(chatroomID).collection("chats");
    }



}
