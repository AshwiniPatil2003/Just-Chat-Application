package com.example.mychatapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mychatapplication.adapter.SearchUserRecyclerAdapter;
import com.example.mychatapplication.model.UserModel;
import com.example.mychatapplication.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class SearchUserActivity extends AppCompatActivity {

    ImageButton searchbtn ;
    RecyclerView recyclerView;
    EditText searchinput;

    SearchUserRecyclerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        searchbtn = findViewById(R.id.search_user_btn);
        recyclerView = findViewById(R.id.search_user_recyclerView);
        searchinput = findViewById(R.id.search_username_input);

        searchinput.requestFocus();



        searchbtn.setOnClickListener(view -> {
            String searchTerm = searchinput.getText().toString();
            if(searchTerm.isEmpty() || searchTerm.length()<3){
                searchinput.setError("Invalid Username");
                return;
            }
            setupSearchRecyclerView(searchTerm);
        });

    }

    void setupSearchRecyclerView(String searchterm){

        Query query = FirebaseUtils.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("username",searchterm);

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();

        adapter = new SearchUserRecyclerAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart(){
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }
}