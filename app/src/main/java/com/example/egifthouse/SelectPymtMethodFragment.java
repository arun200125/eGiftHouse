package com.example.egifthouse;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectPymtMethodFragment extends Fragment {

    public SelectPymtMethodFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_select_pymt_method, container, false);



        return view;
    }
}