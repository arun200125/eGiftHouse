package com.example.egifthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.egifthouse.Model.Cart;
import com.example.egifthouse.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProductsActivity extends AppCompatActivity
{
    RecyclerView productListUser;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference userProductRef;
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        productListUser = findViewById(R.id.product_list);
        productListUser.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productListUser.setLayoutManager(layoutManager);

        userId = getIntent().getStringExtra("uid");

        userProductRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(userId)
                .child("Products");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(userProductRef,Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                cartViewHolder.txtProductName.setText(cart.getPnamee());
                cartViewHolder.txtProductQuantity.setText(cart.getQuantityy());
                cartViewHolder.txtProductPrice.setText(cart.getPricee());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_item_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        productListUser.setAdapter(adapter);
        adapter.startListening();
    }
}
