package com.example.egifthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

public class LoginActivity extends AppCompatActivity
{

    EditText Email, Password;
    FirebaseAuth mAuth;
    DatabaseReference mUsersRef;
    ProgressDialog loadingBar;
    String parentDBName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Email = (EditText) findViewById(R.id.et_email_address_lo);
        Password = (EditText) findViewById(R.id.et_password_lo);
        loadingBar = new ProgressDialog(this);

    }

    public void signup(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    public void backLogin(View view) {
        startActivity(new Intent(LoginActivity.this,SplashActivity.class));
        finish();
    }

    public void AllowUserToLogin(View view)
    {
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Field's are empty!", Toast.LENGTH_SHORT).show();
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setMessage("please wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity.this, "LOGGED IN!", Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(LoginActivity.this,MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(main);
                        finish();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    public void LoginAsAdmin(View view)
    {
        startActivity(new Intent(LoginActivity.this,LoginAdmin.class));
    }
}
