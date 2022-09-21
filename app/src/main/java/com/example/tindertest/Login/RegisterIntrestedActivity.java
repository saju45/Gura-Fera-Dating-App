package com.example.tindertest.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tindertest.R;

public class RegisterIntrestedActivity extends AppCompatActivity {


    Button male,female,continueBtn;
    boolean maleB=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_intrested);

        init();
        female.setAlpha(.5f);
        female.setBackgroundColor(Color.GRAY);
        clickListener();


    }
    public void init()
    {
        male=findViewById(R.id.insmaleButton);
        female=findViewById(R.id.insfemaleSelectionButton);
        continueBtn=findViewById(R.id.preferenceContinueButton);
    }

  public void clickListener(){

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButtonSelected();
                SharedPreferences sharedPreferences=getSharedPreferences("userDb", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor=sharedPreferences.edit();
                editor.putString("interest","male");
                editor.apply();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButtonSelected();
                SharedPreferences sharedPreferences=getSharedPreferences("userDb", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;

                editor=sharedPreferences.edit();
                editor.putString("interest","female");
                editor.apply();

            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterIntrestedActivity.this, RegisterAgeActivity.class));
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