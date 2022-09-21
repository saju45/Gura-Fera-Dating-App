package com.example.tindertest.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;


import com.example.tindertest.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterAgeActivity extends AppCompatActivity {


    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
    private DatePicker ageSelectionPicker;
    private Button ageContinueButton;
    // age limit attribute
    private int ageLimit = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_age);

        init();
        clickListener();

    }

    public void init()
    {
        ageSelectionPicker = findViewById(R.id.ageSelectionPicker);
        ageContinueButton = findViewById(R.id.ageContinueButton);
    }

    public void clickListener(){

        ageContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int age = getAge(ageSelectionPicker.getYear(), ageSelectionPicker.getMonth(), ageSelectionPicker.getDayOfMonth());

                String ageStr=String.valueOf(age);
                SharedPreferences sharedPreferences = getSharedPreferences("userDb",MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor=sharedPreferences.edit();
                editor.putInt("age",age);
                editor.apply();

                startActivity(new Intent(RegisterAgeActivity.this, RegisterHobbyActivity.class));

            }
        });
    }

    private int getAge(int year, int month, int day) {
        Calendar dateOfBirth = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dateOfBirth.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }


}