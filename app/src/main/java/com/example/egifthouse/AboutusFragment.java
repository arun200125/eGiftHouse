package com.example.egifthouse;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AboutusFragment extends Fragment
{

    private Toolbar mToolbar;
    Button contactLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_aboutus, container, false);

        mToolbar = (Toolbar) getActivity().findViewById(R.id.main_page_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("About Us");

        contactLink = view.findViewById(R.id.contactusLink);
        contactLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*Intent contact = new Intent(getActivity(),ContactusFragment.class);
                startActivity(contact);*/
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new ContactusFragment()).commit();
            }
        });

        return view;
    }
}
