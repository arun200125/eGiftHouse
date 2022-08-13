package com.example.egifthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.egifthouse.Model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity
{
    ImageView productImage;
    ElegantNumberButton numberBtn;
    TextView productName, productPrice, productDescription;
    String productId = "", currentUserId;
    Button addToCartButton;
    FirebaseAuth mAuth;
    DatabaseReference cartRefList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        cartRefList = FirebaseDatabase.getInstance().getReference().child("Cart List");
        productId = getIntent().getStringExtra("pid");

        //addToCart = findViewById(R.id.add_product_cart_btn);
        numberBtn = findViewById(R.id.number_btn);
        productImage = findViewById(R.id.product_image_details);
        productName = findViewById(R.id.product_name_details);
        productPrice = findViewById(R.id.product_price_details);
        productDescription = findViewById(R.id.product_description_details);

        addToCartButton = findViewById(R.id.addToCartBtn);
        addToCartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addingToCartList();
            }
        });

        getProductDetails(productId);

    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productId);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberBtn.getNumber());
        cartMap.put("discount","");
        cartRefList.child("User View").child(currentUserId).child("Products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    cartRefList.child("Admin View").child(currentUserId).child("Products").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(ProductDetailsActivity.this, "Added to cart!", Toast.LENGTH_SHORT).show();
                                /*Intent intent = new Intent(ProductDetailsActivity.this, HomeFragment.class);
                                startActivity(intent);*/
                                HomeFragment homeFragment = new HomeFragment();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.main_container,homeFragment);
                                transaction.commit();
                            }
                        }
                    });
                }
            }
        });
    }

    private void getProductDetails(String productId)
    {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Product product = dataSnapshot.getValue(Product.class);
                    productName.setText(product.getPnamee());
                    productDescription.setText(product.getDescriptionn());
                    productPrice.setText(product.getPricee());
                    Picasso.with(getApplicationContext()).load(product.getImagee()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
    }
}
