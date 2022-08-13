package com.example.egifthouse.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.egifthouse.Interfaces.ItemClickListener;
import com.example.egifthouse.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;

    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image_layout);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name_layout);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price_layout);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description_layout);
    }

    public void setItemClickedListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v)
    {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
