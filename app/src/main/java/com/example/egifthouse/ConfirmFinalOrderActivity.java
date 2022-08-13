package com.example.egifthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity
{

    EditText txtConfirmName, txtConfirmPhone, txtConfirmAddress, txtConfirmCity;
    Button btnConfirmFinalOrder;
    FirebaseAuth mAuth;
    DatabaseReference confirmOrderRef;
    String currentUserId, totalAmount = "";
    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        confirmOrderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(currentUserId);

        totalAmount = getIntent().getStringExtra("Total Price");

        txtConfirmName = findViewById(R.id.shipment_name);
        txtConfirmPhone = findViewById(R.id.shipment_phone);
        txtConfirmAddress = findViewById(R.id.shipment_address);
        txtConfirmCity = findViewById(R.id.shipment_city);

        btnConfirmFinalOrder = findViewById(R.id.confirm_order_btn);
        btnConfirmFinalOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckDetails();
            }
        });

    }

    private void CheckDetails() {
        final String name = txtConfirmName.getText().toString();
        final String phone = txtConfirmPhone.getText().toString();
        final String address = txtConfirmAddress.getText().toString();
        final String city = txtConfirmCity.getText().toString();
        if (TextUtils.isEmpty(txtConfirmName.getText().toString())
                && TextUtils.isEmpty(txtConfirmPhone.getText().toString())
                && TextUtils.isEmpty(txtConfirmAddress.getText().toString())
                && TextUtils.isEmpty(txtConfirmCity.getText().toString()))
        {
            Toast.makeText(this, "Field's are empty!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        HashMap<String,Object> orderMap = new HashMap<>();
        orderMap.put("totalAmount",totalAmount);
        orderMap.put("name",txtConfirmName.getText().toString());
        orderMap.put("phone",txtConfirmPhone.getText().toString());
        orderMap.put("address",txtConfirmAddress.getText().toString());
        orderMap.put("city",txtConfirmCity.getText().toString());
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("state","not shipped");
        confirmOrderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(currentUserId)
                            .removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(ConfirmFinalOrderActivity.this, "Order Placed!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeFragment.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                /*HomeFragment homeFragment = new HomeFragment();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.add(R.id.main_container,homeFragment);
                                transaction.commit();*/
                            }
                        }
                    });
                }
            }
        });
    }
}
