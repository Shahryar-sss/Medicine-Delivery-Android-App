package com.spark.medicinedelivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeMainActivity extends AppCompatActivity {

    private Button logout_btn, viewbuyer_btn, viewseller_btn, viewemp_btn;
    private TextView name_tv, email_tv, buyer_stat_tv, seller_stat_tv, profit_tv;
    CircleImageView emp_pic;

    private DatabaseReference dbref;
    private FirebaseAuth mAuth;

    int no_of_buyers = 0, no_of_sellers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);

        logout_btn = (Button)findViewById(R.id.employee_mainscreen_logout_btn);
        viewbuyer_btn = (Button)findViewById(R.id.employee_mainscreen_viewbuyer_btn);
        viewseller_btn = (Button)findViewById(R.id.employee_mainscreen_viewseller_btn);
        viewemp_btn = (Button)findViewById(R.id.employee_mainscreen_viewemp_btn);
        name_tv = (TextView)findViewById(R.id.employee_mainscreen_empname);
        email_tv = (TextView)findViewById(R.id.employee_mainscreen_empemail);
        buyer_stat_tv = (TextView)findViewById(R.id.employee_mainscreen_customer_stat);
        seller_stat_tv = (TextView)findViewById(R.id.employee_mainscreen_seller_stat);
        profit_tv = (TextView)findViewById(R.id.employee_mainscreen_profit_stat);
        emp_pic = (CircleImageView)findViewById(R.id.employee_mainscreen_profile_pic);

        dbref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        String UID = mAuth.getCurrentUser().getUid();

        dbref.child("Employee").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name_tv.setText(dataSnapshot.child("username").getValue().toString());
                email_tv.setText(dataSnapshot.child("email").getValue().toString());

                String image = dataSnapshot.child("image").getValue().toString();

                if(!image.equalsIgnoreCase("default"))
                    Picasso.get().load(image).placeholder(R.drawable.employee_avatar).into(emp_pic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Date date1 = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        String current_date1 = simpleDateFormat1.format(date1);

        dbref.child("Employee").child(UID).child("last_login_date").setValue(current_date1);

        dbref.child("Buyer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                    no_of_buyers = no_of_buyers + 1;
                buyer_stat_tv.setText(String.valueOf(no_of_buyers) + " customers registered");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbref.child("Seller").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                    no_of_sellers = no_of_sellers + 1;
                seller_stat_tv.setText(String.valueOf(no_of_sellers) + " vendors registered");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        final String current_date = simpleDateFormat.format(date);
        final String current_month = current_date.substring(3,10);

        dbref.child("Transaction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(current_month))
                    profit_tv.setText("Rs. " + dataSnapshot.child(current_month).child("Amount").getValue().toString() + " profit this month");
                else
                    profit_tv.setText("No transactions this month");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent loginIntent = new Intent(EmployeeMainActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();
            }
        });

        viewbuyer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewBuyerIntent = new Intent(EmployeeMainActivity.this, ViewBuyersActivity.class);
                viewBuyerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewBuyerIntent);
                finish();
            }
        });

        viewseller_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewSellerIntent = new Intent(EmployeeMainActivity.this, ViewSellersActivity.class);
                viewSellerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewSellerIntent);
                finish();
            }
        });

        viewemp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewEmployeeIntent = new Intent(EmployeeMainActivity.this, ViewEmployeeActivity.class);
                viewEmployeeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewEmployeeIntent);
                finish();
            }
        });
    }
}
