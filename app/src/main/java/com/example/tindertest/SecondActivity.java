package com.example.tindertest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tindertest.Chat.ChatActivity;
import com.example.tindertest.Chat.MessageActivity;
import com.example.tindertest.Main.Cards;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SecondActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    String userId;
    DatabaseReference reference;
    ImageView imageView;
    boolean isOkay=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        imageView=findViewById(R.id.profile);
        firebaseAuth=FirebaseAuth.getInstance();
        userId=firebaseAuth.getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference();

        reference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    Cards cards=snapshot.getValue(Cards.class);
                    Glide.with(getApplicationContext()).load(cards.getProfileImageUrl()).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        reference.child("chat")
                .orderByChild("status")
                .equalTo(0).limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.getChildrenCount()>0){
                            isOkay=true;
                            //room available
                            for (DataSnapshot childSnap: snapshot.getChildren()){

                                reference.child("users")
                                        .child(childSnap.getKey())
                                        .child("incoming")
                                        .setValue(userId);

                                reference.child("users")
                                        .child(childSnap.getKey())
                                        .child("status")
                                        .setValue(1);

                                Intent intent=new Intent(getApplicationContext(), MessageActivity.class);
                                String incoming=childSnap.child("incoming").getValue(String.class);
                                String createdBy=childSnap.child("createdBy").getValue(String.class);
                                boolean isAvailable=childSnap.child("isAvailable").getValue(Boolean.class);
                                intent.putExtra("matchId",userId);
                                intent.putExtra("incoming",incoming);
                                intent.putExtra("createdBy",createdBy);
                                intent.putExtra("isAvailable",isAvailable);
                                startActivity(intent);



                            }
                            Log.e("TAG", "Room is Available: " );

                        }else {
                            //room not Available
                            HashMap<String,Object> room=new HashMap<>();
                            room.put("incoming",userId);
                            room.put("createdBy",userId);
                            room.put("isAvailable",true);
                            room.put("status",0);

                            reference.child("chat")
                                    .child(userId)
                                    .setValue(room).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            reference.child("chat")
                                                    .child(userId)
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                            if (snapshot.child("status").exists()){
                                                                if (snapshot.child("status").getValue(Integer.class)==1){

                                                                    if (isOkay)
                                                                        return;
                                                                    isOkay=true;

                                                                    Intent intent=new Intent(getApplicationContext(),MessageActivity.class);
                                                                    String incoming=snapshot.child("incoming").getValue(String.class);
                                                                    String createdBy=snapshot.child("createdBy").getValue(String.class);
                                                                    boolean isAvailable=snapshot.child("isAvailable").getValue(Boolean.class);
                                                                    intent.putExtra("matchId",userId);
                                                                    intent.putExtra("incoming",incoming);
                                                                    intent.putExtra("createdBy",createdBy);
                                                                    intent.putExtra("isAvailable",isAvailable);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
