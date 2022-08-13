package com.example.egifthouse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.egifthouse.Model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class ProductDetailsFragment extends Fragment
{

    ImageView productImage;
    ElegantNumberButton numberBtn;
    TextView productName, productPrice, productDescription;
    String productId = "", currentUserId, state = "Normal", imageUrl;
    Button addToCartButton, addToBargainBtn;
    FirebaseAuth mAuth;
    DatabaseReference cartRefList;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        cartRefList = FirebaseDatabase.getInstance().getReference().child("Cart List");
        //productId = getActivity().getIntent().getStringExtra("pid");
        productId = getArguments().getString("pid");
        imageUrl = getArguments().getString("imageUrl");

        //addToCart = findViewById(R.id.add_product_cart_btn);
        numberBtn = view.findViewById(R.id.number_btn);
        productImage = view.findViewById(R.id.product_image_details);
        productName = view.findViewById(R.id.product_name_details);
        productPrice = view.findViewById(R.id.product_price_details);
        productDescription = view.findViewById(R.id.product_description_details);

        addToCartButton = view.findViewById(R.id.addToCartBtn);
        addToCartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (state.equals("Order Shipped") || state.equals("Order Placed"))
                {
                    Toast.makeText(getActivity(), "Please wait till your product get shipped!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }
            }
        });

        /*addToBargainBtn = view.findViewById(R.id.addToBargainBtn);*/

        getProductDetails(productId);

        return view;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        CheckOrderState();
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
        cartMap.put("image",imageUrl);
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("uid",currentUserId);
        cartMap.put("quantity",numberBtn.getNumber());
        cartMap.put("discount","");
        cartRefList.child("User View")
                .child(currentUserId)
                .child("Products")
                .child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            cartRefList.child("Admin View")
                                    .child(currentUserId)
                                    .child("Products")
                                    .child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(getActivity(), "Added to cart!", Toast.LENGTH_SHORT).show();
                                                /*Intent intent = new Intent(ProductDetailsActivity.this, HomeFragment.class);
                                                startActivity(intent);*/
                                                HomeFragment homeFragment = new HomeFragment();
                                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
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
                    Picasso.with(getActivity()).load(product.getImagee()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
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
                    String stateO = dataSnapshot.child("state").getValue().toString();

                    if (stateO.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if (stateO.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
