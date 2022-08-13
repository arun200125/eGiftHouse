package com.example.egifthouse.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.egifthouse.Interfaces.ItemClickListener;
import com.example.egifthouse.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemClickListener itemClickListener;
    public ImageView imageProductImage;

    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_name_layout);
        txtProductPrice = itemView.findViewById(R.id.cart_price_layout);
        txtProductQuantity = itemView.findViewById(R.id.cart_quantity_layout);

        imageProductImage = itemView.findViewById(R.id.cart_image_layout);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
