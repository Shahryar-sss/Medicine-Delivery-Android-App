package com.spark.medicinedelivery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.googlecode.mp4parser.h264.BTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopReportActivity extends AppCompatActivity {

    String shop_name, shopref_key, shop_phno, shop_email;
    private TextView shopname_tv;

    private BarChart barChart;
    private BarDataSet barDataSet;
    private BarData barData;

    private PieChart pieChart;
    private PieDataSet pieDataSet;
    private PieData pieData;

    private RecyclerView medview;
    private ArrayList<ViewMedCard> medlist;

    DatabaseReference dbref;
    DatabaseReference medref;

    private Button call_btn, email_btn;

    private TextView regdate_tv, lastlogindate_tv, accid_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_report);

        shop_name = getIntent().getExtras().getString("Shop_Name");

        shopname_tv = (TextView) findViewById(R.id.report_screen_shop_name);
        barChart = (BarChart) findViewById(R.id.report_screen_bar_graph);
        shopname_tv.setText(shop_name);
        pieChart = (PieChart) findViewById(R.id.report_screen_pie_chart);
        regdate_tv = (TextView) findViewById(R.id.report_screen_reg_date);
        accid_tv = (TextView) findViewById(R.id.report_screen_acc_id);
        lastlogindate_tv = (TextView) findViewById(R.id.report_screen_logged_in_date);
        medview = (RecyclerView) findViewById(R.id.report_screen_medicines_list);
        call_btn = (Button) findViewById(R.id.report_screen_call_btn);
        email_btn = (Button) findViewById(R.id.report_screen_email_btn);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        medview.setLayoutManager(layoutManager);
        medview.setHasFixedSize(true);

        final ArrayList<BarEntry> barEntries = new ArrayList<>();
        final ArrayList<String> months = new ArrayList<>();

        dbref = FirebaseDatabase.getInstance().getReference().child("Seller");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name;
                    name = ds.child("Shop_Name").getValue().toString();

                    if (name.equalsIgnoreCase(shop_name)) {
                        shopref_key = ds.getKey();
                        accid_tv.setText(shopref_key.substring(shopref_key.length() - 10, shopref_key.length()));
                        lastlogindate_tv.setText(ds.child("last_login_date").getValue().toString());
                        regdate_tv.setText(ds.child("reg_date").getValue().toString());

                        shop_phno = ds.child("Shop_Phone").getValue().toString();
                        shop_email = ds.child("email").getValue().toString();

                        DataSnapshot ds2 = ds.child("Transaction");

                        String pairs = ds2.getValue().toString();
                        pairs = pairs.substring(1, pairs.length() - 1);

                        String pair[] = pairs.split(",");

                        for (int i = 0; i < pair.length; i++)
                            pair[i] = pair[i].trim();

                        for (int i = 0; i < pair.length - 1; i++) {
                            for (int j = i + 1; j < pair.length; j++) {
                                if (pair[i].compareTo(pair[j]) > 0) {
                                    String temp = pair[i];
                                    pair[i] = pair[j];
                                    pair[j] = temp;
                                }
                            }
                        }

                        int counter = 0;
                        for (String p : pair) {
                            p = p.trim();
                            String key = p.substring(0, 7);
                            String value = p.substring(8, p.length() - 1);

                            barEntries.add(new BarEntry(counter, Integer.parseInt(value)));
                            counter++;

                            months.add(key);
                        }

                        barDataSet = new BarDataSet(barEntries, "Transaction Amount (in tens)");

                        barData = new BarData(barDataSet);
                        barChart.setData(barData);

                        barChart.getDescription().setEnabled(false);
                        barChart.getLegend().setEnabled(true);

                        barChart.animateY(4500);

                        barData.setBarWidth(0.2f);
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);
                        xAxis.setXOffset(1f);
                        xAxis.setValueFormatter(new IAxisValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                return months.get((int) value);
                            }
                        });

                        barChart.setTouchEnabled(true);
                        barChart.setDragEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ArrayList names = new ArrayList();
        final ArrayList total_transaction_value = new ArrayList();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int serial_no = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    names.add(ds.child("Shop_Name").getValue().toString());

                    String pairs = ds.child("Transaction").getValue().toString();
                    pairs = pairs.substring(1, pairs.length() - 1);
                    String pair[] = pairs.split(",");

                    int total_value = 0;

                    for (String p : pair) {
                        p = p.trim();
                        int value = Integer.parseInt(p.substring(8, p.length() - 1));
                        total_value += value;
                    }

                    total_transaction_value.add(new PieEntry(total_value, names.get(serial_no).toString()));
                    serial_no++;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        pieDataSet = new PieDataSet(total_transaction_value, "");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        int[] colors = new int[10];
        int colour_counter = 0;

        //mixing colours from two templates to create a different colours

        for (int color : ColorTemplate.JOYFUL_COLORS
        ) {
            colors[colour_counter] = color;
            colour_counter++;
        }

        for (int color : ColorTemplate.MATERIAL_COLORS
        ) {
            colors[colour_counter] = color;
            colour_counter++;
        }

        pieDataSet.setColors(colors);
        pieChart.animateXY(4500, 4500);
        pieChart.getDescription().setEnabled(false);

        //Draw Slice Text false makes the labels from inside the pie chart itself disappear
        pieChart.setDrawSliceText(false);
//        pieChart.setEntryLabelColor(Color.BLACK);

        ArrayList<LegendEntry> legendList = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.label = names.get(i).toString();
            legendList.add(entry);
        }

        pieChart.getLegend().setEnabled(true);
//        pieChart.getLegend().setCustom(legendList);
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setWordWrapEnabled(true);
        legend.setDrawInside(false);
        legend.setYOffset(5f);

        medref = FirebaseDatabase.getInstance().getReference().child("Medicine");
        medref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medlist = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("Shop").hasChild(shop_name)) {
                        ViewMedCard viewMedCard = ds.child("Info").getValue(ViewMedCard.class);
                        medlist.add(viewMedCard);
                    }
                }

                ViewMedCardAdapter adapter = new ViewMedCardAdapter(ShopReportActivity.this, medlist);
                medview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + shop_phno);
                Intent callIntent = new Intent(Intent.ACTION_CALL, uri);
                if (ActivityCompat.checkSelfPermission(ShopReportActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });

        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"+shop_email));
                //emailIntent.setType("message/rfc822");
                //emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{shop_email});
                try
                {
                    startActivity(emailIntent);
                    finish();
                }
                catch (android.content.ActivityNotFoundException e)
                {
                    Toast.makeText(ShopReportActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(ShopReportActivity.this);
        startActivity(parentIntent);
        finish();
    }
}
