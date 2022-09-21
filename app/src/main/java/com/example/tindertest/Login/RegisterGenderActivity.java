package com.example.tindertest.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tindertest.R;

public class RegisterGenderActivity extends AppCompatActivity {

    Button male,female,continueBtn;
    boolean maleB=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gender);

        init();

        clickListener();


    }

    public void init(){

        male=findViewById(R.id.maleSelectionButton);
        female=findViewById(R.id.femaleSelectionButton);
        continueBtn=findViewById(R.id.genderContinueButton);

        female.setAlpha(.5f);
        female.setBackgroundColor(Color.GRAY);
    }

    public void clickListener()
    {

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButtonSelected();
                SharedPreferences sharedPreferences = getSharedPreferences("userDb",MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor=sharedPreferences.edit();
                editor.putString("gender","male");
                editor.apply();

            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    female.setBackgroundColor(Color.parseColor("#FF4081"));
              //  male.setBackgroundColor(Color.GRAY);

               femaleButtonSelected();
                SharedPreferences sharedPreferences = getSharedPreferences("userDb",MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor=sharedPreferences.edit();
                editor.putString("gender","female");
                editor.apply();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterGenderActivity.this,RegisterIntrestedActivity.class));
            }
        });

    }


    public void maleButtonSelected() {
        maleB = true;
        male.setBackgroundColor(Color.parseColor("#FF4081"));
        male.setAlpha(1.0f);
        female.setAlpha(.5f);
        female.setBackgroundColor(Color.GRAY);
    }

    public void femaleButtonSelected() {
        maleB = false;
        female.setBackgroundColor(Color.parseColor("#FF4081"));
        female.setAlpha(1.0f);
        male.setAlpha(.5f);
        male.setBackgroundColor(Color.GRAY);
    }
}