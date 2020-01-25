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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class SubscriptionCardAdapter extends RecyclerView.Adapter<SubscriptionCardAdapter.MyViewHolder> {

    Context context;
    ArrayList<SubscriptionCard> medicines;

    public SubscriptionCardAdapter(Context context, ArrayList<SubscriptionCard> medicines) {
        this.context = context;
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public SubscriptionCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.subscribedmeds_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionCardAdapter.MyViewHolder holder, final int position) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Subscription")
                .child(UID).child("Medicines");

        holder.medicine_name_tv.setText(medicines.get(position).getMed_Name());
        holder.shop_name_tv.setText(medicines.get(position).getShop_Name());
        holder.medicine_total_price_tv.setText(medicines.get(position).getTotal_Price());
        holder.med_quantity_tv.setText("Quantity: "+medicines.get(position).getQuantity());

        holder.remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbref.child(medicines.get(position).getMed_Name()).setValue(null)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                            Toast.makeText(context, medicines.get(position).getMed_Name()+" removed", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView medicine_name_tv, shop_name_tv, medicine_total_price_tv, med_quantity_tv;
        Button remove_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            medicine_name_tv = (TextView)itemView.findViewById(R.id.subscription_cardview_med_name);
            shop_name_tv = (TextView)itemView.findViewById(R.id.subscription_cardview_shop_name);
            medicine_total_price_tv = (TextView)itemView.findViewById(R.id.subscription_cardview_price_text);
            remove_btn = (Button)itemView.findViewById(R.id.subscription_cardview_remove_icon);
            med_quantity_tv = (TextView)itemView.findViewById(R.id.subscription_cardview_med_quantity);
        }
    }
}
