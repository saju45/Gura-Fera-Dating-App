package com.example.tindertest.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tindertest.Main.Cards;
import com.example.tindertest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    ImageView profile;
    TextView username;
    ImageButton send;
    EditText messageEt;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String userId;

    MessageAdapter adapter;
    ArrayList<MessageModel> list;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profile=findViewById(R.id.profile);
        username=findViewById(R.id.username);
        send=findViewById(R.id.send);
        messageEt=findViewById(R.id.message);

        recyclerView=findViewById(R.id.messageRv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("users");
        userId=firebaseAuth.getCurrentUser().getUid();


        Intent intent=getIntent();
        String matchId=intent.getStringExtra("matchId");


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message=messageEt.getText().toString();
                if (message.isEmpty()){

                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }else {
                    sendMessage(userId,matchId,message);
                }
                messageEt.setText("");
            }
        });
        databaseReference.child(matchId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()){

                    Cards cards=snapshot.getValue(Cards.class);
                    username.setText(cards.getName());
                //    Glide.with(getApplicationContext()).load(cards.getProfileImageUrl()).into(profile);

                    getMessage(userId,matchId,cards.getProfileImageUrl());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendMessage(String sender,String receiver,String message){

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> chatMap=new HashMap<>();
        chatMap.put("sender",sender);
        chatMap.put("receiver",receiver);
        chatMap.put("message",message);

        databaseReference.child("Chats").push().setValue(chatMap);

    }

    public void getMessage(String myId,String userId,String imageUrl){

        list=new ArrayList<>();

        DatabaseReference reference;
        reference=FirebaseDatabase.getInstance().getReference().child("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    MessageModel model=dataSnapshot.getValue(MessageModel.class);

                    if (model.getReceiver().equals(myId) && model.getSender().equals(userId) ||
                            model.getReceiver().equals(userId) && model.getSender().equals(myId))
                    {
                        list.add(model);

                    }

                    adapter=new MessageAdapter(MessageActivity.this,list,imageUrl);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}