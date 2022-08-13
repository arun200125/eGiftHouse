package com.example.egifthouse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment
{

    private TextView navProfileName, navProfileEmail, navProfilePhone, navProfileGender, navProfileCity;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,ClientRef;
    private String currentUserId;
    private Toolbar mToolbar;
    CircleImageView setProfileImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        mToolbar = (Toolbar) getActivity().findViewById(R.id.main_page_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");

        navProfileName = (TextView) view.findViewById(R.id.profileName);
        navProfileEmail = (TextView) view.findViewById(R.id.profileEmail);
        navProfilePhone = (TextView) view.findViewById(R.id.profilePhone);
        navProfileGender = (TextView) view.findViewById(R.id.profileGender);
        navProfileCity = (TextView) view.findViewById(R.id.profileCity);

        setProfileImg = view.findViewById(R.id.profileProfileImage);

        UserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String email = dataSnapshot.child("Email").getValue().toString();
                    String phone = dataSnapshot.child("Phone").getValue().toString();
                    String gender = dataSnapshot.child("Gender").getValue().toString();
                    String city = dataSnapshot.child("City").getValue().toString();
                    navProfileName.setText(name);
                    navProfileEmail.setText(email);
                    navProfilePhone.setText(phone);
                    navProfileGender.setText(gender);
                    navProfileCity.setText(city);

                    final String image = dataSnapshot.child("image").getValue().toString();
                    if (!image.equals("default"))
                    {
                        Picasso.with(getActivity()).load(image).placeholder(R.drawable.default_avatar).into(setProfileImg);
                        Picasso.with(getActivity()).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(setProfileImg, new Callback() {
                            @Override
                            public void onSuccess()
                            { }

                            @Override
                            public void onError()
                            {
                                Picasso.with(getActivity()).load(image).placeholder(R.drawable.default_avatar).into(setProfileImg);
                            }
                        });
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
        return view;
    }
}
