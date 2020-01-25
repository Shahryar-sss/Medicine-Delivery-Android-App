package com.spark.medicinedelivery;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopCardAdapter extends RecyclerView.Adapter<ShopCardAdapter.MyViewHolder> {

    Context context;
    ArrayList<ShopCard> shops;
    String medicine_name;

    public ShopCardAdapter(Context context, ArrayList<ShopCard> shops, String medicine_name) {
        this.context = context;
        this.shops = shops;
        this.medicine_name = medicine_name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.shop_cardview, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final ShopCardAdapter.MyViewHolder holder, final int position) {

        holder.shop_name_tv.setText(shops.get(position).getName());
        holder.expiry_date_tv.setText(shops.get(position).getExpiry());

        String image_uri = shops.get(position).getImage();
        Picasso.get().load(image_uri).placeholder(R.drawable.shop_avatar).into(holder.shop_image_civ);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chosen_shop = shops.get(position).getName();
                Intent addtocartintent = new Intent(v.getContext(), AddToCartActivity.class);
                addtocartintent.putExtra("chosen_shop", chosen_shop);
                addtocartintent.putExtra("chosen_medicine", medicine_name);
                context.startActivity(addtocartintent);
                ((Activity)context).finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shop_name_tv, expiry_date_tv;
        CircleImageView shop_image_civ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            shop_name_tv = (TextView)itemView.findViewById(R.id.shop_selection_shop_name);
            expiry_date_tv = (TextView)itemView.findViewById(R.id.shop_selection_med_expiry);
            shop_image_civ = (CircleImageView)itemView.findViewById(R.id.shop_selection_shop_image);
        }
    }
}
