package com.example.egifthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.egifthouse.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginAdmin extends AppCompatActivity {

    String parentDBName = "Admin";
    EditText Userid, Password;
    FirebaseAuth mAuth;
    DatabaseReference mUsersRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        mAuth = FirebaseAuth.getInstance();

        Userid = (EditText) findViewById(R.id.et_email_address_lo);
        Password = (EditText) findViewById(R.id.et_password_lo);
        loadingBar = new ProgressDialog(this);

    }

    public void LoginAsAdmin(View view)
    {
        parentDBName = "Admin";

        final String userid = Userid.getText().toString();
        final String password = Password.getText().toString();

        if (TextUtils.isEmpty(userid) && TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Field's are empty!", Toast.LENGTH_SHORT).show();
        }
        else
        {

            loadingBar.setMessage("please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            final DatabaseReference rootRef;
            rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.child(parentDBName).child(userid).exists())
                    {
                        Users usersData = dataSnapshot.child(parentDBName).child(userid).getValue(Users.class);
                        if (usersData.getUseridd().equals(userid))
                        {
                            if (usersData.getPasswordd().equals(password))
                            {
                                if (parentDBName.equals("Admin"))
                                {
                                    Toast.makeText(LoginAdmin.this, "Logged in as admin!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginAdmin.this,AdminMainActivity.class));
                                    finish();
                                    loadingBar.dismiss();
                                }
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(LoginAdmin.this, "No record found!", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                { }
            });
        }
    }
}
