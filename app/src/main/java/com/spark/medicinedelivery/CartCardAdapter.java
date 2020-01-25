package com.spark.medicinedelivery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartCardAdapter extends RecyclerView.Adapter<CartCardAdapter.MyViewHolder> {

    Context context;
    ArrayList<CartCard> list;

    public CartCardAdapter(Context context, ArrayList<CartCard> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CartCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CartCardAdapter.MyViewHolder holder, final int position) {

        final DatabaseReference dbref;
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();

        dbref = FirebaseDatabase.getInstance().getReference().child("Cart").child(UID);

        holder.medicine_name_tv.setText(list.get(position).getMed_Name());
        holder.shop_name_tv.setText(list.get(position).getShop_Name());
        holder.medicine_total_price_tv.setText(list.get(position).getTotal_Price());
        holder.med_quantity_tv.setText("Quantity: "+list.get(position).getQuantity());

        holder.remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String remove_med_name = list.get(position).getMed_Name();
                dbref.child(remove_med_name).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(context, remove_med_name+" removed", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, "Databse connection error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView medicine_name_tv, shop_name_tv, medicine_total_price_tv, med_quantity_tv;
        Button remove_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            medicine_name_tv = (TextView)itemView.findViewById(R.id.cart_cardview_med_name);
            shop_name_tv = (TextView)itemView.findViewById(R.id.cart_cardview_shop_name);
            medicine_total_price_tv = (TextView)itemView.findViewById(R.id.cart_cardview_price_text);
            remove_btn = (Button)itemView.findViewById(R.id.cart_cardview_remove_icon);
            med_quantity_tv = (TextView)itemView.findViewById(R.id.cart_cardview_med_quantity);
        }
    }
}


