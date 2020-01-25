package com.spark.medicinedelivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MedicineCardAdapter extends RecyclerView.Adapter<MedicineCardAdapter.MyViewHolder> {

    Context context;
    ArrayList<MedicineCard> medicines;

    public MedicineCardAdapter(Context context, ArrayList<MedicineCard> medicines) {
        this.context = context;
        this.medicines = medicines;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.medicine_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineCardAdapter.MyViewHolder holder, final int position) {
        holder.med_name.setText(medicines.get(position).getName());
        holder.med_price.setText(medicines.get(position).getPrice());

        String med_type = medicines.get(position).getType();
        if (med_type.equalsIgnoreCase("Tablet"))
        {
            int resourceID = context.getResources().getIdentifier("pills_icon_cardview", "drawable", "com.spark.medicinedelivery");
            holder.med_image.setImageDrawable(ContextCompat.getDrawable(context, resourceID));
        }
        else if (med_type.equalsIgnoreCase("Syrup"))
        {
            int resourceID = context.getResources().getIdentifier("bottle_icon_cardview", "drawable", "com.spark.medicinedelivery");
            holder.med_image.setImageDrawable(ContextCompat.getDrawable(context, resourceID));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String med = medicines.get(position).getName();
                Intent shopSelectionIntent = new Intent(context, ShopSelectionActivity.class);
                shopSelectionIntent.putExtra("selected_medicine", med);
                context.startActivity(shopSelectionIntent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView med_name, med_price;
        ImageView med_image;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            med_name = (TextView) itemView.findViewById(R.id.med_name_cardview);
            med_price = (TextView) itemView.findViewById(R.id.med_price_cardview);
            med_image = (ImageView) itemView.findViewById(R.id.med_image_cardview);
            cardView = (CardView)itemView.findViewById(R.id.card_view);


        }



    }
}
