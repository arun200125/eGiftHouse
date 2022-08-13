package com.example.egifthouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyReciever extends BroadcastReceiver
{
    ConnectivityManager connectivityManager ;
    NetworkInfo networkInfo;
    AlertDialog.Builder builder;
    private AlertDialog alertdailog;
    FirebaseAuth mAuth;
    DatabaseReference UserRef;
    String userId;

    @Override
    public void onReceive(final Context context, Intent intent)
    {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);


        UserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild("Phone"))
                {
                    builder = new AlertDialog.Builder(context);

                    View view = LayoutInflater.from(context).inflate(R.layout.customalertlayout,null);
                    builder.setView(view);
                    builder.setCancelable(false);

                    builder.create();
                    alertdailog = builder.show();

                    view.findViewById(R.id.proceedBtnApp).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent setup = new Intent(context,SetupActivity.class);
                            context.startActivity(setup);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });


        /*if(connectivityManager != null)
        {
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected())
            {
                if(alertdailog != null)
                {
                    alertdailog.dismiss();
                }
            }
            else
            {
                builder = new AlertDialog.Builder(context);

                View view = LayoutInflater.from(context).inflate(R.layout.customalertlayout,null);
                builder.setView(view);
                builder.setCancelable(false);

                builder.create();
                alertdailog = builder.show();

                view.findViewById(R.id.btnCloseApp).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        System.exit(0);
                    }
                });
            }
        }*/
    }
}
