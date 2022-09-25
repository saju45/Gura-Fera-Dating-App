package com.example.tindertest.Matches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tindertest.Main.Cards;
import com.example.tindertest.Main.MainActivity;
import com.example.tindertest.Main.PhotoAdapter;
import com.example.tindertest.R;
import com.example.tindertest.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Matched_Activity extends AppCompatActivity {

    ImageView backImg;
    TextView textView;
    RecyclerView recyclerView;
    ArrayList<Cards> list;
    UserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);



        backImg=findViewById(R.id.back);
        textView=findViewById(R.id.toolbartag);
        list=new ArrayList<>();


        textView.setText("Matches");
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Matched_Activity.this, MainActivity.class));
                finish();
            }
        });



        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new UserAdapter(this,list);
        recyclerView.setAdapter(adapter);


        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                        Cards cards=dataSnapshot.getValue(Cards.class);
                        list.add(cards);
                    }
                }adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

 /*   @Override
    protected void onStart(){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                        Cards cards=dataSnapshot.getValue(Cards.class);
                        list.add(cards);
                    }


                }adapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        super.onStart();


    }*/

}