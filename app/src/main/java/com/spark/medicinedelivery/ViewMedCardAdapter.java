package com.spark.medicinedelivery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewMedCardAdapter extends RecyclerView.Adapter<ViewMedCardAdapter.MyViewHolder> {

    Context context;
    ArrayList<ViewMedCard> medlist;

    public ViewMedCardAdapter(Context context, ArrayList<ViewMedCard> medlist) {
        this.context = context;
        this.medlist = medlist;
    }

    @NonNull
    @Override
    public ViewMedCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.viewmedicine_medicinecard, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewMedCardAdapter.MyViewHolder holder, int position) {

        holder.med_name.setText(medlist.get(position).getName());
        holder.med_price.setText(medlist.get(position).getPrice());

        String med_type = medlist.get(position).getType();
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

    }

    @Override
    public int getItemCount() {
        return medlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView med_name, med_price;
        ImageView med_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            med_name = (TextView) itemView.findViewById(R.id.viewmeds_med_name_cardview);
            med_price = (TextView) itemView.findViewById(R.id.viewmeds_med_price_cardview);
            med_image = (ImageView) itemView.findViewById(R.id.viewmeds_med_image_cardview);
        }
    }
}
