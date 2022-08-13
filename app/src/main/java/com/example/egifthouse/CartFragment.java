package com.example.egifthouse;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.egifthouse.Model.Cart;
import com.example.egifthouse.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartFragment extends Fragment
{
    private Toolbar mToolbar;
    private RecyclerView cartRecV;
    private Button proceedButton;
    private TextView totalCartProPrice, msg1, totalCartProName;
    private RecyclerView.LayoutManager layoutManager;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private int overAllTotalPrice = 0;
    LinearLayout nameLayout, priceLayout, stateLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        mToolbar = (Toolbar) getActivity().findViewById(R.id.main_page_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Shopping Cart");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        cartRecV = view.findViewById(R.id.cart_list);
        cartRecV.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        cartRecV.setLayoutManager(layoutManager);

        msg1 = view.findViewById(R.id.msg1);

        nameLayout = view.findViewById(R.id.nameLayout);
        priceLayout = view.findViewById(R.id.priceLayout);
        stateLayout = view.findViewById(R.id.stateLayout);

        totalCartProPrice = view.findViewById(R.id.total_cart_product_price);
        totalCartProName = view.findViewById(R.id.total_cart_product_name);

        proceedButton = view.findViewById(R.id.next_process_button);
        proceedButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*totalCartProPrice.setText("Total Price : " +String.valueOf(overAllTotalPrice) +" Rs.");*/
                /*Intent intent = new Intent(getActivity(),ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overAllTotalPrice));
                startActivity(intent);
                getActivity().finish();*/

                Toast.makeText(getActivity(), "TOTAL AMOUNT IS " + String.valueOf(overAllTotalPrice), Toast.LENGTH_LONG).show();
                ConfirmFinalOrderFragment confirmFinalOrderFragment = new ConfirmFinalOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Total Price",String.valueOf(overAllTotalPrice));
                confirmFinalOrderFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container,confirmFinalOrderFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        overAllTotalPrice = 0;

        CheckOrderState();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(currentUserId)
                        .child("Products"), Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart)
            {
                cartViewHolder.txtProductName.setText(cart.getPnamee());
                cartViewHolder.txtProductQuantity.setText(cart.getQuantityy());
                cartViewHolder.txtProductPrice.setText(cart.getPricee());
                Picasso.with(getActivity()).load(cart.getImagee()).into(cartViewHolder.imageProductImage);
                int oneTypeProductTPrice = Integer.parseInt(cart.getPricee()) * Integer.parseInt(cart.getQuantityy());
                overAllTotalPrice = overAllTotalPrice + oneTypeProductTPrice;
                totalCartProPrice.setText(String.valueOf(overAllTotalPrice));
                //overAllTotalPrice = oneTypeProductTPrice;

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Delete"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Choose Your Option:");

                        builder.setItems(options, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                if (i==0)
                                {
                                    /*Intent intent = new Intent(getActivity(), ProductDetailsFragment.class);
                                    intent.putExtra("pid",cart.getPidd());
                                    startActivity(intent);*/
                                    ProductDetailsFragment homeFragment = new ProductDetailsFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("pid",cart.getPidd());
                                    homeFragment.setArguments(bundle);
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.main_container,homeFragment);
                                    transaction.commit();
                                }
                                if (i==1)
                                {
                                    cartListRef.child("User View")
                                            .child(currentUserId)
                                            .child("Products")
                                            .child(cart.getPidd()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(getActivity(), "Product Removed!", Toast.LENGTH_SHORT).show();
                                                        /*Intent home = new Intent(getActivity(),HomeFragment.class);
                                                        startActivity(home);*/

                                                        HomeFragment homeFragment = new HomeFragment();
                                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                        transaction.replace(R.id.main_container,homeFragment);
                                                        transaction.commit();

                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_item_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        cartRecV.setAdapter(adapter);
        adapter.startListening();

    }

    private void CheckOrderState()
    {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(currentUserId);
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String state = dataSnapshot.child("state").getValue().toString();
                    String nameP = dataSnapshot.child("name").getValue().toString();

                    if (state.equals("shipped"))
                    {
                        priceLayout.setVisibility(View.INVISIBLE);
                        nameLayout.setVisibility(View.VISIBLE);
                        totalCartProName.setText(nameP);
                        cartRecV.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        proceedButton.setVisibility(View.GONE);
                    }
                    else if (state.equals("not shipped"))
                    {
                        priceLayout.setVisibility(View.INVISIBLE);
                        nameLayout.setVisibility(View.INVISIBLE);
                        stateLayout.setVisibility(View.INVISIBLE);
                        cartRecV.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        proceedButton.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
