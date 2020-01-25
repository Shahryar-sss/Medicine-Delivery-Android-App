package com.spark.medicinedelivery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeCardAdapter extends RecyclerView.Adapter<EmployeeCardAdapter.MyViewHolder> {

    Context context;
    ArrayList<EmployeeCard> employee;

    public EmployeeCardAdapter(Context context, ArrayList<EmployeeCard> employee) {
        this.context = context;
        this.employee = employee;
    }

    @NonNull
    @Override
    public EmployeeCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.employee_employeecard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeCardAdapter.MyViewHolder holder, int position) {

        holder.name_tv.setText(employee.get(position).getUsername());
        holder.email_tv.setText(employee.get(position).getEmail());
        holder.phone_tv.setText(employee.get(position).getPhone());
        holder.id_tv.setText("Emp ID : "+employee.get(position).getEmployee_ID());

        String image_uri = employee.get(position).getImage();
        if(!image_uri.equalsIgnoreCase("default"))
            Picasso.get().load(image_uri).placeholder(R.drawable.male_avatar).into(holder.profile_pic);

    }

    @Override
    public int getItemCount() {
        return employee.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile_pic;
        TextView name_tv, email_tv, phone_tv, id_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_pic = (CircleImageView)itemView.findViewById(R.id.employeecard_profile_image);
            name_tv = (TextView)itemView.findViewById(R.id.employeecard_employee_name);
            email_tv = (TextView)itemView.findViewById(R.id.employeecard_employee_email);
            phone_tv = (TextView)itemView.findViewById(R.id.employeecard_employee_phone);
            id_tv = (TextView)itemView.findViewById(R.id.employeecard_employee_id);
        }
    }
}
