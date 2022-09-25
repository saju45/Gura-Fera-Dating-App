package com.example.tindertest.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tindertest.Main.Cards;
import com.example.tindertest.R;
import com.example.tindertest.TopNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ImageView profile;
    ConstraintLayout layout;
    private static final int ACTIVITY_NUM = 1;
    ArrayList<PostModel> postArrayList;
    PostAdapter postAdapter;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String userId;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        setupTopNavigationView();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,PostActivity.class));
            }
        });


    }
    public void  init(){

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        userId=firebaseUser.getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        postArrayList =new ArrayList<>();
        postAdapter =new PostAdapter(getApplicationContext(), postArrayList);
        recyclerView=findViewById(R.id.homeRv);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(postAdapter);


        layout=findViewById(R.id.postLayout);
        profile=findViewById(R.id.home_profile);

    }
    private void setupTopNavigationView() {
        Log.d("TAG", "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationView tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(HomeActivity.this, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    protected void onStart() {

        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    Cards cards=snapshot.getValue(Cards.class);

                    Glide.with(getApplicationContext()).load(cards.getProfileImageUrl()).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    postArrayList.clear();
                    for (DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        PostModel postModel=snapshot1.getValue(PostModel.class);

                        postArrayList.add(postModel);
                    }
                    postAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();
    }
}