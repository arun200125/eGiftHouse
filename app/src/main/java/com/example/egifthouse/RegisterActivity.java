package com.example.egifthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{

    EditText Fullname, Email, Password;
    FirebaseAuth mAuth;
    DatabaseReference mUsersRef;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        Fullname = (EditText) findViewById(R.id.et_full_name);
        Email = (EditText) findViewById(R.id.et_email_address);
        Password = (EditText) findViewById(R.id.et_password);

        loadingBar = new ProgressDialog(this);
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
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

    public void backRegis(View view) {
        startActivity(new Intent(RegisterActivity.this,SplashActivity.class));
        finish();
    }

    public void CreteUsersAccount(View view)
    {
        final String fullname = Fullname.getText().toString();
        final String email = Email.getText().toString();
        String password = Password.getText().toString();

        if(TextUtils.isEmpty(fullname) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Field's are empty!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setMessage("please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    final String userId = mAuth.getCurrentUser().getUid();
                    mUsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    HashMap<String,Object> userMap = new HashMap<String,Object>();
                    userMap.put("Name",fullname);
                    userMap.put("Email",email);
                    userMap.put("image","default");
                    mUsersRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
                                Intent mainActi = new Intent(RegisterActivity.this,MainActivity.class);
                                mainActi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainActi);
                                finish();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String msg = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error! "+ msg, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
            });
        }
    }
}
