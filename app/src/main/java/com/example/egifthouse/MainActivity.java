package com.example.egifthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
{

    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;
    CircleImageView imageVHead;

    private TextView navProfileHeaderName, navProfileHeaderEmail;
    private Button mainPageButton;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,ClientRef;
    private String currentUserId;

    Button btnCloseApp;
    MyReciever myReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        currentUserId = mAuth.getCurrentUser().getUid();

        myReciever = new MyReciever();

        UserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        ClientRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        drawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.nav_header);

        navProfileHeaderName = (TextView) navView.findViewById(R.id.nav_user_full_name);
        navProfileHeaderEmail = (TextView) navView.findViewById(R.id.nav_user_email);

        imageVHead = (CircleImageView) navView.findViewById(R.id.nav_header_profile);

        ClientRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String email = dataSnapshot.child("Email").getValue().toString();
                    navProfileHeaderName.setText(name);
                    navProfileHeaderEmail.setText(email);

                    final String image = dataSnapshot.child("image").getValue().toString();
                    if (!image.equals("default"))
                    {
                        Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.default_avatar).into(imageVHead);
                        Picasso.with(MainActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(imageVHead, new Callback() {
                            @Override
                            public void onSuccess()
                            { }

                            @Override
                            public void onError()
                            {
                                Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.default_avatar).into(imageVHead);
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                UserMenuSelector(item);
                return false;
            }
        });

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //DRAWER MENU ACTIVITY
    private void UserMenuSelector(MenuItem item)
    {
        switch ( item.getItemId())
        {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new SettingFragment()).commit();
                break;
            case R.id.nav_cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new CartFragment()).commit();
                break;
            case R.id.nav_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new ContactusFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new AboutusFragment()).commit();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        else
        {
            CheckUserExistance();
        }
    }

    private void CheckUserExistance()
    {
        final String currentUserId = mAuth.getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild("Phone"))
                {
                    //IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                    myReciever = new MyReciever();
                    //registerReceiver(myReciever,intentFilter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


}
