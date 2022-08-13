package com.example.egifthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.egifthouse.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminNewOrdersActivity extends AppCompatActivity {

    RecyclerView orderProList;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference ordersRef, ordersRefNew;
    FirebaseAuth mAuth;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        orderProList = findViewById(R.id.order_list);

        mAuth = FirebaseAuth.getInstance();
        //currentUserID = mAuth.getCurrentUser().getUid();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersRefNew = FirebaseDatabase.getInstance().getReference().child("Orders").child("One");
        orderProList.setHasFixedSize(true);
        orderProList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef, AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, @SuppressLint("RecyclerView") final int i, @NonNull final AdminOrders adminOrders) {
                holder.userName.setText("Name:- " + adminOrders.getNamee());
                holder.userPhone.setText(adminOrders.getPhonee());
                holder.userTotalPrice.setText(adminOrders.getTotalAmountt());
                holder.userDateTime.setText("Ordered on :- " + adminOrders.getDatee() + "  " + adminOrders.getTimee());
                holder.userShipAddress.setText("Address :- " + adminOrders.getAddresss() + " , " + adminOrders.getCityy());

                holder.showAllProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid = getRef(i).getKey();
                        Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                        //intent.putExtra("uid",adminOrders.getUidd());
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "YES",
                                        "NO"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Is this product shipped?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    String uid = getRef(i).getKey();
                                    AddToDatabase(uid);
                                    RemoveOrderThroughId(uid);
                                } else {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new AdminOrdersViewHolder(view);
            }
        };
        orderProList.setAdapter(adapter);
        adapter.startListening();
    }

    private void AddToDatabase(String uid) {
        HashMap map = new HashMap();
        map.put("one","one");
        ordersRefNew.child(uid).updateChildren(map);
    }

    private void RemoveOrderThroughId(String uid) {
        ordersRef.child(uid).removeValue();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhone, userTotalPrice, userDateTime, userShipAddress;
        public Button showAllProduct;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_username_layout);
            userPhone = itemView.findViewById(R.id.order_phone_layout);
            userTotalPrice = itemView.findViewById(R.id.order_price_layout);
            userShipAddress = itemView.findViewById(R.id.order_address_city_layout);
            userDateTime = itemView.findViewById(R.id.order_date_time_layout);

            showAllProduct = itemView.findViewById(R.id.show_all_product);
        }
    }
}
