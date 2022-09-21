package com.example.tindertest.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tindertest.Main.MainActivity;
import com.example.tindertest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText emailEt,passwordEt;
    TextView signUpText;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();

        emailEt=findViewById(R.id.login_email);
        passwordEt=findViewById(R.id.login_password);
        login=findViewById(R.id.btn_login);
        signUpText=findViewById(R.id.link_signup);


        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=emailEt.getText().toString();
                String password=passwordEt.getText().toString();

                if (email.isEmpty())
                {
                    emailEt.setError("Please enter your email");
                    emailEt.requestFocus();
                 }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    emailEt.setError("Please enter your valid email");
                    emailEt.requestFocus();

                }else if (password.isEmpty()){

                    passwordEt.setError("enter your password");
                    passwordEt.requestFocus();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(LoginActivity.this, "Login is Success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser;
        firebaseUser=firebaseAuth.getCurrentUser();

        if (firebaseUser!=null)
        {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

    }
}