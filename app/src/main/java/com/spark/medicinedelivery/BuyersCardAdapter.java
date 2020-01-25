package com.spark.medicinedelivery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BuyersCardAdapter extends RecyclerView.Adapter<BuyersCardAdapter.MyViewHolder> {

    ArrayList<BuyersCard> buyers;
    Context context;

    public BuyersCardAdapter(ArrayList<BuyersCard> buyers, Context context) {
        this.buyers = buyers;
        this.context = context;
    }

    @NonNull
    @Override
    public BuyersCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.employee_buyercard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BuyersCardAdapter.MyViewHolder holder, int position) {

        holder.name_tv.setText(buyers.get(position).getUsername());
        holder.email_tv.setText(buyers.get(position).getEmail());
        holder.phone_tv.setText((buyers.get(position).getPhone()));
        holder.address_tv.setText(buyers.get(position).getAddress());

        String image_uri = buyers.get(position).getImage();
        if(!image_uri.equalsIgnoreCase("default"))
            Picasso.get().load(image_uri).placeholder(R.drawable.male_avatar).into(holder.profile_pic);

    }

    @Override
    public int getItemCount() {
        return buyers.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile_pic;
        TextView name_tv, email_tv, phone_tv, address_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_tv = (TextView)itemView.findViewById(R.id.buyer_name);
            email_tv = (TextView)itemView.findViewById(R.id.buyer_email);
            phone_tv = (TextView)itemView.findViewById(R.id.buyer_phone);
            address_tv = (TextView)itemView.findViewById(R.id.buyer_address);
            profile_pic = (CircleImageView)itemView.findViewById(R.id.buyer_dp);
        }
    }
}
