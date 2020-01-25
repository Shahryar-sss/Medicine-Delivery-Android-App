package com.spark.medicinedelivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerCardAdapter extends RecyclerView.Adapter<SellerCardAdapter.MyViewHolder> {

    ArrayList<SellerCard> sellers;
    Context context;

    public SellerCardAdapter(ArrayList<SellerCard> sellers, Context context) {
        this.sellers = sellers;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.employee_sellercard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.name_tv.setText(sellers.get(position).getShop_Name());
        holder.email_tv.setText(sellers.get(position).getEmail());
        holder.phone_tv.setText(sellers.get(position).getShop_Phone());
        holder.address_tv.setText(sellers.get(position).getShop_Address());

        String image_uri = sellers.get(position).getImage();
        if(!image_uri.equalsIgnoreCase("default"))
            Picasso.get().load(image_uri).placeholder(R.drawable.shop_avatar).into(holder.shop_pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shopReportIntent = new Intent(context, ShopReportActivity.class);
                shopReportIntent.putExtra("Shop_Name", sellers.get(position).getShop_Name());
                context.startActivity(shopReportIntent);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return sellers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView shop_pic;
        TextView name_tv, email_tv, phone_tv, address_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_tv = (TextView)itemView.findViewById(R.id.seller_shop_name);
            email_tv = (TextView)itemView.findViewById(R.id.seller_shop_email);
            phone_tv = (TextView)itemView.findViewById(R.id.seller_shop_phone);
            address_tv = (TextView)itemView.findViewById(R.id.seller_shop_address);
            shop_pic = (CircleImageView)itemView.findViewById(R.id.seller_shop_image);
        }
    }
}
