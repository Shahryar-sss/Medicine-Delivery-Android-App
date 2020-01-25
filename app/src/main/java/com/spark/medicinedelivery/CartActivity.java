package com.spark.medicinedelivery;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {

    private TextView total_price_text;
    private RecyclerView recyclerView;
    private Button checkout_btn, subscribe_btn, prev_page_btn;
    FirebaseAuth mAuth;
    DatabaseReference dbref, transactref, subscribe_ref, shop_transact_ref;
    ArrayList<CartCard> cartlist;
    DataSnapshot subscribe_snapshot;

    int total_price = 0;
    int discount;
    String period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        total_price_text = (TextView)findViewById(R.id.cart_screen_totalprice_text);
        recyclerView = (RecyclerView)findViewById(R.id.cart_screen_recyclerview);
        checkout_btn = (Button)findViewById(R.id.cart_screen_checkout_button);
        subscribe_btn = (Button)findViewById(R.id.cart_screen_subscribe_button);
        prev_page_btn = (Button)findViewById(R.id.cart_screen_back_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);



        mAuth = FirebaseAuth.getInstance();
        final String UID = mAuth.getCurrentUser().getUid();

        dbref = FirebaseDatabase.getInstance().getReference().child("Cart").child(UID);

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartlist = new ArrayList<CartCard>();
                subscribe_snapshot = dataSnapshot;
                total_price = 0;
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    CartCard cartCard = ds.getValue(CartCard.class);
                    cartlist.add(cartCard);
                    String price = ds.child("Total_Price").getValue().toString();
                    total_price = total_price + Integer.parseInt(price);

                }
                total_price_text.setText(String.valueOf(total_price));
                CartCardAdapter adapter = new CartCardAdapter(CartActivity.this, cartlist);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        transactref = FirebaseDatabase.getInstance().getReference().child("Transaction");


        prev_page_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buyerMainIntent = new Intent(CartActivity.this, BuyerMainActivity.class);
                buyerMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(buyerMainIntent);
                finish();
            }
        });

        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String current_date = simpleDateFormat.format(date);
                        String current_month = current_date.substring(3, 10);
                        if (dataSnapshot.hasChild(current_month))
                        {
                            int amt = Integer.parseInt(dataSnapshot.child(current_month).child("Amount").getValue().toString());
                            amt = amt+total_price;
                            transactref.child(current_month).child("Amount").setValue(String.valueOf(amt));

                        }
                        else
                            transactref.child(current_month).child("Amount").setValue(String.valueOf(total_price));

                        addToHistory(UID);
                        updateShopTransaction();
                        dbref.setValue(null);

                        Intent buyerMainIntent = new Intent(CartActivity.this, BuyerMainActivity.class);
                        buyerMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(buyerMainIntent);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        subscribe_ref = FirebaseDatabase.getInstance().getReference().child("Subscription").child(UID);


        subscribe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sub_duration[] = {"3 months", "6 months", "12 months"};
                final AlertDialog.Builder dialog = new AlertDialog.Builder(CartActivity.this);
                dialog.setTitle("Select a duration");
                dialog.setItems(sub_duration, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0)
                        {
                            discount = 10;
                            period = sub_duration[which];
                            Map sub_info = new HashMap();
                            sub_info.put("duration", period);
                            sub_info.put("discount", String.valueOf(discount)+"%");

                            Date date = Calendar.getInstance().getTime();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String current_date = simpleDateFormat.format(date);

                            sub_info.put("start_date", "Pending");

                            subscribe_ref.child("Info").setValue(sub_info);

                            //subscribe_ref.child("Info").setValue(sub_info);

                            subscribe_ref.child("Medicines").setValue(subscribe_snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        Toast.makeText(CartActivity.this, "Added to your subscription", Toast.LENGTH_LONG).show();
                                }
                            });
                            dbref.setValue(null);
                            Intent buyerMainIntent = new Intent(CartActivity.this, BuyerMainActivity.class);
                            buyerMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(buyerMainIntent);
                            finish();

                        }
                        else if (which==1)
                        {
                            discount = 20;
                            period = sub_duration[which];
                            Map sub_info = new HashMap();
                            sub_info.put("duration", period);
                            sub_info.put("discount", String.valueOf(discount)+"%");

                            Date date = Calendar.getInstance().getTime();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String current_date = simpleDateFormat.format(date);

                            sub_info.put("start_date", "Pending");

                            subscribe_ref.child("Info").setValue(sub_info);

                            subscribe_ref.child("Medicines").setValue(subscribe_snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        Toast.makeText(CartActivity.this, "Added to your subscription", Toast.LENGTH_LONG).show();
                                }
                            });
                            dbref.setValue(null);
                            Intent buyerMainIntent = new Intent(CartActivity.this, BuyerMainActivity.class);
                            buyerMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(buyerMainIntent);
                            finish();
                        }
                        else
                        {
                            discount = 30;
                            period = sub_duration[which];
                            Map sub_info = new HashMap();
                            sub_info.put("duration", period);
                            sub_info.put("discount", String.valueOf(discount)+"%");

                            Date date = Calendar.getInstance().getTime();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String current_date = simpleDateFormat.format(date);

                            sub_info.put("start_date", "Pending");

                            subscribe_ref.child("Info").setValue(sub_info);

                            subscribe_ref.child("Info").setValue(sub_info);

                            subscribe_ref.child("Medicines").setValue(subscribe_snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        Toast.makeText(CartActivity.this, "Added to your subscription", Toast.LENGTH_LONG).show();
                                }
                            });

                            dbref.setValue(null);
                            Intent buyerMainIntent = new Intent(CartActivity.this, BuyerMainActivity.class);
                            buyerMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(buyerMainIntent);
                            finish();
                        }
                    }
                });
                dialog.create().show();


            }
        });

    }

    private void updateShopTransaction() {

        shop_transact_ref = FirebaseDatabase.getInstance().getReference().child("Seller");

        shop_transact_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String current_shopname, current_transaction;

                for (int i=0; i<cartlist.size(); i++)
                {
                    current_shopname = cartlist.get(i).getShop_Name();
                    current_transaction = cartlist.get(i).getTotal_Price();

                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String current_date = simpleDateFormat.format(date);
                    String current_month = current_date.substring(3, 10);

                    Map transact_map = new HashMap();

                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        String s_name = ds.child("Shop_Name").getValue().toString();

                        if (s_name.equalsIgnoreCase(current_shopname))
                        {
                            String key = ds.getKey();

                            if (ds.child("Transaction").hasChild(current_month))
                            {
                                String s_val = ds.child("Transaction").child(current_month).getValue().toString();
                                current_transaction = String.valueOf(Integer.valueOf(current_transaction) + Integer.valueOf(s_val));
                            }
                            transact_map.put(current_month, current_transaction);
                            shop_transact_ref.child(key).child("Transaction").updateChildren(transact_map);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addToHistory(String UID) {

        UUID uuid = UUID.randomUUID();
        String uuID = uuid.toString();

        final DatabaseReference history_ref = FirebaseDatabase.getInstance().getReference().child("Order History").child(UID).child(uuID);

//        int time = (int)System.currentTimeMillis();
//        Timestamp t_stamp = new Timestamp(time);
//        String timeStamp = t_stamp.toString();

        Date t_date = new Date();
        String timeStamp = new Timestamp(t_date.getTime()).toString();

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = simpleDateFormat.format(date);

        Map history_map = new HashMap();
        history_map.put("Status", "Ongoing");
        history_map.put("TimeStamp", timeStamp);
        history_map.put("Date", current_date);

        history_ref.child("Info").setValue(history_map);


        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                history_ref.child("Medicines").setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CartActivity.this, "Payment Successfull", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(CartActivity.this);
        startActivity(parentIntent);
        finish();
    }
}
