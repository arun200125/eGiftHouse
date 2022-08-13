package com.example.egifthouse;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderFragment extends Fragment
 implements PaymentResultListener {

    EditText txtConfirmName, txtConfirmEmail, txtConfirmPhone, txtConfirmAddress, txtConfirmCity , txtConfirmDate;
    Button btnConfirmFinalOrder, btnConfirmDate;
    FirebaseAuth mAuth;
    DatabaseReference confirmOrderRef;
    String currentUserId, totalAmount = "";

    CardView cod_card, pay_online_card;

    DatePickerDialog.OnDateSetListener mDateSetListener;

    public ConfirmFinalOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_confirm_final_order, container, false);

        Checkout.preload(getActivity());

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        confirmOrderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(currentUserId);

        cod_card = view.findViewById(R.id.cod_card);
        pay_online_card = view.findViewById(R.id.pay_online_card);

        totalAmount = getArguments().getString("Total Price");

        txtConfirmName = view.findViewById(R.id.shipment_name);
        txtConfirmPhone = view.findViewById(R.id.shipment_phone);
        txtConfirmEmail = view.findViewById(R.id.shipment_email);
        txtConfirmAddress = view.findViewById(R.id.shipment_address);
        txtConfirmCity = view.findViewById(R.id.shipment_city);
        txtConfirmDate = view.findViewById(R.id.shipment_dateEdt);

        btnConfirmDate = view.findViewById(R.id.shipment_dateBtn);
        btnConfirmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Animation_Dialog,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                month = month + 1;
                //Log.d(TAG,"onDateSet: " + month + "/" + dayOfMonth + "/" + year);
                final String proDate = month + "/" + dayOfMonth + "/" + year;
                txtConfirmDate.setText(proDate);
            }
        };

        btnConfirmFinalOrder = view.findViewById(R.id.confirm_order_btn);
        btnConfirmFinalOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckDetails();
            }
        });

        cod_card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckDetails();
            }
        });

        pay_online_card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String cusName = txtConfirmName.getText().toString();
                String cusPhone = txtConfirmPhone.getText().toString();
                String cusAddress = txtConfirmAddress.getText().toString();
                String cusCity = txtConfirmCity.getText().toString();

                if (TextUtils.isEmpty(cusName) && TextUtils.isEmpty(cusPhone) && TextUtils.isEmpty(cusAddress) && TextUtils.isEmpty(cusCity))
                {
                    Toast.makeText(getActivity(), "Field's are empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    initializeOnlineGateway(totalAmount);
                }
            }
        });

        /*if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.checkSelfPermission(getActivity(),new String[]{Manifest.permission.READ_SMS,Manifest.permission.READ_SMS},);
        }*/

        return view;
    }

    private void initializeOnlineGateway(String totalAmount) {
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_13VlyTkwkKTmjJ");
            checkout.setImage(R.drawable.ic_launcher_background);

            double finalamt = Float.parseFloat(totalAmount)*100;

            try {
                JSONObject options = new JSONObject();
                options.put("name",txtConfirmName.getText().toString());
                options.put("description","Buying toys");
                options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                options.put("theme.color","#3399cc");
                options.put("currency","INR");
                options.put("amount",finalamt);
                options.put("prefill.email",txtConfirmEmail.getText().toString());
                options.put("prefill.contact","+91"+txtConfirmPhone.getText().toString());

                checkout.open(getActivity(),options);
            } catch (Exception e){
                Log.e("TAG","Error in razor pay checkout",e);
            }
    }

    private void CheckDetails() {

        SelectPymtMethodFragment homeFragment = new SelectPymtMethodFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main_container,homeFragment);
        transaction.commit();

        String cusName = txtConfirmName.getText().toString();
        String cusPhone = txtConfirmPhone.getText().toString();
        String cusAddress = txtConfirmAddress.getText().toString();
        String cusCity = txtConfirmCity.getText().toString();

        if (TextUtils.isEmpty(cusName) && TextUtils.isEmpty(cusPhone) && TextUtils.isEmpty(cusAddress) && TextUtils.isEmpty(cusCity))
        {
            Toast.makeText(getActivity(), "Field's are empty!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //ConfirmOrder();
            final String saveCurrentTime, saveCurrentDate;
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calForDate.getTime());

            HashMap<String,Object> orderMap = new HashMap<>();
            orderMap.put("totalAmount",totalAmount);
            orderMap.put("name",cusName);
            orderMap.put("phone",cusPhone);
            orderMap.put("address",cusAddress);
            orderMap.put("city",cusCity);
            orderMap.put("date",saveCurrentDate);
            orderMap.put("time",saveCurrentTime);
            orderMap.put("uid",currentUserId);
            //orderMap.put("requiredOn",proDate);
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
                                            Toast.makeText(getActivity(), "Order Placed!", Toast.LENGTH_SHORT).show();
                                        /*Intent intent = new Intent(getActivity(), HomeFragment.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();*/
                                            HomeFragment homeFragment = new HomeFragment();
                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            transaction.add(R.id.main_container,homeFragment);
                                            transaction.commit();
                                            /*SelectPymtMethodFragment homeFragment = new SelectPymtMethodFragment();
                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
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

    private void ConfirmOrder()
    {
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
        orderMap.put("uid",currentUserId);
        //orderMap.put("requiredOn",proDate);
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
                                        Toast.makeText(getActivity(), "Order Placed!", Toast.LENGTH_SHORT).show();
                                        /*Intent intent = new Intent(getActivity(), HomeFragment.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();*/
                                        HomeFragment homeFragment = new HomeFragment();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.add(R.id.main_container,homeFragment);
                                        transaction.commit();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getActivity(), "Payment Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getActivity(), "Payment Failed", Toast.LENGTH_SHORT).show();
    }
}
