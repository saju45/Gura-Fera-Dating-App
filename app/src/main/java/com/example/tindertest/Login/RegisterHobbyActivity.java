package com.example.tindertest.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tindertest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterHobbyActivity extends AppCompatActivity {



    boolean selectedBtn=true;
    private Button hobbiesContinueButton;
    private Button sportsSelectionButton;
    private Button travelSelectionButton;
    private Button musicSelectionButton;
    private Button fishingSelectionButton;

    private FirebaseAuth firebaseAuth;
    private   DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_hobby);


        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");


        init();
        clickListener();

    }
    public void init(){


        sportsSelectionButton = findViewById(R.id.sportsSelectionButton);
        travelSelectionButton = findViewById(R.id.travelSelectionButton);
        musicSelectionButton = findViewById(R.id.musicSelectionButton);
        fishingSelectionButton = findViewById(R.id.fishingSelectionButton);
        hobbiesContinueButton = findViewById(R.id.hobbiesContinueButton);


        travelSelectionButton.setAlpha(.5f);
        travelSelectionButton.setBackgroundColor(Color.GRAY);

        musicSelectionButton.setAlpha(.5f);
        musicSelectionButton.setBackgroundColor(Color.GRAY);

        fishingSelectionButton.setAlpha(.5f);
        fishingSelectionButton.setBackgroundColor(Color.GRAY);





    }
    public void clickListener(){

        sportsSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spotsButtonSelected();
                SharedPreferences sharedPreferences=getSharedPreferences("userDb", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor=sharedPreferences.edit();
                editor.putString("hobby","Sports");
                editor.apply();
            }
        });

        travelSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                travelButtonSelected();

                SharedPreferences sharedPreferences=getSharedPreferences("userDb", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor=sharedPreferences.edit();
                editor.putString("hobby","travel");
                editor.apply();
            }
        });

        musicSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicButtonSelected();
                SharedPreferences sharedPreferences=getSharedPreferences("userDb", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor=sharedPreferences.edit();
                editor.putString("hobby","Music");
                editor.apply();
            }
        });

        fishingSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fishingButtonSelected();

                SharedPreferences sharedPreferences=getSharedPreferences("userDb", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor=sharedPreferences.edit();
                editor.putString("hobby","Fishing");
                editor.apply();
            }
        });


        hobbiesContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("userDb",MODE_PRIVATE);
                String name=sharedPreferences.getString("name", "name");
                String email=sharedPreferences.getString("email","email");
                String password=sharedPreferences.getString("password","pass");
                String gender=sharedPreferences.getString("gender","gender");
                String interest=sharedPreferences.getString("interest","intrest");
                String hobby=sharedPreferences.getString("hobby","hobby");
                 int age=sharedPreferences.getInt("age",0);


                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {

                            String uid=task.getResult().getUser().getUid();

                            HashMap<String,Object> hashMap=new HashMap();
                            hashMap.put("name",name);
                            hashMap.put("email",email);
                            hashMap.put("password",password);
                            hashMap.put("gender",gender);
                            hashMap.put("interest",interest);
                            hashMap.put("age",age);
                            hashMap.put("bio","");
                            hashMap.put("userId",uid);
                            hashMap.put("hobby",hobby);
                            hashMap.put("distance",0);
                            hashMap.put("profileImageUrl","default");

                            databaseReference.child(uid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterHobbyActivity.this, "Register success ", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterHobbyActivity.this, LoginActivity.class));
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(RegisterHobbyActivity.this, "Fail : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "Fail cause "+task.getException().getMessage());
                        }
                    }
                });

            }
        });

    }


    public void spotsButtonSelected() {
        selectedBtn = true;
        sportsSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        sportsSelectionButton.setAlpha(1.0f);

        travelSelectionButton.setAlpha(.5f);
        travelSelectionButton.setBackgroundColor(Color.GRAY);

        musicSelectionButton.setAlpha(.5f);
        musicSelectionButton.setBackgroundColor(Color.GRAY);

        fishingSelectionButton.setAlpha(.5f);
        fishingSelectionButton.setBackgroundColor(Color.GRAY);
    }
    public void travelButtonSelected() {
        selectedBtn = false;
        travelSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        travelSelectionButton.setAlpha(1.0f);

        sportsSelectionButton.setAlpha(.5f);
        sportsSelectionButton.setBackgroundColor(Color.GRAY);

        musicSelectionButton.setAlpha(.5f);
        musicSelectionButton.setBackgroundColor(Color.GRAY);

        fishingSelectionButton.setAlpha(.5f);
        fishingSelectionButton.setBackgroundColor(Color.GRAY);
    }

    public void sportsButtonSelected() {
        selectedBtn = false;
        sportsSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        sportsSelectionButton.setAlpha(1.0f);

        travelSelectionButton.setAlpha(.5f);
        travelSelectionButton.setBackgroundColor(Color.GRAY);

        musicSelectionButton.setAlpha(.5f);
        musicSelectionButton.setBackgroundColor(Color.GRAY);

        fishingSelectionButton.setAlpha(.5f);
        fishingSelectionButton.setBackgroundColor(Color.GRAY);
    }

    public void musicButtonSelected() {
        selectedBtn = false;
        musicSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        musicSelectionButton.setAlpha(1.0f);

        travelSelectionButton.setAlpha(.5f);
        travelSelectionButton.setBackgroundColor(Color.GRAY);

        sportsSelectionButton.setAlpha(.5f);
        sportsSelectionButton.setBackgroundColor(Color.GRAY);

        fishingSelectionButton.setAlpha(.5f);
        fishingSelectionButton.setBackgroundColor(Color.GRAY);
    }


    public void fishingButtonSelected() {
        selectedBtn = false;
        fishingSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        fishingSelectionButton.setAlpha(1.0f);

        travelSelectionButton.setAlpha(.5f);
        travelSelectionButton.setBackgroundColor(Color.GRAY);

        musicSelectionButton.setAlpha(.5f);
        musicSelectionButton.setBackgroundColor(Color.GRAY);

        sportsSelectionButton.setAlpha(.5f);
        sportsSelectionButton.setBackgroundColor(Color.GRAY);
    }
}