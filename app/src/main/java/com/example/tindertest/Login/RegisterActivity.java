package com.example.tindertest.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tindertest.R;


public class RegisterActivity extends AppCompatActivity {


    EditText emailEt,nameEt,passEt;
    Button continueBtn;
    TextView loginText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        init();

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name=nameEt.getText().toString();
                String email=emailEt.getText().toString();
                String password=passEt.getText().toString();

                if (name.isEmpty()){
                    nameEt.setError("Please enter your name");
                    nameEt.requestFocus();

                }else if (email.isEmpty())
                {
                    emailEt.setError("Please enter you email");
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    emailEt.setError("Enter valid email");
                    emailEt.requestFocus();
                }else if (password.isEmpty()){

                    passEt.setError("Enter your password");
                    passEt.requestFocus();

                }else {

                  SharedPreferences sharedPreferences=RegisterActivity.this.getSharedPreferences("userDb", Context.MODE_PRIVATE);
                  SharedPreferences.Editor editor;
                  editor=sharedPreferences.edit();
                  editor.putString("name",name);
                  editor.putString("email",email);
                  editor.putString("password",password);
                  editor.apply();

                  startActivity(new Intent(RegisterActivity.this, RegisterGenderActivity.class));



                }

            }
        });

    }

    public void init()
    {

        emailEt=findViewById(R.id.input_email);
        nameEt=findViewById(R.id.input_username);
        passEt=findViewById(R.id.input_password);
        continueBtn=findViewById(R.id.btn_register);
        loginText=findViewById(R.id.loginText);

    }
}