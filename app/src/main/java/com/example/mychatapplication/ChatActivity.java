package com.example.mychatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mychatapplication.adapter.ChatRecyclerAdapter;
import com.example.mychatapplication.adapter.SearchUserRecyclerAdapter;
import com.example.mychatapplication.model.ChatMessageModel;
import com.example.mychatapplication.model.ChatroomModel;
import com.example.mychatapplication.model.UserModel;
import com.example.mychatapplication.utils.AndroidUtil;
import com.example.mychatapplication.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    UserModel otheruser;
    String chatroomID;
    ChatRecyclerAdapter adapter;

    ChatroomModel chatroomModel;
    EditText messageinput;
    ImageButton sendMessageButton;
    TextView otherUsername;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otheruser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomID = FirebaseUtils.getChatroomID(FirebaseUtils.currentUserID(), otheruser.getUserID());


        messageinput=findViewById(R.id.chat_message_input);
        sendMessageButton = findViewById(R.id.message_send_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);

        otherUsername.setText(otheruser.getUsername());


        sendMessageButton.setOnClickListener(view -> {
            String message = messageinput.getText().toString().trim();
            if(message.isEmpty())
                return;

            sendMessageToUser(message);

        });
        getOrCreateChatroomModel();
        setupChatRecyclerView();

    }


    void setupChatRecyclerView(){
        Query query = FirebaseUtils.getChatroomMessageReference(chatroomID)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    void sendMessageToUser(String message){


        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderID(FirebaseUtils.currentUserID());
        FirebaseUtils.getChatroomReference(chatroomID).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseUtils.currentUserID(),Timestamp.now());
        FirebaseUtils.getChatroomMessageReference(chatroomID).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            messageinput.setText("");
                        }
                    }
                });
    }


    void getOrCreateChatroomModel(){
            FirebaseUtils.getChatroomReference(chatroomID).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    chatroomModel = task.getResult().toObject(ChatroomModel.class);
                    if(chatroomModel==null){
                        //First time chatting
                        chatroomModel = new ChatroomModel(
                                chatroomID,
                                Arrays.asList(FirebaseUtils.currentUserID(),otheruser.getUserID()),
                                Timestamp.now(),
                                ""
                        );
                        FirebaseUtils.getChatroomReference(chatroomID).set(chatroomModel);
                    }
                }
            });
     }


}