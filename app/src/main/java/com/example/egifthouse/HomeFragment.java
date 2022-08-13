package com.example.egifthouse;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.egifthouse.Model.Product;
import com.example.egifthouse.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class HomeFragment extends Fragment
{

    private Toolbar mToolbar;

    FirebaseAuth mAuth;
    DatabaseReference UserRef, productRef;
    String userId;
    MyReciever myReciever;
    RecyclerView productsRec;
    RecyclerView.LayoutManager layoutManager;
    private AdView mAdView;

    public HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //getActivity().super.onCreateView(savedInstanceState);
        //getActivity().setContentView(R.layout.fragment_home);

        mToolbar = (Toolbar) getActivity().findViewById(R.id.main_page_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Products");

        myReciever = new MyReciever();

        productsRec = view.findViewById(R.id.productRec);
        productsRec.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        productsRec.setLayoutManager(layoutManager);

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            startActivity(new Intent(getActivity(),LoginActivity.class));
            getActivity().finish();
        }
        else
        {
            CheckUserExistance();
        }

        startListening();

    }

    private void startListening()
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("Products").limitToLast(50);
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>().setQuery(query,Product.class).build();
        FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, final int i, @NonNull final Product product)
            {
                productViewHolder.txtProductName.setText(product.getPnamee());
                productViewHolder.txtProductDescription.setText(product.getDescriptionn());
                productViewHolder.txtProductPrice.setText(product.getPricee());

                Picasso.with(getActivity()).load(product.getImagee()).into(productViewHolder.imageView);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent intent = new Intent(getActivity(),ProductDetailsActivity.class);
                        intent.putExtra("pid",product.getPidd());
                        startActivity(intent);*/
                        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("pid",product.getPidd());
                        bundle.putString("uid",userId);
                        bundle.putString("imageUrl",product.getImagee());
                        productDetailsFragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_container,productDetailsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        productsRec.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

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
                    IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                    myReciever = new MyReciever();
                    requireActivity().registerReceiver(myReciever,intentFilter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

}
