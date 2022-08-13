package com.example.egifthouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends AppCompatActivity
{
    FirebaseAuth mAuth;
    ImageView Teddy, PhotoFrame, Watch, VideoGame, RCcar, CoffeeMug, Toys;
    Button checkOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Teddy = (ImageView) findViewById(R.id.teddy);
        PhotoFrame = (ImageView) findViewById(R.id.photoframe);
        Watch = (ImageView) findViewById(R.id.watches);
        VideoGame = (ImageView) findViewById(R.id.videogames);
        RCcar = (ImageView) findViewById(R.id.rccar);
        CoffeeMug = (ImageView) findViewById(R.id.coffeemug);
        Toys = (ImageView) findViewById(R.id.toys);

        checkOrderBtn = findViewById(R.id.checkOrders);
        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this,AdminNewOrdersActivity.class));
            }
        });

        Teddy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","TeddyBear");
                startActivity(intent);
            }
        });

        PhotoFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","PhotoFrame");
                startActivity(intent);
            }
        });

        Watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","Watch");
                startActivity(intent);
            }
        });

        VideoGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","VideoGame");
                startActivity(intent);
            }
        });

        RCcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","RadioControlCar");
                startActivity(intent);
            }
        });

        CoffeeMug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","CoffeeMug");
                startActivity(intent);
            }
        });

        Toys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","Toys");
                startActivity(intent);
            }
        });
    }
    public void LOGOUT(View view)
    {
        mAuth.signOut();
        startActivity(new Intent(AdminMainActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        System.exit(0);
        /*mAuth.signOut();
        Intent back = new Intent(this,SplashActivity.class);
        startActivity(back);*/
    }
}
