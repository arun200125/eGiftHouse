package com.example.egifthouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity
{

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

    }

    public void  login(View view)
    {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
    public void  getStarted(View view)
    {
        startActivity(new Intent(this,RegisterActivity.class));
        finish();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser fUser = mAuth.getCurrentUser();
        if (fUser != null)
        {
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }
    }
}
